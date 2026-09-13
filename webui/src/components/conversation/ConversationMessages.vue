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
    gap: 1rem;
    overflow-y: auto;
    padding: 1rem;
}

.message {
    max-width: 70%;
}

.assistant-separator {
    width: 100%;
}

.approval-actions {
    display: flex;
    justify-content: space-evenly;
}

.approval-actions button {
    flex: 1;
    margin: 0 1rem;
}

.assistant {
    align-self: flex-start;
}

.user {
    align-self: flex-end;
}

.event {
    align-self: center;
}
</style>