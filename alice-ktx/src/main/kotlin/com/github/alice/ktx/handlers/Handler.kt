package com.github.alice.ktx.handlers

import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

interface Handler {

    suspend fun event(message: MessageRequest): Boolean

    suspend fun response(request: MessageRequest): MessageResponse
}