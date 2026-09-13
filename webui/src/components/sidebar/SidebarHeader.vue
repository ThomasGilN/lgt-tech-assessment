<script setup lang="ts">

import {useAuthStore} from "@/stores/auth.ts";
import {useConversationStore} from "@/stores/conversation.ts";

const authStore = useAuthStore();
const conversationStore = useConversationStore();
const logout = async () => {
    conversationStore.unsubscribe()
    await logout()
}
</script>

<template>
    <section class="user-section">
        <span>{{ authStore.user?.displayName }}</span>
        <button type="button" @click="logout">Logout</button>
    </section>
</template>

<style scoped>
.user-section {
    display: flex;
    align-items: center;
    justify-content: space-between;
}
</style>