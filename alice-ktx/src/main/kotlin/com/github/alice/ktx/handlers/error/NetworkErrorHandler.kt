package com.github.alice.ktx.handlers.error

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Расширение для `Dispatcher`, добавляющее обработку сетевых ошибок с помощью блоков функций.
 *
 * @param block Функция-обработчик, которая вызывается при возникновении ошибки. Принимает `Throwable` и возвращает `MessageResponse?`.
 */
fun Dispatcher.responseFailure(block: suspend Request.(throwable: Throwable) -> MessageResponse?) {
    networkError { message, throwable  -> block(request(message), throwable) }
}

/**
 * Расширение для `Dispatcher`, позволяющее установить обработчик сетевых ошибок.
 *
 * @param responseFailure Функция-обработчик, которая вызывается при возникновении ошибки. Принимает модель запроса и исключение, возвращает `MessageResponse?`.
 * По умолчанию, возвращает `null`, что позволяет передать событие следующему обработчику.
 */
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

/**
 * Расширение для `Dispatcher`, добавляющее обработчик сетевых ошибок.
 *
 * @param handler Обработчик сетевых ошибок, реализующий интерфейс `NetworkErrorHandler`.
 */
fun Dispatcher.networkError(handler: NetworkErrorHandler) {
    addNetworkErrorHandler(handler)
}

/**
 * Интерфейс `NetworkErrorHandler` предназначен для обработки сетевых ошибок.
 */
interface NetworkErrorHandler {
    /**
     * Вызывается при возникновении ошибки
     * @param model Модель запроса, при обработке которого произошла ошибка.
     * @param throwable Исключение, которое возникло.
     *
     * [responseFailure] должен всегда возвращать null чтобы передать событие следующему хэндлеру.
     * Если вы хотите завершить обработку события, вы должны вернуть [MessageResponse]
     *
     * */
    suspend fun responseFailure(model: MessageRequest, throwable: Throwable): MessageResponse? = null
}
