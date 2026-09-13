<script setup lang="ts">

import {useConversationStore} from "@/stores/conversation.ts";
import type {ConversationEvent} from "@/models/Conversation.ts";
import {approveOperationProposal} from "@/services/conversation-service.ts";
import {nextTick, ref, watch} from "vue";

const conversationStore = useConversationStore();
const messagesContainer = ref<HTMLElement | null>(null)

watch(
    () => conversationStore.conversation?.history.length ?? 0,
    async (messagesCount, previousMessageCount) => {
        if(messagesCount <= previousMessageCount) {
            return
        }

        await nextTick()

        if (messagesContainer.value) {
            messagesContainer.value.scrollTop = messagesContainer.value.scrollHeight
        }
    }
)

const sendOperationDecision = async (event: ConversationEvent, decision: 'accepted' | 'rejected') => {
    if(!conversationStore.isConversationStarted()){
        return
    }

    const operationId = event.data.parameters.operationId as string
    const conversationId = conversationStore.conversation?.conversationId as string

    await approveOperationProposal({
        conversationId,
        operationId,
        decision
    })
}
</script>

<template>
    <section ref="messagesContainer" class="messages">
        <template
            v-for="event in conversationStore.conversation?.history"
            :key="event.id"
        >
            <hr v-if="event.source === 'ASSISTANT'" class="assistant-separator" />

            <div class="message" :class="event.source.toLowerCase()">
                {{ event.data.text }}

                <div
                    v-if="event.data.entryName === 'OperationApprovalRequestedEvent'"
                    class="approval-actions"
                >
                    <button
                        type="button"
                        @click="sendOperationDecision(event, 'accepted')"
                    >
                        Approve
                    </button>
                    <button
                        type="button"
                        @click="sendOperationDecision(event, 'rejected')"
                    >
                        Deny
                    </button>
                </div>
            </div>
        </template>
    </section>
</template>

<style scoped>
.messages {
    display: flex;
    flex: 1;
    flex-direction: column;
    gap: 0.875rem;
    overflow-y: auto;
    padding: 2.5rem 7%;
}

.message {
    border: 1px solid var(--lgt-border);
    border-radius: 0.5rem;
    box-shadow: 0 0.125rem 0.5rem rgba(23, 32, 61, 0.04);
    line-height: 1.6;
    max-width: min(75%, 48rem);
    padding: 1rem 1.125rem;
    white-space: pre-wrap;
}

.assistant-separator {
    border: 0;
    border-top: 1px solid var(--lgt-border);
    margin: 1rem 0 0.125rem;
    width: 100%;
}

.approval-actions {
    display: flex;
    gap: 0.75rem;
    margin-top: 1rem;
}

.approval-actions button {
    background: var(--lgt-blue);
    border: 1px solid var(--lgt-blue);
    border-radius: 0.25rem;
    color: #ffffff;
    flex: 1;
    font-weight: 700;
    padding: 0.625rem 0.875rem;
}

.approval-actions button:hover {
    background: var(--lgt-blue-dark);
}

.approval-actions button + button {
    background: #ffffff;
    border-color: var(--lgt-danger);
    color: var(--lgt-danger);
}

.approval-actions button + button:hover {
    background: #fff4f5;
}

.assistant {
    align-self: flex-start;
    background: var(--lgt-surface);
}

.user {
    align-self: flex-end;
    background: var(--lgt-blue-soft);
    border-color: #cbd7f7;
}

.event {
    align-self: center;
    background: rgba(255, 255, 255, 0.72);
    backdrop-filter: blur(0.375rem);
    -webkit-backdrop-filter: blur(0.375rem);
    border-left: 0.1875rem solid #9aa9d4;
    box-shadow: none;
    color: var(--lgt-muted);
    font-size: 0.8125rem;
    line-height: 1.45;
    max-width: min(64%, 36rem);
    opacity: 0.82;
    padding: 0.6875rem 0.875rem;
}

@media (max-width: 48rem) {
    .messages {
        padding: 1.25rem 5%;
    }

    .message {
        max-width: 88%;
    }
}
</style>
