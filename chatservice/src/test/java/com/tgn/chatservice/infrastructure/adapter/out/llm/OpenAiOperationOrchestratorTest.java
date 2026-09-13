package com.tgn.chatservice.infrastructure.adapter.out.llm;

import com.tgn.chatservice.application.model.in.ExecuteOperationRequest;
import com.tgn.chatservice.application.model.in.RequestOperationProposalRequest;
import com.tgn.chatservice.domain.model.operation.OperationProposal;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.tool.execution.ToolExecutionException;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class OpenAiOperationOrchestratorTest {

    @Test
    void getAvailableToolsTest(){
        final var itKnowledgeBaseMcpClient = mock(McpSyncClient.class);
        final var chatModel = mock(ChatModel.class);

        final var mcpClients = List.of(itKnowledgeBaseMcpClient);

        final var listToolsResult = mock(McpSchema.ListToolsResult.class);
        final var testTool = mock(McpSchema.Tool.class);

        when(itKnowledgeBaseMcpClient.isInitialized()).thenReturn(true);

        when(itKnowledgeBaseMcpClient.listTools()).thenReturn(listToolsResult);
        when(listToolsResult.tools()).thenReturn(List.of(testTool));

        when(testTool.name()).thenReturn("test_tool");
        when(testTool.description()).thenReturn("test_tool_description");
        when(testTool.inputSchema()).thenReturn(Map.of());

        final var orchestrator = new OpenAiOperationOrchestrator(
                mcpClients,
                chatModel,
                new ObjectMapper()
        );

        final var result = orchestrator.getAvailableOperations();

        assertEquals(1, result.size());
        assertEquals("test_tool", result.getFirst().name());
    }

    @Test
    void getOperationProposalTest(){
        final var itKnowledgeBaseMcpClient = mock(McpSyncClient.class);
        final var chatModel = mock(ChatModel.class);

        final var mcpClients = List.of(itKnowledgeBaseMcpClient);

        final var listToolsResult = mock(McpSchema.ListToolsResult.class);
        final var testTool = mock(McpSchema.Tool.class);

        when(itKnowledgeBaseMcpClient.isInitialized()).thenReturn(true);
        when(itKnowledgeBaseMcpClient.getClientCapabilities())
                .thenReturn(McpSchema.ClientCapabilities.builder().build());
        when(itKnowledgeBaseMcpClient.getClientInfo())
                .thenReturn(McpSchema.Implementation.builder("test-client", "1.0").build());

        when(itKnowledgeBaseMcpClient.listTools()).thenReturn(listToolsResult);
        when(listToolsResult.tools()).thenReturn(List.of(testTool));

        when(testTool.name()).thenReturn("test_tool");
        when(testTool.description()).thenReturn("test_tool_description");
        when(testTool.inputSchema()).thenReturn(Map.of());

        final var assistantMessage = AssistantMessage.builder()
                .content("test_tool_result_proposal_reason")
                .toolCalls(List.of(new AssistantMessage.ToolCall(
                        "call-1",
                        "function",
                        "test_tool",
                        "{}"
                )))
                .build();

        when(chatModel.call(any(Prompt.class)))
                .thenReturn(new ChatResponse(List.of(
                        new Generation(assistantMessage)
                )));

        final var orchestrator = new OpenAiOperationOrchestrator(
                mcpClients,
                chatModel,
                new ObjectMapper()
        );

        final var request = new RequestOperationProposalRequest(List.of());
        final var result = orchestrator.requestOperationProposal(request);

        assertTrue(result.proposal().isPresent());

        final var proposal = result.proposal().get();
        assertEquals("test_tool", proposal.name());
        assertEquals("test_tool_result_proposal_reason", result.reason());
    }

    @Test
    void executeOperationTest(){
        final var mcpClient = mockCreateArticleClient(false);
        final var chatModel = mock(ChatModel.class);
        final var arguments = Map.<String, Object>of(
                "title", "Coding Standards",
                "content", "All code must be readable."
        );
        final var proposal = new OperationProposal(
                "create_article", "Create an article", arguments, true
        );
        final var expectedReply = "Created the article Coding-Standards.";

        when(chatModel.call(any(Prompt.class)))
                .thenReturn(new ChatResponse(List.of(
                        new Generation(new AssistantMessage(expectedReply))
                )));

        final var orchestrator = new OpenAiOperationOrchestrator(
                List.of(mcpClient), chatModel, new ObjectMapper()
        );
        final var result = orchestrator.executeOperation(
                new ExecuteOperationRequest(proposal, List.of())
        );

        final var call = ArgumentCaptor.forClass(McpSchema.CallToolRequest.class);
        verify(mcpClient).callTool(call.capture());
        assertEquals("create_article", call.getValue().name());
        assertEquals(arguments, call.getValue().arguments());

        final var prompt = ArgumentCaptor.forClass(Prompt.class);
        verify(chatModel).call(prompt.capture());
        final var outcome = prompt.getValue().getInstructions().getLast().getText();
        assertTrue(outcome.contains("Executed operation: create_article"));
        assertTrue(outcome.contains("\"title\":\"Coding Standards\""));
        assertTrue(outcome.contains("\"content\":\"All code must be readable.\""));
        assertTrue(outcome.contains("Execution outcome: succeeded"));
        assertTrue(outcome.contains("Coding-Standards"));
        assertEquals(expectedReply, result.assistantResponse());
    }

    @Test
    void failedMcpCallDoesNotGenerateSuccessResponse() {
        final var mcpClient = mockCreateArticleClient(true);
        final var chatModel = mock(ChatModel.class);
        final var proposal = new OperationProposal(
                "create_article",
                "Create an article",
                Map.of("title", "Coding Standards", "content", "Draft"),
                true
        );
        final var orchestrator = new OpenAiOperationOrchestrator(
                List.of(mcpClient), chatModel, new ObjectMapper()
        );

        assertThrows(ToolExecutionException.class, () ->
                orchestrator.executeOperation(
                        new ExecuteOperationRequest(proposal, List.of())
                ));

        verifyNoInteractions(chatModel);
    }

    private McpSyncClient mockCreateArticleClient(boolean isError) {
        final var client = mock(McpSyncClient.class);
        final var tools = mock(McpSchema.ListToolsResult.class);
        final var tool = mock(McpSchema.Tool.class);

        when(client.isInitialized()).thenReturn(true);
        when(client.getClientCapabilities())
                .thenReturn(McpSchema.ClientCapabilities.builder().build());
        when(client.getClientInfo())
                .thenReturn(McpSchema.Implementation.builder("test-client", "1.0").build());
        when(client.listTools()).thenReturn(tools);
        when(tools.tools()).thenReturn(List.of(tool));
        when(tool.name()).thenReturn("create_article");
        when(tool.description()).thenReturn("Create an article");
        when(tool.inputSchema()).thenReturn(Map.of("type", "object"));

        final var content = isError
                ? "Unable to create article"
                : """
                  {"id":"article-1","title":"Coding-Standards",
                   "description":"All code must be readable."}
                  """;
        when(client.callTool(any(McpSchema.CallToolRequest.class)))
                .thenReturn(McpSchema.CallToolResult.builder()
                        .isError(isError)
                        .addTextContent(content)
                        .build());

        return client;
    }
}
