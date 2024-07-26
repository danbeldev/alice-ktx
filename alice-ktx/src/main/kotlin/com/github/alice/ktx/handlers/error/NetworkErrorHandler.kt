package com.github.alice.ktx.handlers.error

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

fun Dispatcher.responseFailure(block: suspend Request.(throwable: Throwable) -> MessageResponse?) {
    networkError { message, throwable  -> block(request(message), throwable) }
}

fun Dispatcher.networkError(
    responseFailure: suspend (model: MessageRequest, throwable: Throwable) -> MessageResponse? = { _, _ -> null }
) {
    val networkErrorHandler = object : NetworkErrorHandler {
        override suspend fun responseFailure(model: MessageRequest, throwable: Throwable): MessageResponse? {
            return responseFailure(model, throwable)
        }
    }
    networkError(networkErrorHandler)
}

fun Dispatcher.networkError(handler: NetworkErrorHandler) {
    addNetworkErrorHandler(handler)
}

interface NetworkErrorHandler {
    /**
     * [responseFailure] должен всегда возвращать null чтобы передать событие следующему хэндлеру.
     * Если вы хотите завершить обработку события, вы должны вернуть [MessageResponse]
     *
     * */
    suspend fun responseFailure(model: MessageRequest, throwable: Throwable): MessageResponse? = null
}
