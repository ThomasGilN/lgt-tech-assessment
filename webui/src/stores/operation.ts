import {defineStore} from "pinia";
import {ref} from "vue";
import type {Operation} from "@/models/Operation.ts";

import * as conversationService from '@/services/conversation-service.ts'


export const useOperationStore = defineStore('operation', () => {
    const operations = ref<Operation[]>([]);
    const activeOperationName = ref<string>('');

    async function loadAvailableOperations(conversationId: string): Promise<void> {
        operations.value = await conversationService.getAvailableOperations({
            conversationId
        })
        activeOperationName.value = ''
    }

    function markActiveOperation(operationName: string): void {
        activeOperationName.value = operationName
    }

    return {
        loadAvailableOperations,
        markActiveOperation,
        operations,
        activeOperationName
    }
})