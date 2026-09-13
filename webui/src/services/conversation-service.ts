import type {Operation} from "@/models/Operation.ts";
import type {ConversationEventData, ConversationEventSource} from "@/models/Conversation.ts";

interface StartConversationResponse {
    conversationId: string
}

interface GetAvailableOperationsRequest {
    conversationId: string
}

interface PostUserMessageRequest {
    conversationId: string,
    userMessage: string
}

interface ApproveOperationProposalRequest {
    conversationId: string,
    operationId: string,
    decision: 'accepted' | 'rejected'
}

export async function startConversation(possibleConversationId = ''): Promise<string> {
    const response = await fetch(`/api/conversations/${encodeURIComponent(possibleConversationId)}`, {
        method: 'POST'
    });

    if(!response.ok) {
        throw new Error('Could not start a new conversation.');
    }

    const { conversationId } = await response.json() as StartConversationResponse
    return conversationId
}

export async function getAvailableOperations(request: GetAvailableOperationsRequest): Promise<Array<Operation>> {
    const response = await fetch(`/api/conversations/${encodeURIComponent(request.conversationId)}/operations`);

    if(!response.ok) {
        throw new Error('Could not get available operations for conversation.');
    }

    return await response.json() as Operation[]
}

export async function postUserMessage(request: PostUserMessageRequest): Promise<void> {
    const response = await fetch(`/api/conversations/${encodeURIComponent(request.conversationId)}/messages`, {
        headers: {
            'Content-type': 'application/json',
        },
        method: 'POST',
        body: JSON.stringify({ message: request.userMessage })
    });

    if(!response.ok) {
        throw new Error('Could not send message to conversation.');
    }
}

export async function approveOperationProposal(
    request: ApproveOperationProposalRequest
): Promise<void> {
    const response = await fetch(
        `/api/conversations/${encodeURIComponent(request.conversationId)}/operationApprovalRequests/${encodeURIComponent(request.operationId)}/decision`,
        {
            method: 'POST',
            headers: { 'Content-type': 'application/json' },
            body: JSON.stringify({ decision: request.decision })
        }
    );

    if(!response.ok){
        throw new Error('Could not approve/reject operation.')
    }
}

interface SubscribeToConversationUpdatesRequest {
    conversationId: string
    onEvent: Function
}

interface SubscribeToConversationUpdatesResponse {
    unsubscribeCallback: () => void
}

export async function subscribeToConversationsUpdates(
    request: SubscribeToConversationUpdatesRequest
): Promise<SubscribeToConversationUpdatesResponse> {
    const controller = new AbortController();

    const response = await fetch(`/api/conversations/${encodeURIComponent(request.conversationId)}/events`, {
        headers: { Accept: 'text/event-stream' },
        signal: controller.signal
    })

    if (!response.ok) {
        throw new Error(`Unable to subscribe to the conversation.`)
    }
    if (!response.body) {
        throw new Error('The conversation stream has no response body.')
    }

    readEventStream(response.body, request.onEvent).catch((error) => {
        if (!controller.signal.aborted) {
            console.error('Conversation stream failed', error)
        }
    })

    return { unsubscribeCallback: () => controller.abort() }
}

async function readEventStream(stream: ReadableStream<Uint8Array<ArrayBuffer>>, onEvent: Function) {
    const reader = stream.getReader()
    const decoder = new TextDecoder()
    let buffer = ''

    while (true) {
        const { done, value } = await reader.read()
        if (done) break

        buffer = (buffer + decoder.decode(value, { stream: true })).replace(/\r\n/g, '\n')
        const frames = buffer.split('\n\n')
        buffer = frames.pop() ?? ''

        for (const frame of frames) {
            const lines = frame.split('\n')
            const id = lines.find((line) => line.startsWith('id:'))?.slice(3).trim()
            const source = lines.find((line) => line.startsWith('event:'))?.slice(6).trim()
            const data = lines.find((line) => line.startsWith('data:'))?.slice(5).trimStart()

            if (id && data && (source === 'USER' || source === 'ASSISTANT' || source === 'EVENT')) {
                onEvent({
                    id,
                    source: source as ConversationEventSource,
                    data: JSON.parse(data) as ConversationEventData,
                })
            }
        }
    }
}