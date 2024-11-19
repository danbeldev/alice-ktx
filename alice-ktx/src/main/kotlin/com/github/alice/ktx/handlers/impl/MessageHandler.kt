package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.handlers.filters.Filter
import com.github.alice.ktx.models.request.content.RequestContentType
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Окружение для проверки, следует ли обрабатывать сообщение.
 */
@AliceDsl
data class MessageShouldHandleEnvironment(
    private val request: ShouldRequestEnvironment
) : ShouldRequestEnvironment by request {

    val command = request.message.request.command ?: ""
    val messageText = request.message.request.originalUtterance ?: ""
    val nlu = request.message.request.nlu
}

/**
 * Окружение для обработки запроса сообщения.
 */
@AliceDsl
data class MessageProcessRequestEnvironment(
    private val request: ProcessRequestEnvironment
) : ProcessRequestEnvironment by request {

    val command = request.message.request.command ?: ""
    val messageText = request.message.request.originalUtterance ?: ""
    val nlu = request.message.request.nlu
}

@AliceDsl
fun Dispatcher.message(
    filter: Filter,
    processRequest: suspend MessageProcessRequestEnvironment.() -> MessageResponse
) {
    message(
        shouldHandle = { filter.checkFor(this) },
        processRequest = processRequest
    )
}

/**
 * Функция расширения для `Dispatcher`, которая добавляет обработчик для сообщений.
 * Этот обработчик срабатывает, если:
 *
 * 1. Пользователь произносит фразу.
 * 2. Пользователь нажимает кнопку в бабле из предыдущего ответа навыка (свойство hide со значением false).
 * 3. Пользователь нажимает отдельную кнопку в предыдущем ответе навыка (свойство hide со значением true) с отсутствующим значением в поле payload.
 *
 * @param shouldHandle Логика, которая определяет, следует ли обрабатывать это сообщение.
 * @param processRequest Логика обработки сообщения, которая будет выполнена при удовлетворении условиям.
 */
@AliceDsl
fun Dispatcher.message(
    shouldHandle: suspend MessageShouldHandleEnvironment.() -> Boolean = { true },
    processRequest: suspend MessageProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(
        MessageHandler(
            shouldHandleBlock = shouldHandle,
            processRequestBlock = processRequest
        )
    )
}

internal class MessageHandler(
    private val shouldHandleBlock: suspend MessageShouldHandleEnvironment.() -> Boolean,
    private val processRequestBlock: suspend MessageProcessRequestEnvironment.() -> MessageResponse
) : Handler {

    /**
     * Определяет, следует ли обрабатывать данный запрос.
     *
     * @param request Запрос, который проверяется.
     * @return `true`, если обработчик должен сработать, в противном случае `false`.
     */
    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean {
        return request.message.request.type == RequestContentType.SimpleUtterance && shouldHandleBlock(MessageShouldHandleEnvironment(request))
    }

    /**
     * Выполняет обработку запроса и возвращает соответствующий ответ.
     *
     * @param request Запрос, который будет обработан.
     * @return Ответ на запрос в виде `MessageResponse`.
     */
    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse =
        processRequestBlock(MessageProcessRequestEnvironment(request))
}