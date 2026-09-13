package com.tgn.chatservice.infrastructure.adapter.out.llm;

import com.tgn.chatservice.application.adapter.ConversationEntryFactoryImpl;
import com.tgn.chatservice.application.adapter.in.service.*;
import com.tgn.chatservice.application.model.in.ApproveOperationProposalRequest;
import com.tgn.chatservice.application.port.in.usecase.ApproveOperationProposalUseCase;
import com.tgn.chatservice.application.port.in.usecase.CreateOperationApprovalRequestUseCase;
import com.tgn.chatservice.application.port.in.usecase.VerifyConversationExistsUseCase;
import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.conversation.event.OperationApprovalDecisionTakenEvent;
import com.tgn.chatservice.domain.model.conversation.message.AssistantMessage;
import com.tgn.chatservice.domain.model.operation.OperationApprovalRequestDecision;
import com.tgn.chatservice.domain.model.operation.OperationApprovalUserDecision;
import com.tgn.chatservice.domain.model.operation.OperationProposal;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationRepository;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationSubscriptionRepository;
import io.modelcontextprotocol.client.McpSyncClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import tools.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class OperationDecisionFlowTest {

    private final ChatModel chatModel = mock(ChatModel.class);
    private final McpSyncClient mcpClient = mock(McpSyncClient.class);
    private final ApproveOperationProposalUseCase approvals =
            mock(ApproveOperationProposalUseCase.class);
    private final InMemoryConversationRepository conversations =
            new InMemoryConversationRepository();
    private final InMemoryConversationSubscriptionRepository subscriptions =
            new InMemoryConversationSubscriptionRepository();
    private final List<ConversationUpdate> updates = new ArrayList<>();

    private ConversationOrchestratorService service;
    private GetConversationHistoryService history;
    private UUID conversationId;
    private UUID operationId;

    @BeforeEach
    void setUp() {
        final var start = new StartConversationService(conversations, UUID::randomUUID);
        history = new GetConversationHistoryService(conversations);
        conversationId = start.perform();
        operationId = UUID.randomUUID();
        subscriptions.create(conversationId, updates::add);

        service = new ConversationOrchestratorService(
                new OpenAiOperationOrchestrator(
                        List.of(mcpClient), chatModel, new ObjectMapper()),
                start,
                new PushConversationEntryService(conversations, subscriptions),
                new SubscribeToConversationUpdatesService(subscriptions),
                approvals,
                history,
                mock(CreateOperationApprovalRequestUseCase.class),
                mock(VerifyConversationExistsUseCase.class),
                new ConversationEntryFactoryImpl(UUID::randomUUID)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"User decision", "Operation approval request expired"})
    void informsModelAndPublishesReplyWithoutCallingMcp(String reason) {
        stubDecision(reason);
        final var reply = "The operation was not executed.";
        when(chatModel.call(any(Prompt.class))).thenReturn(new ChatResponse(List.of(
                new Generation(
                        new org.springframework.ai.chat.messages.AssistantMessage(reply))
        )));

        service.approveOperationExecution(
                conversationId, operationId, OperationApprovalUserDecision.REJECTED);

        final var prompt = ArgumentCaptor.forClass(Prompt.class);
        verify(chatModel).call(prompt.capture());
        final var messages = prompt.getValue().getInstructions();
        final var context = messages.stream()
                .map(message -> message.getText())
                .collect(Collectors.joining("\n"));

        assertTrue(context.contains("Operation: create_article"));
        assertTrue(context.contains("\"title\":\"VPN guide\""));
        assertTrue(context.contains("Approval granted: false"));
        assertTrue(context.contains("Reason: " + reason));
        assertTrue(context.contains("Execution: not performed"));
        verifyNoInteractions(mcpClient);

        final var entries = history.perform(conversationId);
        assertEquals(2, entries.size());
        assertInstanceOf(OperationApprovalDecisionTakenEvent.class, entries.getFirst());
        assertEquals(reply,
                assertInstanceOf(AssistantMessage.class, entries.getLast()).message());
        assertEquals(2, updates.size());
        assertEquals(reply, updates.getLast().data().text());
    }

    @Test
    void preservesDecisionAndPublishesFallbackWhenModelFails() {
        stubDecision("User decision");
        when(chatModel.call(any(Prompt.class)))
                .thenThrow(new IllegalStateException("Model unavailable"));

        assertDoesNotThrow(() -> service.approveOperationExecution(
                conversationId, operationId, OperationApprovalUserDecision.REJECTED));

        final var entries = history.perform(conversationId);
        assertEquals(2, entries.size());
        assertInstanceOf(OperationApprovalDecisionTakenEvent.class, entries.getFirst());
        final var reply = assertInstanceOf(AssistantMessage.class, entries.getLast());
        assertTrue(reply.message().contains("The operation was not executed."));
        assertTrue(reply.message().contains("couldn't generate a follow-up response"));
        assertEquals(reply.message(), updates.getLast().data().text());
        verifyNoInteractions(mcpClient);
    }

    private void stubDecision(String reason) {
        final var proposal = new OperationProposal(
                "create_article", "Create an article",
                Map.of("title", "VPN guide", "content", "Use the company VPN."),
                true);
        when(approvals.perform(any(ApproveOperationProposalRequest.class)))
                .thenReturn(new OperationApprovalRequestDecision(proposal, reason, false));
    }
}
