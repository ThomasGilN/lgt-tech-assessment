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
    border-right: 1px solid currentColor;
    display: flex;
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
    width: 15vw;
}
</style>