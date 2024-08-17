package com.github.alice.ktx.handlers.error

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse
import kotlin.reflect.KClass

/**
 * Расширение для `Dispatcher`, добавляющее обработку сетевых ошибок с помощью заданных функций.
 *
 * @param event Функция, которая вызывается при возникновении ошибки. Принимает `Exception` и возвращает `Boolean`.
 *              Если функция возвращает `true`, вызывается обработчик `handle`.
 * @param handle Функция-обработчик, которая вызывается, если `event` возвращает `true`. Принимает `Exception` и возвращает `MessageResponse?`.
 */
fun Dispatcher.responseFailure(
    event: suspend Request.(ex: Exception) -> Boolean,
    handle: suspend Request.(ex: Exception) -> MessageResponse?,
) {
    networkError { message, ex ->
        request(message).apply {
            if(event(ex)) return@networkError handle(ex)
        }
        null
    }
}

/**
 * Расширение для `Dispatcher`, добавляющее обработку сетевых ошибок с помощью указанного класса исключения.
 *
 * @param thClass Класс исключения, которое должно быть обработано.
 * @param block Функция-обработчик, которая вызывается, если исключение принадлежит классу `thClass`. Принимает исключение типа `E` и возвращает `MessageResponse?`.
 */
fun <E : Exception> Dispatcher.responseFailure(thClass: KClass<E>, block: suspend Request.(ex: E) -> MessageResponse?) {
    networkError { message, ex ->
        if (thClass.isInstance(ex)) {
            @Suppress("UNCHECKED_CAST")
            return@networkError block(request(message), ex as E)
        }
        null
    }
}

/**
 * Расширение для `Dispatcher`, добавляющее обработку сетевых ошибок с помощью блоков функций.
 *
 * @param block Функция-обработчик, которая вызывается при возникновении ошибки. Принимает `Exception` и возвращает `MessageResponse?`.
 */
fun Dispatcher.responseFailure(block: suspend Request.(ex: Exception) -> MessageResponse?) {
    networkError { message, ex  -> block(request(message), ex) }
}

/**
 * Расширение для `Dispatcher`, позволяющее установить обработчик сетевых ошибок.
 *
 * @param responseFailure Функция-обработчик, которая вызывается при возникновении ошибки. Принимает модель запроса и исключение, возвращает `MessageResponse?`.
 * По умолчанию, возвращает `null`, что позволяет передать событие следующему обработчику.
 */
fun Dispatcher.networkError(
    responseFailure: suspend (model: MessageRequest, ex: Exception) -> MessageResponse? = { _, _ -> null }
) {
    val networkErrorHandler = object : NetworkErrorHandler {
        override suspend fun responseFailure(model: MessageRequest, ex: Exception): MessageResponse? {
            return responseFailure(model, ex)
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
     * @param ex Исключение, которое возникло.
     *
     * [responseFailure] должен всегда возвращать null чтобы передать событие следующему хэндлеру.
     * Если вы хотите завершить обработку события, вы должны вернуть [MessageResponse]
     *
     * */
    suspend fun responseFailure(model: MessageRequest, ex: Exception): MessageResponse? = null
}
