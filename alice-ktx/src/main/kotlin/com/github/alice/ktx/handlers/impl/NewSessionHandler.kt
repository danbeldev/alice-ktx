package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.models.EventRequest
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.response.MessageResponse

fun Dispatcher.newSession(
    event: suspend EventRequest.() -> Boolean = { true },
    handle: suspend Request.() -> MessageResponse
) {
    addHandler(
        NewSessionHandler(
            eventBlock = event,
            handleBlock = handle
        )
    )
}

internal class NewSessionHandler(
    private val eventBlock: suspend EventRequest.() -> Boolean,
    private val handleBlock: suspend Request.() -> MessageResponse
) : Handler {

    override suspend fun event(request: EventRequest): Boolean {
        return request.message.session.new && eventBlock(request)
    }

    override suspend fun handle(request: Request): MessageResponse {
        return handleBlock(request)
    }
}