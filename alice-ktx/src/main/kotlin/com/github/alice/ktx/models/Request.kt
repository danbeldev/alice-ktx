package com.github.alice.ktx.models

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.state.FSMContext
import com.github.alice.ktx.state.MutableFSMContext
import com.github.alice.ktx.storage.apiStorage.ApiStorageDetails

internal suspend fun Dispatcher.request(message: MessageRequest): Request {
    val context = fsmContext(message)
    context.init()

    return Request(
        message = message,
        context = context,
        fsmStrategy = fsmStrategy,
        enableApiStorage = enableApiStorage
    )
}

data class Request(
    val message: MessageRequest,
    val context: MutableFSMContext,
    internal val fsmStrategy: FSMStrategy,
    internal val enableApiStorage: Boolean
)