package com.github.alice.server

import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

interface WebServerResponseCallback {

    fun message(model: MessageRequest): MessageResponse?
}