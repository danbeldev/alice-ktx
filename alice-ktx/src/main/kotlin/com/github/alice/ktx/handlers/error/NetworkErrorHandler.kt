package com.github.alice.ktx.handlers.error

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.response.MessageResponse
import kotlin.reflect.KClass

/**
 * Расширение для `Dispatcher`, добавляющее обработку сетевых ошибок с помощью заданных функций.
 *
 * @param shouldHandle Функция, которая вызывается при возникновении ошибки. Принимает `Exception` и возвращает `Boolean`.
 *              Если функция возвращает `true`, вызывается обработчик `handle`.
 * @param processRequest Функция-обработчик, которая вызывается, если `event` возвращает `true`. Принимает `Exception` и возвращает `MessageResponse?`.
 */
@AliceDsl
fun Dispatcher.responseFailure(
    shouldHandle: suspend ShouldRequestEnvironment.(exception: Exception) -> Boolean,
    processRequest: suspend ProcessRequestEnvironment.(exception: Exception) -> MessageResponse?,
) {
    networkError { request, ex ->
        if(request.shouldHandle(ex)) return@networkError request.processRequest(ex)
        null
    }
}

/**
 * Расширение для `Dispatcher`, добавляющее обработку сетевых ошибок с помощью указанного класса исключения.
 *
 * @param thClass Класс исключения, которое должно быть обработано.
 * @param block Функция-обработчик, которая вызывается, если исключение принадлежит классу `thClass`. Принимает исключение типа `E` и возвращает `MessageResponse?`.
 */
@AliceDsl
fun <E : Exception> Dispatcher.responseFailure(
    thClass: KClass<E>,
    block: suspend ProcessRequestEnvironment.(exception: E) -> MessageResponse?
) {
    networkError { request, ex ->
        if (thClass.isInstance(ex)) {
            @Suppress("UNCHECKED_CAST")
            return@networkError block(request, ex as E)
        }
        null
    }
}

/**
 * Расширение для `Dispatcher`, добавляющее обработку сетевых ошибок с помощью блоков функций.
 *
 * @param block Функция-обработчик, которая вызывается при возникновении ошибки. Принимает `Exception` и возвращает `MessageResponse?`.
 */
@AliceDsl
fun Dispatcher.responseFailure(block: suspend ProcessRequestEnvironment.(exception: Exception) -> MessageResponse?) {
    networkError { request, ex  -> block(request, ex) }
}

/**
 * Расширение для `Dispatcher`, позволяющее установить обработчик сетевых ошибок.
 *
 * @param responseFailure Функция-обработчик, которая вызывается при возникновении ошибки. Принимает модель запроса и исключение, возвращает `MessageResponse?`.
 * По умолчанию, возвращает `null`, что позволяет передать событие следующему обработчику.
 */
@AliceDsl
fun Dispatcher.networkError(
    responseFailure: suspend (model: ProcessRequestEnvironment, exception: Exception) -> MessageResponse? = { _, _ -> null }
) {
    val networkErrorHandler = object : NetworkErrorHandler {
        override suspend fun responseFailure(request: ProcessRequestEnvironment, exception: Exception): MessageResponse? {
            return responseFailure(request, exception)
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
     * @param request Запроса, при обработке которого произошла ошибка.
     * @param exception Исключение, которое возникло.
     *
     * [responseFailure] должен всегда возвращать null чтобы передать событие следующему хэндлеру.
     * Если вы хотите завершить обработку события, вы должны вернуть [MessageResponse]
     *
     * */
    suspend fun responseFailure(request: ProcessRequestEnvironment, exception: Exception): MessageResponse? = null
}
