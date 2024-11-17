package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.models.EventRequest
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Расширение для `Dispatcher`, позволяющее установить обработчик сообщений.
 *
 * @param event Функция-условие, определяющая, должен ли обработчик сработать для данного события. Принимает объект `EventMessage` и возвращает `Boolean`.
 * @param handle Функция-обработчик, которая будет вызвана, если условие события возвращает `true`. Принимает объект `Request` и возвращает `MessageResponse`.
 */
fun Dispatcher.message(
    event: suspend EventRequest.() -> Boolean = { true },
    handle: suspend Request.() -> MessageResponse
) {
    addHandler(
        MessageHandler(
            eventBlock = event,
            handleBlock = handle
        )
    )
}

/**
 * Внутренний класс `MessageHandler`, реализующий интерфейс `Handler`.
 *
 * @param eventBlock Функция, определяющая условие срабатывания обработчика. Принимает объект `MessageRequest` и возвращает `Boolean`.
 * @param handleBlock Функция-обработчик запроса. Принимает объект `MessageRequest` и возвращает `MessageResponse`.
 */
internal class MessageHandler(
    private val eventBlock: suspend EventRequest.() -> Boolean,
    private val handleBlock: suspend Request.() -> MessageResponse
) : Handler {

    override suspend fun event(request: EventRequest): Boolean = eventBlock(request)

    override suspend fun handle(request: Request): MessageResponse {
        return handleBlock(request)
    }
}