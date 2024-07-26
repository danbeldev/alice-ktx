package com.github.alice.models

import com.github.alice.Dispatcher
import com.github.alice.models.request.MessageRequest
import com.github.alice.state.FSMContext
import com.github.alice.state.FSMContextImpl

internal fun Dispatcher.request(message: MessageRequest): Request {
    return Request(
        message = message,
        state = FSMContextImpl(message, fsmStrategy)
    )
}

data class Request(
    val message: MessageRequest,
    val state: FSMContext
)