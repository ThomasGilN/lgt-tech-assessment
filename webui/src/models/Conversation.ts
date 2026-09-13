export type ConversationEventSource = 'USER' | 'ASSISTANT' | 'EVENT'

export interface ConversationEventData {
    entryName: string
    text: string
    parameters: Record<string, unknown>
    timestamp: string
}

export interface ConversationEvent {
    id: string
    source: ConversationEventSource
    data: ConversationEventData
}

export interface Conversation {
    id: string
    events: ConversationEvent[]
}
