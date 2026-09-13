<script setup lang="ts">

import {useOperationStore} from "@/stores/operation.ts";

const operationStore = useOperationStore();
</script>

<template>
    <section class="operations-section">
        <h2>Operations</h2>
        <ul>
            <li
                v-for="operation in operationStore.operations"
                :key="operation.name"
                :class="{ 'active-operation': operation.name === operationStore.activeOperationName }"
            >
                {{ operation.name }}
            </li>
        </ul>
    </section>
</template>

<style scoped>
.operations-section {
    flex: 1;
    overflow-y: auto;
}
.active-operation {
    background-color: var(--color-background-mute);
}
</style>