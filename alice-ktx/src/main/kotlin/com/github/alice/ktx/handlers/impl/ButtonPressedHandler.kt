package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.models.EventRequest
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request.RequestContentType
import com.github.alice.ktx.models.response.MessageResponse
import com.github.alice.ktx.context.ReadOnlyFSMContext

data class EventButtonPressed(
    val context: ReadOnlyFSMContext,
    val payload: Map<String, String>
)

fun Dispatcher.buttonPressed(
    event: suspend EventButtonPressed.() -> Boolean = { true },
    handle: suspend Request.() -> MessageResponse
) {
    addHandler(
        ButtonPressedHandler(
            eventBlock = event,
            handleBlock = handle
        )
    )
}

internal class ButtonPressedHandler(
    private val eventBlock: suspend EventButtonPressed.() -> Boolean,
    private val handleBlock: suspend Request.() -> MessageResponse
) : Handler {
    override suspend fun event(request: EventRequest): Boolean {
        return request.message.request.type == RequestContentType.ButtonPressed &&
                eventBlock(EventButtonPressed(context = request.context, payload = request.message.request.payload!!))
    }

    override suspend fun handle(request: Request): MessageResponse {
        return handleBlock(request)
    }
}