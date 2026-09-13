<script setup lang="ts">

import {ref} from "vue";
import {useConversationStore} from "@/stores/conversation.ts";
import {postUserMessage} from "@/services/conversation-service.ts";

const conversationStore = useConversationStore();

const userInput = ref<string>('');


const sendMessage = async () => {
    const message = userInput.value.trim()

    if (!conversationStore.isConversationStarted() || !message || conversationStore.isAwaitingAssistant) return

    const conversationId = (conversationStore.conversation?.conversationId || '') as string

    conversationStore.waitForAssistant()

    try {
        await postUserMessage({
            conversationId,
            userMessage: message
        })

        userInput.value = ''
    } catch (error) {
        conversationStore.stopWaitingForAssistant()
        throw error
    }
}

</script>

<template>
    <form class="user-input" @submit.prevent="sendMessage">
        <textarea
            v-model="userInput"
            :disabled="conversationStore.isAwaitingAssistant"
            aria-label="Message"
            placeholder="Write a message"
        ></textarea>
        <button :disabled="conversationStore.isAwaitingAssistant" type="submit">Send</button>
    </form>
</template>

<style scoped>
.user-input {
    background: var(--lgt-surface);
    border-top: 1px solid var(--lgt-border);
    display: flex;
    gap: 0.75rem;
    padding: 1rem 7%;
}

.user-input textarea {
    background: #ffffff;
    border: 1px solid var(--lgt-border);
    border-radius: 0.375rem;
    color: var(--lgt-ink);
    flex: 1;
    line-height: 1.45;
    min-height: 2.75rem;
    padding: 0.625rem 0.75rem;
    resize: vertical;
}

.user-input textarea::placeholder {
    color: var(--lgt-muted);
}

.user-input textarea:hover {
    border-color: var(--color-border-hover);
}

.user-input textarea:disabled {
    background: #f0f2f7;
    color: var(--lgt-muted);
    cursor: not-allowed;
}

.user-input button {
    margin: auto;
    align-self: flex-end;
    background: var(--lgt-blue);
    border: 1px solid var(--lgt-blue);
    border-radius: 0.25rem;
    color: #ffffff;
    font-weight: 700;
    min-height: 2.75rem;
    padding: 0.625rem 1.25rem;
}

.user-input button:hover {
    background: var(--lgt-blue-dark);
    border-color: var(--lgt-blue-dark);
}

.user-input button:disabled {
    background: #aebce0;
    border-color: #aebce0;
    cursor: wait;
}

@media (max-width: 48rem) {
    .user-input {
        padding: 0.75rem 5%;
    }
}
</style>
