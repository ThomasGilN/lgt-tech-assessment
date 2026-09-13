<script setup lang="ts">

import {useAuthStore} from "@/stores/auth.ts";
import {useConversationStore} from "@/stores/conversation.ts";

const authStore = useAuthStore();
const conversationStore = useConversationStore();
const logout = async () => {
    conversationStore.unsubscribe()
    await authStore.logout()
}
</script>

<template>
    <section class="user-section">
        <div class="user-controls">
            <span class="user-name">{{ authStore.user?.displayName }}</span>
            <button type="button" @click="logout">Sign out</button>
        </div>
    </section>
</template>

<style scoped>
.user-section {
    display: flex;
    flex-direction: column;
    gap: 1.5rem;
}

.user-name {
    display: block;
    font-size: 0.875rem;
    overflow-wrap: anywhere;
}

.user-controls {
    display: flex;
    align-items: flex-start;
    flex-direction: column;
    gap: 0.5rem;
    border-bottom: 1px solid rgba(255, 255, 255, 0.26);
    padding-bottom: 1rem;
}

button {
    background: transparent;
    border: 0;
    color: #dbe4ff;
    font-size: 0.8125rem;
    padding: 0;
    text-decoration: underline;
    text-underline-offset: 0.2rem;
}

button:hover {
    color: #ffffff;
}
</style>
