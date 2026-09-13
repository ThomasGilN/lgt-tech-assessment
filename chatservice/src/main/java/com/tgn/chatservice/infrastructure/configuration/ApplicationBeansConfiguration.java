package com.tgn.chatservice.infrastructure.configuration;

import com.tgn.chatservice.application.adapter.ConversationEntryFactoryImpl;
import com.tgn.chatservice.application.adapter.RandomUUIDIdGenerator;
import com.tgn.chatservice.application.adapter.in.service.*;
import com.tgn.chatservice.application.port.in.orchestrator.ConversationOrchestrator;
import com.tgn.chatservice.application.port.in.usecase.*;
import com.tgn.chatservice.application.port.out.llm.OperationOrchestrator;
import com.tgn.chatservice.application.port.out.repository.ConversationRepository;
import com.tgn.chatservice.application.port.out.repository.ConversationSubscriptionRepository;
import com.tgn.chatservice.application.port.out.repository.OperationApprovalRepository;
import com.tgn.chatservice.domain.model.ConversationEntryFactory;
import com.tgn.chatservice.domain.model.IdGenerator;
import com.tgn.chatservice.infrastructure.adapter.out.llm.OpenAiOperationOrchestrator;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationRepository;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryConversationSubscriptionRepository;
import com.tgn.chatservice.infrastructure.adapter.out.repository.InMemoryOperationApprovalRepository;
import io.modelcontextprotocol.client.McpSyncClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

@Configuration
public class ApplicationBeansConfiguration {

    @Bean
    public IdGenerator idGenerator() {
        return new RandomUUIDIdGenerator();
    }

    @Bean
    public ConversationEntryFactory conversationEntryFactory(
            IdGenerator idGenerator
    ) {
        return new ConversationEntryFactoryImpl(idGenerator);
    }

    @Bean
    public ConversationRepository conversationRepository() {
        return new InMemoryConversationRepository();
    }

    @Bean
    public ConversationSubscriptionRepository conversationSubscriptionRepository() {
        return new InMemoryConversationSubscriptionRepository();
    }

    @Bean
    public OperationApprovalRepository operationApprovalRepository() {
        return new InMemoryOperationApprovalRepository();
    }

    @Bean
    public OperationOrchestrator operationOrchestrator(
            ObjectProvider<List<McpSyncClient>> mcpClientsProvider,
            ChatModel chatModel,
            ObjectMapper objectMapper
    ) {
        return new OpenAiOperationOrchestrator(
                mcpClientsProvider.getIfAvailable(List::of),
                chatModel,
                objectMapper
        );
    }

    @Bean
    public StartConversationUseCase startConversationUseCase(
            ConversationRepository conversationRepository,
            IdGenerator idGenerator
    ) {
        return new StartConversationService(conversationRepository, idGenerator);
    }

    @Bean
    public PushConversationEntryUseCase pushConversationEntryUseCase(
            ConversationRepository conversationRepository,
            ConversationSubscriptionRepository conversationSubscriptionRepository
    ) {
        return new PushConversationEntryService(conversationRepository, conversationSubscriptionRepository);
    }

    @Bean
    public SubscribeToConversationUpdatesUseCase subscribeToConversationUpdatesUseCase(
            ConversationSubscriptionRepository conversationSubscriptionRepository
    ) {
        return new SubscribeToConversationUpdatesService(conversationSubscriptionRepository);
    }

    @Bean
    public GetConversationHistoryUseCase getConversationHistoryUseCase(
            ConversationRepository conversationRepository
    ) {
        return new GetConversationHistoryService(conversationRepository);
    }

    @Bean
    public CreateOperationApprovalRequestUseCase createOperationApprovalRequestUseCase(
            OperationApprovalRepository operationApprovalRepository,
            IdGenerator idGenerator
    ) {
        return new CreateOperationApprovalRequestService(operationApprovalRepository, idGenerator);
    }

    @Bean
    public ApproveOperationProposalUseCase approveOperationProposalUseCase(
            OperationApprovalRepository operationApprovalRepository
    ) {
        return new ApproveOperationProposalService(operationApprovalRepository);
    }

    @Bean
    public ConversationOrchestrator conversationOrchestrator(
            OperationOrchestrator operationOrchestrator,
            StartConversationUseCase startConversationUseCase,
            PushConversationEntryUseCase pushConversationEntryUseCase,
            SubscribeToConversationUpdatesUseCase subscribeToConversationUpdatesUseCase,
            ApproveOperationProposalUseCase approveOperationProposalUseCase,
            GetConversationHistoryUseCase getConversationHistoryUseCase,
            CreateOperationApprovalRequestUseCase createOperationApprovalRequestUseCase,
            ConversationEntryFactory conversationEntryFactory
    ) {
        return new ConversationOrchestratorService(
                operationOrchestrator,
                startConversationUseCase,
                pushConversationEntryUseCase,
                subscribeToConversationUpdatesUseCase,
                approveOperationProposalUseCase,
                getConversationHistoryUseCase,
                createOperationApprovalRequestUseCase,
                conversationEntryFactory
        );
    }
}
