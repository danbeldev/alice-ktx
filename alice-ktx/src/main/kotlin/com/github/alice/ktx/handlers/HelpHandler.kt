package com.github.alice.ktx.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

fun Dispatcher.help(
    handle: suspend Request.() -> MessageResponse
) {
    val handler = HelpHandler { handle(request(this)) }
    addHandler(handler)
}

internal class HelpHandler(
    private val handle: suspend MessageRequest.() -> MessageResponse
) : Handler {
    override suspend fun event(message: MessageRequest): Boolean {
        return message.request.command == "помощь" || message.request.command == "что ты умеешь"
    }

    override suspend fun response(request: MessageRequest): MessageResponse = handle(request)
}