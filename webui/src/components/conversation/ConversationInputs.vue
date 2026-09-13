<script setup lang="ts">

import {ref} from "vue";
import {useConversationStore} from "@/stores/conversation.ts";
import {postUserMessage} from "@/services/conversation-service.ts";

const conversationStore = useConversationStore();

const userInput = ref<string>('');


const sendMessage = async () => {
    const message = userInput.value.trim()

    if (!conversationStore.isConversationStarted() || !message) return

    const conversationId = (conversationStore.conversation?.conversationId || '') as string

    await postUserMessage({
        conversationId,
        userMessage: message
    })
    
    userInput.value = ''
}

</script>

<template>
    <form class="user-input" @submit.prevent="sendMessage">
        <textarea v-model="userInput" placeholder="Message"></textarea>
        <button type="submit">Send</button>
    </form>
</template>

<style scoped>
.user-input {
    display: flex;
    gap: 0.5rem;
    padding: 1rem;
}

.user-input textarea {
    flex: 1;
}
</style>