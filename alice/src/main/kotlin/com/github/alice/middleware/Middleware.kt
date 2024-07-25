package com.github.alice.middleware

import com.github.alice.Dispatcher
import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse

fun Dispatcher.outerMiddleware(invoke: MessageRequest.() -> MessageResponse?) {
    addMiddleware(middleware(invoke), MiddlewareType.OUTER)
}

fun Dispatcher.innerMiddleware(invoke: MessageRequest.() -> MessageResponse?) {
    addMiddleware(middleware(invoke), MiddlewareType.INNER)
}

fun middleware(invoke: (MessageRequest) -> MessageResponse?): Middleware = object : Middleware {
    override fun invoke(model: MessageRequest): MessageResponse? = invoke(model)
}

interface Middleware {
    /**
     * Мидлварь должен всегда возвращать null чтобы передать событие следующему мидлварю/хэндлеру.
     * Если вы хотите завершить обработку события, вы должны вернуть [MessageResponse]
     *
     * */
    fun invoke(model: MessageRequest): MessageResponse?
}