package com.github.alice.handlers

import com.github.alice.Dispatcher
import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

fun Dispatcher.message(
    event: MessageRequest.() -> Boolean = { true },
    handle: MessageRequest.() -> MessageResponse
) {
    addHandler(MessageHandler(event, handle))
}

internal class MessageHandler(
    private val eventBlock: (MessageRequest) -> Boolean,
    private val handle: MessageRequest.() -> MessageResponse
): Handler {

    override fun event(message: MessageRequest): Boolean = eventBlock(message)

    override fun response(request: MessageRequest): MessageResponse {
        return handle(request)
    }
}