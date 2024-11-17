package com.github.alice.ktx.models

import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.context.ReadOnlyFSMContext

internal fun Request.toEventRequest(): EventRequest {
    return EventRequest(
        message = message,
        context = context
    )
}

data class EventRequest(
    val message: MessageRequest,
    val context: ReadOnlyFSMContext
)