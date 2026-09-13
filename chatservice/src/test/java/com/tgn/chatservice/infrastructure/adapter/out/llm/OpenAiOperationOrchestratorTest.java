package com.tgn.chatservice.infrastructure.adapter.out.llm;

import com.tgn.chatservice.application.model.in.RequestOperationProposalRequest;
import io.modelcontextprotocol.client.McpSyncClient;
import io.modelcontextprotocol.spec.McpSchema;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.model.ChatModel;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
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

        final var request = new RequestOperationProposalRequest(List.of());
        final var result = orchestrator.requestOperationProposal(request);

        assertTrue(result.proposal().isPresent());

        final var proposal = result.proposal().get();
        assertEquals("test_tool", proposal.name());
        assertEquals("test_tool_result_proposal_reason", result.reason());
    }

    @Test
    void executeOperationTest(){
    }
}
