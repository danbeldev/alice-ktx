package com.github.alice.handlers.error

import com.github.alice.Dispatcher
import com.github.alice.models.Request
import com.github.alice.models.request
import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

fun Dispatcher.responseFailure(block: suspend Request.(throwable: Throwable) -> MessageResponse?) {
    networkError { message, throwable  -> block(request(message), throwable) }
}

private fun Dispatcher.networkError(
    block: suspend (model: MessageRequest, throwable: Throwable) -> MessageResponse? = { _, _ -> null }
) {
    val networkErrorHandler = object : NetworkErrorHandler {
        override suspend fun responseFailure(model: MessageRequest, throwable: Throwable): MessageResponse? {
            return block(model, throwable)
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
