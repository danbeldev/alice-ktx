package com.github.alice.ktx.models

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.state.FSMContext

internal fun Dispatcher.request(message: MessageRequest): Request {
    return Request(
        message = message,
        state = fsmContext(message)
    )
}

data class Request(
    val message: MessageRequest,
    val state: FSMContext
)