package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.request.RequestContentType
import com.github.alice.ktx.models.response.MessageResponse
import com.github.alice.ktx.state.ReadOnlyFSMContext

data class EventButtonPressed(
    val context: ReadOnlyFSMContext,
    val payload: Map<String, String>
)

fun Dispatcher.buttonPressed(
    event: suspend EventButtonPressed.() -> Boolean = { true },
    handle: suspend Request.() -> MessageResponse
) {
    val handler = ButtonPressedHandler(
        eventBlock = { message ->
            event(
                EventButtonPressed(
                    payload = message.request.payload!!,
                    context = request(message).context
                )
            )
        },
        handleBlock = {
            handle(request(this))
        }
    )
    addHandler(handler)
}

internal class ButtonPressedHandler(
    private val eventBlock: suspend (MessageRequest) -> Boolean,
    private val handleBlock: suspend MessageRequest.() -> MessageResponse
) : Handler {
    override suspend fun event(message: MessageRequest): Boolean {
        return message.request.type == RequestContentType.ButtonPressed && eventBlock(message)
    }

    override suspend fun handle(request: MessageRequest): MessageResponse = handleBlock(request)
}