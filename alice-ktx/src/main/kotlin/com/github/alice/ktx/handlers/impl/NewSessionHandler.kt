package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Функция расширения для `Dispatcher`, которая добавляет обработчик для нового сеанса.
 * Этот обработчик срабатывает, когда создается новый сеанс, и выполняет соответствующую логику.
 *
 * @param shouldHandle Логика, которая определяет, должен ли обработчик сработать для данного запроса.
 * @param processRequest Логика обработки запроса, которая будет выполнена, когда сработает обработчик.
 */
@AliceDsl
fun Dispatcher.newSession(
    shouldHandle: suspend ShouldRequestEnvironment.() -> Boolean = { true },
    processRequest: suspend ProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(
        NewSessionHandler(
            shouldHandleBlock = shouldHandle,
            processRequestBlock = processRequest
        )
    )
}

internal class NewSessionHandler(
    private val shouldHandleBlock: suspend ShouldRequestEnvironment.() -> Boolean,
    private val processRequestBlock: suspend ProcessRequestEnvironment.() -> MessageResponse
) : Handler {

    /**
     * Определяет, сработает ли обработчик для данного запроса.
     * Обработчик срабатывает, если запрос связан с новым сеансом.
     *
     * @param request Запрос, который проверяется.
     * @return `true`, если запрос относится к новому сеансу и условие `shouldHandle` выполняется.
     */
    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean {
        return request.message.session.new && shouldHandleBlock(request)
    }

    /**
     * Выполняет обработку запроса и возвращает соответствующий ответ.
     *
     * @param request Запрос, который будет обработан.
     * @return Ответ на запрос в виде `MessageResponse`.
     */
    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse {
        return processRequestBlock(request)
    }
}