package com.github.alice.handlers

import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

interface Handler {

    suspend fun event(message: MessageRequest): Boolean

    suspend fun response(request: MessageRequest): MessageResponse
}