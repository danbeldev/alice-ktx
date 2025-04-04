package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Функция расширения для `Dispatcher`, которая добавляет обработчик для запроса помощи.
 * Этот обработчик срабатывает, когда пользователь запрашивает помощь.
 *
 * @param processRequest Логика обработки запроса, которая будет выполнена, когда сработает обработчик.
 */
@AliceDsl
fun Dispatcher.help(
    processRequest: suspend ProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(HelpHandler(processRequestBlock = processRequest))
}

internal class HelpHandler(
    private val processRequestBlock: suspend ProcessRequestEnvironment.() -> MessageResponse
) : Handler {

    /**
     * Определяет, должен ли обработчик сработать для данного запроса.
     * Обработчик срабатывает, если команда запроса соответствует "помощь".
     *
     * @param request Запрос, который проверяется.
     * @return `true`, если команда запроса соответствует "помощь";
     *         `false` в противном случае.
     */
    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean {
        return request.message.request.command == "помощь"
    }

    /**
     * Выполняет обработку запроса и возвращает соответствующий ответ.
     *
     * @param request Запрос, который будет обработан.
     * @return Ответ на запрос в виде `MessageResponse`.
     */
    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse = processRequestBlock(request)
}