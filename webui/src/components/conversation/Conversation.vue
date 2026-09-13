<script setup lang="ts">
import ConversationMessages from "@/components/conversation/ConversationMessages.vue";
import ConversationInputs from "@/components/conversation/ConversationInputs.vue";
</script>

<template>
    <main class="conversation">
        <ConversationMessages/>
        <ConversationInputs/>
    </main>
</template>

<style scoped>
.conversation {
    background: var(--lgt-canvas);
    display: flex;
    flex: 1;
    flex-direction: column;
    min-width: 0;
}
</style>
