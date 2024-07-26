package com.github.alice.server

import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

interface WebServerResponseCallback {

    suspend fun message(model: MessageRequest): MessageResponse?

    suspend fun responseFailure(model: MessageRequest, throwable: Throwable): MessageResponse?
}