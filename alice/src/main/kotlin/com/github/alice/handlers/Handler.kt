package com.github.alice.handlers

import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

interface Handler {

    fun event(message: MessageRequest): Boolean

    fun response(request: MessageRequest): MessageResponse
}