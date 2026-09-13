package com.tgn.chatservice.infrastructure.adapter.in.web;


import com.tgn.chatservice.application.port.in.orchestrator.ConversationOrchestrator;
import com.tgn.chatservice.domain.model.conversation.ConversationUpdate;
import com.tgn.chatservice.domain.model.operation.Operation;
import com.tgn.chatservice.domain.model.operation.OperationApprovalUserDecision;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;

@RestController
@RequestMapping("api/conversations")
public class ConversationRestController {

    private final ConversationOrchestrator conversationOrchestrator;

    public ConversationRestController(ConversationOrchestrator conversationOrchestrator) {
        this.conversationOrchestrator = conversationOrchestrator;
    }

    @GetMapping("/{conversationId}/operations")
    public Mono<ResponseEntity<List<Operation>>> getAvailableOperations() {
        return toMono(() -> ResponseEntity.ok(conversationOrchestrator.getAvailableOperations()));
    }

    @PostMapping({"", "/", "/{conversationId}"})
    public Mono<ResponseEntity<StartConversationResponse>> createConversation(
            @PathVariable(required = false, value = "conversationId") UUID possibleExistingConversationId
    ) {
        return toMono(() -> {
            var conversationId = conversationOrchestrator.startConversation(possibleExistingConversationId);
            var location = URI.create("/api/conversations/" + conversationId);
            return ResponseEntity
                    .created(location)
                    .body(new StartConversationResponse(conversationId));
        });
    }

    @PostMapping("/{conversationId}/messages")
    public Mono<ResponseEntity<Void>> postUserMessage(
            @PathVariable("conversationId") UUID conversationId,
            @RequestBody(required = false) PostUserMessageRequestBody requestBody
    ) {
        return toMono(() -> {
            conversationOrchestrator.pushUserMessage(conversationId, requestBody.message());
            return ResponseEntity.accepted().build();
        });
    }

    @GetMapping(value = "/{conversationId}/events", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<?>> conversationEvents(
            @PathVariable("conversationId") UUID conversationId
    ){
        return Flux.<ConversationUpdate>create(sink -> {
            final var subscription = conversationOrchestrator.subscribeToConversationUpdates(conversationId, sink::next);
            sink.onDispose(subscription::unsubscribe);
        }).map(this::toSSE);
    }

    @PostMapping("/{conversationId}/operationApprovalRequests/{operationId}/decision")
    public Mono<ResponseEntity<Void>> operationDecision(
            @PathVariable("conversationId") UUID conversationId,
            @PathVariable("operationId") UUID operationId,
            @RequestBody(required = false) OperationApprovalRequestDecisionRequestBody requestBody
    ) {
        return toMono(() -> {
            final var decision = "accepted".equals(requestBody.decision())
                    ? OperationApprovalUserDecision.APPROVED
                    : OperationApprovalUserDecision.REJECTED;
            conversationOrchestrator.approveOperationExecution(conversationId, operationId, decision);
            return ResponseEntity.accepted().build();
        });
    }

    private <T> Mono<ResponseEntity<T>> toMono(Supplier<ResponseEntity<T>> supplier) {
        return Mono.fromCallable(supplier::get).subscribeOn(Schedulers.boundedElastic());
    }

    private ServerSentEvent<?> toSSE(ConversationUpdate conversationUpdate) {
        return ServerSentEvent.builder()
                .id(conversationUpdate.id().toString())
                .event(conversationUpdate.source().toString())
                .data(conversationUpdate.data())
                .build();
    }

    public record StartConversationResponse(UUID conversationId) {}

    public record PostUserMessageRequestBody(String message){}

    public record OperationApprovalRequestDecisionRequestBody(String decision){}
}
