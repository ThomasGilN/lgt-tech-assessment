import {createRouter, createWebHistory} from 'vue-router'

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            name: 'conversation',
            path: '/conversation/:conversationId?',
            props: true,
            component: () => import('@/views/ConversationView.vue')
        },
        {
            path: '/:pathMatch(.*)*',
            redirect: { name: 'conversation' }
        },
    ],
})

export default router
