import Keycloak, {type KeycloakTokenParsed} from 'keycloak-js'

export interface AuthenticatedUser {
    id: string
    username: string
    displayName: string
    firstName?: string
    lastName?: string
    email?: string
}

export interface AuthenticationSession {
    accessToken: string
    expiresAt: number | null
    user: AuthenticatedUser
}

export const keycloak = new Keycloak({
    url: import.meta.env.VITE_KEYCLOAK_URL ?? 'http://localhost:8081',
    realm: import.meta.env.VITE_KEYCLOAK_REALM ?? 'document-chat',
    clientId: import.meta.env.VITE_KEYCLOAK_CLIENT_ID ?? 'chat-ui',
})

function stringClaim(token: KeycloakTokenParsed, name: string): string | undefined {
    const value = token[name]
    return typeof value === 'string' && value.length > 0 ? value : undefined
}

export function readAuthenticationSession(): AuthenticationSession {
    const accessToken = keycloak.token
    const token = keycloak.tokenParsed
    const subject = token?.sub ?? keycloak.subject

    if (!accessToken || !token || !subject) {
        throw new Error('Keycloak did not provide a complete authenticated session')
    }

    const email = stringClaim(token, 'email')
    const firstName = stringClaim(token, 'given_name')
    const lastName = stringClaim(token, 'family_name')
    const username = stringClaim(token, 'preferred_username') ?? email ?? subject
    const fullName = [firstName, lastName].filter(Boolean).join(' ')
    const displayName = stringClaim(token, 'name') ?? (fullName || username)

    return {
        accessToken,
        expiresAt: typeof token.exp === 'number' ? token.exp * 1_000 : null,
        user: {
            id: subject,
            username,
            displayName,
            firstName,
            lastName,
            email,
        },
    }
}

export async function initializeAuthentication(): Promise<AuthenticationSession> {
    const authenticated = await keycloak.init({
        onLoad: 'login-required',
        flow: 'standard',
        pkceMethod: 'S256',
        checkLoginIframe: false,
    })

    if (!authenticated) {
        await keycloak.login()
        throw new Error('Keycloak did not authenticate the user')
    }

    return readAuthenticationSession()
}

export async function refreshAuthentication(): Promise<AuthenticationSession> {
    await keycloak.updateToken(30)
    return readAuthenticationSession()
}

export async function redirectToLogin(): Promise<void> {
    await keycloak.login()
}

export async function logoutFromKeycloak(): Promise<void> {
    await keycloak.logout({ redirectUri: window.location.origin })
}
