<script setup lang="ts">

import {useOperationStore} from "@/stores/operation.ts";

const operationStore = useOperationStore();

const toHumanReadable = (string: string) => string.replaceAll("_", " ")

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
                {{ toHumanReadable(operation.name) }}
            </li>
        </ul>
    </section>
</template>

<style scoped>
.operations-section {
    flex: 1;
    overflow-y: auto;
}

h2 {
    color: #dbe4ff;
    font-size: 0.6875rem;
    font-weight: 700;
    letter-spacing: 0.1em;
    margin-bottom: 0.75rem;
    text-transform: uppercase;
}

ul {
    display: flex;
    flex-direction: column;
    gap: 0.25rem;
    list-style: none;
}

li {
    border-left: 0.1875rem solid transparent;
    color: #ffffff;
    font-size: 0.875rem;
    padding: 0.6875rem 0.75rem;
    overflow-wrap: anywhere;
}

.active-operation {
    background-color: rgba(255, 255, 255, 0.14);
    border-left-color: #ffffff;
    font-weight: 700;
}
</style>
