package com.github.alice.ktx.server

import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

interface WebServerResponseCallback {

    suspend fun message(model: MessageRequest): MessageResponse?

    suspend fun responseFailure(model: MessageRequest, throwable: Throwable): MessageResponse?
}