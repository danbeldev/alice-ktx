package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.models.EventRequest
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.response.MessageResponse

fun Dispatcher.help(
    handle: suspend Request.() -> MessageResponse
) {
    addHandler(HelpHandler(handleBlock = handle))
}

internal class HelpHandler(
    private val handleBlock: suspend Request.() -> MessageResponse
) : Handler {

    override suspend fun event(request: EventRequest): Boolean {
        return request.message.request.command == "помощь" || request.message.request.command == "что ты умеешь"
    }

    override suspend fun handle(request: Request): MessageResponse = handleBlock(request)
}