<script setup lang="ts">

import Sidebar from "@/components/sidebar/Sidebar.vue";
import Conversation from "@/components/conversation/Conversation.vue";
import {onUnmounted, watch} from "vue";
import router from "@/router";
import {useConversationStore} from "@/stores/conversation.ts";
import type {ConversationEvent} from "@/models/Conversation.ts";
import {useOperationStore} from "@/stores/operation.ts";

const props = defineProps({
    conversationId: String
});

const conversationStore = useConversationStore();
const operationStore = useOperationStore();

function receiveConversationEvent(event: ConversationEvent) {
    if (event.data.entryName === 'OperationProposedEvent') {
        const operationName = (event.data.parameters.operationName || '') as string;
        operationStore.markActiveOperation(operationName);
    }
}

watch(
    () => props.conversationId,
    async (requestedConversationId) => {
        conversationStore.unsubscribe()

        const conversationId = await conversationStore.startConversation(
            requestedConversationId
        )

        // The backend returns a new ID when the requested conversation
        // no longer exists, such as after chatservice restarts.
        if (conversationId !== requestedConversationId) {
            await router.replace({
                name: 'conversation',
                params: { conversationId }
            })
            return
        }

        await conversationStore.subscribeToServerUpdates(
            conversationId,
            receiveConversationEvent
        )
        await operationStore.loadAvailableOperations(conversationId)
    },
    { immediate: true }
)

onUnmounted(conversationStore.unsubscribe)

</script>

<template>
    <div class="conversation-view">
        <Sidebar/>
        <Conversation/>
    </div>
</template>

<style scoped>
.conversation-view {
    display: flex;
    height: 100vh;
}
</style>
