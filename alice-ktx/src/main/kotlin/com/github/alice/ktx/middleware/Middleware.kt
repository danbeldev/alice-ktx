package com.github.alice.ktx.middleware

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Расширение для `Dispatcher`, добавляющее внешний мидлварь (outer middleware), который будет вызван при каждом входящем событии.
 *
 * @param process Функция, которая будет вызвана для обработки запроса сообщения. Принимает объект `Request` и возвращает `MessageResponse?`.
 * Мидлварь должен возвращать `null`, чтобы передать событие следующему мидлварю/хэндлеру.
 * Если требуется завершить обработку события, необходимо вернуть `MessageResponse`.
 */
@AliceDsl
fun Dispatcher.outerMiddleware(process: suspend ProcessRequestEnvironment.() -> MessageResponse?) {
    val middleware = middleware { request -> process(request) }
    addMiddleware(middleware, MiddlewareType.OUTER)
}

/**
 * Расширение для `Dispatcher`, добавляющее внутренний мидлварь (inner middleware), который будет вызван только при прохождении фильтров.
 *
 * @param process Функция, которая будет вызвана для обработки запроса сообщения. Принимает объект `Request` и возвращает `MessageResponse?`.
 * Мидлварь должен возвращать `null`, чтобы передать событие следующему мидлварю/хэндлеру.
 * Если требуется завершить обработку события, необходимо вернуть `MessageResponse`.
 */
@AliceDsl
fun Dispatcher.innerMiddleware(process: suspend ProcessRequestEnvironment.() -> MessageResponse?) {
    val middleware = middleware { request -> process(request) }
    addMiddleware(middleware, MiddlewareType.INNER)
}

/**
 * Расширение для `Dispatcher`, добавляющее постобработчик (`PostMiddleware`), который вызывается после обработки запроса хэндлером.
 *
 * @param block Лямбда-функция, принимающая [ProcessRequestEnvironment] (исходный запрос) и [MessageResponse] (ответ от хэндлера),
 * и возвращающая (возможно модифицированный) [MessageResponse].
 */
@AliceDsl
fun Dispatcher.afterHandle(block: suspend (ProcessRequestEnvironment, MessageResponse) -> MessageResponse) {
    addPostMiddlewares(
        object : PostMiddleware {
            override suspend fun afterHandle(
                request: ProcessRequestEnvironment,
                response: MessageResponse
            ): MessageResponse = block(request, response)
        }
    )
}

/**
 * Создает мидлварь с заданной функцией обработки.
 *
 * @param process Функция, которая будет вызвана для обработки запроса сообщения. Принимает объект `MessageRequest` и возвращает `MessageResponse?`.
 * Мидлварь должен возвращать `null`, чтобы передать событие следующему мидлварю/хэндлеру.
 * Если требуется завершить обработку события, необходимо вернуть `MessageResponse`.
 * @return Реализованный объект `Middleware`.
 */
fun middleware(process: suspend (ProcessRequestEnvironment) -> MessageResponse?): Middleware = object : Middleware {
    override suspend fun process(request: ProcessRequestEnvironment): MessageResponse? = process(request)
}

/**
 * Интерфейс `Middleware` представляет собой мидлварь для обработки запросов сообщений.
 */
interface Middleware {
    /**
     * Мидлварь должен всегда возвращать null чтобы передать событие следующему мидлварю/хэндлеру.
     * Если вы хотите завершить обработку события, вы должны вернуть [MessageResponse]
     *
     * */
    suspend fun process(request: ProcessRequestEnvironment): MessageResponse?
}

/**
 * Интерфейс `PostMiddleware` предназначен для выполнения логики после обработки запроса обработчика(`Handler`).
 */
interface PostMiddleware {

    /**
     * Вызывается после выполнения `Handler.processRequest(...)`.
     *
     * @param request Объект запроса, переданный в `processRequest`.
     * @param response Ответ, полученный от обработчика (`Handler`).
     *
     * @return Модифицированный или оригинальный [MessageResponse].
     */
    suspend fun afterHandle(
        request: ProcessRequestEnvironment,
        response: MessageResponse
    ): MessageResponse
}
