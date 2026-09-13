<script setup lang="ts">

import Sidebar from "@/components/sidebar/Sidebar.vue";
import Conversation from "@/components/conversation/Conversation.vue";
import {onMounted, onUnmounted} from "vue";
import router from "@/router";
import {useConversationStore} from "@/stores/conversation.ts";
import type {ConversationEvent} from "@/models/Conversation.ts";
import {useOperationStore} from "@/stores/operation.ts";

const props = defineProps({
    conversationId: String
});

const conversationStore = useConversationStore();
const operationStore = useOperationStore();

let unsubscribeCallback = () => {}

function receiveConversationEvent(event: ConversationEvent) {
    if (event.data.entryName === 'OperationProposedEvent') {
        const operationName = (event.data.parameters.operationName || '') as string;
        operationStore.markActiveOperation(operationName);
    }
}

onMounted(async () => {
    const { conversationId = '' } = props

    if(conversationId.length === 0) {
        const newConversationId = await conversationStore.startConversation();
        router.replace({
            name: 'conversation',
            params: { conversationId: newConversationId }
        })
    } else {
        await conversationStore.startConversation(conversationId);
        const subscription = await conversationStore.subscribeToServerUpdates(
            conversationId,
            receiveConversationEvent
        );
        unsubscribeCallback = subscription.unsubscribeCallback;

        await operationStore.loadAvailableOperations(conversationId);
    }
})

onUnmounted(unsubscribeCallback)

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