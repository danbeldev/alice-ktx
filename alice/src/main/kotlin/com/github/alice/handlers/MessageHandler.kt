package com.github.alice.handlers

import com.github.alice.Dispatcher
import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

data class MessageHandlerEnvironment(
    val message: MessageRequest
)

fun Dispatcher.message(event: (MessageRequest) -> Boolean, handle: MessageHandlerEnvironment.() -> MessageResponse) {
    addHandler(MessageHandler(event, handle))
}

class MessageHandler(
    private val eventBlock: (MessageRequest) -> Boolean,
    private val handle: MessageHandlerEnvironment.() -> MessageResponse
): Handler {

    override fun event(message: MessageRequest): Boolean = eventBlock(message)

    override fun response(request: MessageRequest): MessageResponse {
        val environment = MessageHandlerEnvironment(request)
        return handle(environment)
    }
}