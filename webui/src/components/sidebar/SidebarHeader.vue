<script setup lang="ts">

const username = 'Me'
const logout = () => {
    console.log("logout")
}
</script>

<template>
    <section class="user-section">
        <span>{{ username }}</span>
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