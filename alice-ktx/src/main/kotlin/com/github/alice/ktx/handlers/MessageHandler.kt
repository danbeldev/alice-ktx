package com.github.alice.ktx.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.request
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Данные события, включающие состояние и данные состояния.
 *
 * @param state Текущее состояние, или `null`, если состояние не установлено.
 * @param stateData Данные состояния, хранящиеся в виде карты ключ-значение.
 * @param message Запрос сообщения.
 */
data class EventMessage(
    val state: String?,
    val stateData: Map<String, String>,
    val message: MessageRequest
)

/**
 * Расширение для `Dispatcher`, позволяющее установить обработчик сообщений.
 *
 * @param event Функция-условие, определяющая, должен ли обработчик сработать для данного события. Принимает объект `EventMessage` и возвращает `Boolean`.
 * @param handle Функция-обработчик, которая будет вызвана, если условие события возвращает `true`. Принимает объект `Request` и возвращает `MessageResponse`.
 */
fun Dispatcher.message(
    event: suspend EventMessage.() -> Boolean = { true },
    handle: suspend Request.() -> MessageResponse
) {
    val messageHandler = MessageHandler(
        eventBlock = { message ->
            val state = request(message).state
            val eventMessage = EventMessage(
                state = state.getState(),
                stateData = state.getData(),
                message = message
            )
            event(eventMessage)
        },
        handleBlock = {
            handle(request(this))
        }
    )
    addHandler(messageHandler)
}

/**
 * Внутренний класс `MessageHandler`, реализующий интерфейс `Handler`.
 *
 * @param eventBlock Функция, определяющая условие срабатывания обработчика. Принимает объект `MessageRequest` и возвращает `Boolean`.
 * @param handleBlock Функция-обработчик запроса. Принимает объект `MessageRequest` и возвращает `MessageResponse`.
 */
internal class MessageHandler(
    private val eventBlock: suspend (MessageRequest) -> Boolean,
    private val handleBlock: suspend MessageRequest.() -> MessageResponse
) : Handler {

    override suspend fun event(message: MessageRequest): Boolean = eventBlock(message)

    override suspend fun handle(request: MessageRequest): MessageResponse {
        return handleBlock(request)
    }
}