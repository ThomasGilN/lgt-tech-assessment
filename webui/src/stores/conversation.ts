import {defineStore} from "pinia";
import * as conversationService from '@/services/conversation-service.ts'
import {ref} from "vue";
import type {ConversationEvent} from "@/models/Conversation.ts";

interface Conversation {
    conversationId: string,
    history: ConversationEvent[]
}

interface ConversationSubscription {
    conversationId: string,
    unsubscribeCallback: () => void
}

export const useConversationStore = defineStore('conversation', () => {

    const conversation = ref<Conversation>();
    const unsubscribeCallback = ref<() => void>();

    async function startConversation(possibleConversationId?: string): Promise<string> {
        const conversationId = await conversationService.startConversation(possibleConversationId);

        conversation.value = {
            conversationId,
            history: []
        }

        return conversationId;
    }

    async function subscribeToServerUpdates(
        conversationId: string,
        onUpdate: (updateEvent: ConversationEvent) => void
    ): Promise<void> {
        if(!isConversationStarted()){
            throw new Error('Attempting to listen to a non started conversation');
        }

        if(conversation.value?.conversationId !== conversationId){
            throw new Error('Attempting to listen to other conversation');
        }

        const subscription = await conversationService.subscribeToConversationsUpdates({
            conversationId,
            onEvent: onUpdateAddToHistory(onUpdate)
        });

        unsubscribeCallback.value = subscription.unsubscribeCallback
    }

    function unsubscribe(){
        unsubscribeCallback.value?.();
        unsubscribeCallback.value = undefined;
    }

    function onUpdateAddToHistory(onUpdate: (updateEvent:ConversationEvent) => void): (updateEvent:ConversationEvent) => void {
        return (updateEvent: ConversationEvent) => {
            conversation.value?.history.push(updateEvent);
            onUpdate(updateEvent);
        }
    }

    function isConversationStarted(): boolean {
        if(!conversation.value) return false;
        return conversation.value.conversationId.length > 0;
    }

    return {
        startConversation,
        subscribeToServerUpdates,
        isConversationStarted,
        unsubscribe,
        conversation
    }
})
