<script setup lang="ts">

import SidebarHeader from "@/components/sidebar/SidebarHeader.vue";
import SidebarContent from "@/components/sidebar/SidebarContent.vue";
</script>

<template>
    <aside class="sidebar">
        <SidebarHeader/>
        <SidebarContent/>
    </aside>
</template>

<style scoped>
.sidebar {
    background: var(--lgt-blue);
    color: #ffffff;
    display: flex;
    flex: 0 0 18%;
    flex-direction: column;
    gap: 2rem;
    min-width: 0;
    padding: 1.75rem 1.25rem;
    width: 18%;
}

@media (max-width: 48rem) {
    .sidebar {
        flex-basis: 27%;
        width: 27%;
    }
}
</style>
