import './assets/main.css'

import {createApp} from 'vue'
import {createPinia} from 'pinia'

import App from './App.vue'
import {initializeAuthentication, keycloak, readAuthenticationSession,} from './auth/keycloak'
import {useAuthStore} from './stores/auth'

async function bootstrap() {
    const initialSession = await initializeAuthentication()
    const pinia = createPinia()
    const authStore = useAuthStore(pinia)

    authStore.setSession(initialSession)

    keycloak.onAuthRefreshSuccess = () => {
        authStore.setSession(readAuthenticationSession())
    }
    keycloak.onAuthLogout = authStore.clearSession
    keycloak.onTokenExpired = () => {
        void authStore.refreshSession()
    }

    const { default: router } = await import('./router')
    const app = createApp(App)

    app.use(pinia)
    app.use(router)
    app.mount('#app')
}

try {
    await bootstrap();
} catch (e) {
    console.error('Unable to initialize authentication', e)

    const message = document.createElement('p')
    message.textContent = 'Unable to initialize authentication. Check that Keycloak is running.'
    document.querySelector('#app')?.replaceChildren(message)
}