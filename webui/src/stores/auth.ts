import {computed, ref} from 'vue'
import {defineStore} from 'pinia'

import {
    type AuthenticatedUser,
    type AuthenticationSession,
    logoutFromKeycloak,
    redirectToLogin,
    refreshAuthentication,
} from '@/auth/keycloak'

export const useAuthStore = defineStore('auth', () => {
    const accessToken = ref<string | null>(null)
    const expiresAt = ref<number | null>(null)
    const user = ref<AuthenticatedUser | null>(null)

    const isAuthenticated = computed(() => accessToken.value !== null && user.value !== null)

    function setSession(session: AuthenticationSession) {
        accessToken.value = session.accessToken
        expiresAt.value = session.expiresAt
        user.value = session.user
    }

    function clearSession() {
        accessToken.value = null
        expiresAt.value = null
        user.value = null
    }

    async function refreshSession() {
        try {
            setSession(await refreshAuthentication())
        } catch {
            clearSession()
            await redirectToLogin()
        }
    }

    async function logout() {
        clearSession()
        await logoutFromKeycloak()
    }

    return {
        accessToken,
        expiresAt,
        user,
        isAuthenticated,
        setSession,
        clearSession,
        refreshSession,
        logout,
    }
})
