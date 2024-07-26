package com.github.alice.ktx.middleware

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

fun Dispatcher.outerMiddleware(invoke: suspend Request.() -> MessageResponse?) {
    val middleware = middleware { message -> invoke(request(message)) }
    addMiddleware(middleware, MiddlewareType.OUTER)
}

fun Dispatcher.innerMiddleware(invoke: suspend Request.() -> MessageResponse?) {
    val middleware = middleware { message -> invoke(request(message)) }
    addMiddleware(middleware, MiddlewareType.INNER)
}

fun middleware(invoke: suspend (MessageRequest) -> MessageResponse?): Middleware = object : Middleware {
    override suspend fun invoke(model: MessageRequest): MessageResponse? = invoke(model)
}

interface Middleware {
    /**
     * Мидлварь должен всегда возвращать null чтобы передать событие следующему мидлварю/хэндлеру.
     * Если вы хотите завершить обработку события, вы должны вернуть [MessageResponse]
     *
     * */
    suspend fun invoke(model: MessageRequest): MessageResponse?
}