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
 * @param event Функция, которая вызывается при возникновении ошибки. Принимает `Throwable` и возвращает `Boolean`.
 *              Если функция возвращает `true`, вызывается обработчик `handle`.
 * @param handle Функция-обработчик, которая вызывается, если `event` возвращает `true`. Принимает `Throwable` и возвращает `MessageResponse?`.
 */
fun Dispatcher.responseFailure(
    event: suspend Request.(throwable: Throwable) -> Boolean,
    handle: suspend Request.(throwable: Throwable) -> MessageResponse?,
) {
    networkError { message, throwable ->
        request(message).apply {
            if(event(throwable)) return@networkError handle(throwable)
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
fun <E : Throwable> Dispatcher.responseFailure(thClass: KClass<E>, block: suspend Request.(throwable: E) -> MessageResponse?) {
    networkError { message, throwable ->
        if (thClass.isInstance(throwable)) {
            @Suppress("UNCHECKED_CAST")
            return@networkError block(request(message), throwable as E)
        }
        null
    }
}

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
