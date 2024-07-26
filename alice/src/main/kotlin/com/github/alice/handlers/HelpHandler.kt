package com.github.alice.handlers

import com.github.alice.Dispatcher
import com.github.alice.models.Request
import com.github.alice.models.request
import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

fun Dispatcher.help(
    handle: Request.() -> MessageResponse
) {
    val handler = HelpHandler { handle(request(this)) }
    addHandler(handler)
}

internal class HelpHandler(
    private val handle: MessageRequest.() -> MessageResponse
) : Handler {
    override fun event(message: MessageRequest): Boolean {
        return message.request.command == "помощь" || message.request.command == "что ты умеешь"
    }

    override fun response(request: MessageRequest): MessageResponse = handle(request)
}