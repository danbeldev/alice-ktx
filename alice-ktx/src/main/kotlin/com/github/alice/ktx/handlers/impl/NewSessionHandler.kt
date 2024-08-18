package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

fun Dispatcher.newSession(
    handle: suspend Request.() -> MessageResponse
) {
    val handler = NewSessionHandler { handle(request(this)) }
    addHandler(handler)
}

internal class NewSessionHandler(
    private val handleBlock: suspend MessageRequest.() -> MessageResponse
) : Handler {

    override suspend fun event(message: MessageRequest): Boolean = message.session.new

    override suspend fun handle(request: MessageRequest): MessageResponse = handleBlock(request)
}