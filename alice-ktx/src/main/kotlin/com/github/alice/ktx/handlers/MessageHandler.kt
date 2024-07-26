package com.github.alice.ktx.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

data class EventMessage(
    val state: String?,
    val stateData: Map<String, String>,
    val message: MessageRequest
)

fun Dispatcher.message(
    event: suspend EventMessage.() -> Boolean = { true },
    handle: suspend Request.() -> MessageResponse
) {
    val messageHandler = MessageHandler(
        eventBlock = { message ->
            val state = request(message).state
            val eventMessage = EventMessage(
                state = state.getState(),
                stateData = state.getData(),
                message = message
            )
            event(eventMessage)
        },
        handle = {
            handle(request(this))
        }
    )
    addHandler(messageHandler)
}

internal class MessageHandler(
    private val eventBlock: suspend (MessageRequest) -> Boolean,
    private val handle: suspend MessageRequest.() -> MessageResponse
) : Handler {

    override suspend fun event(message: MessageRequest): Boolean = eventBlock(message)

    override suspend fun response(request: MessageRequest): MessageResponse {
        return handle(request)
    }
}