package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Функция расширения для `Dispatcher`, которая добавляет обработчик для запроса "что ты умеешь".
 * Этот обработчик срабатывает, когда пользователь запрашивает информацию о возможностях системы.
 *
 * @param processRequest Логика обработки запроса, которая будет выполнена, когда сработает обработчик.
 */
@AliceDsl
fun Dispatcher.whatCanYouDo(
    processRequest: suspend ProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(WhatCanYouDoHandler(processRequestBlock = processRequest))
}

internal class WhatCanYouDoHandler(
    private val processRequestBlock: suspend ProcessRequestEnvironment.() -> MessageResponse
) : Handler {

    /**
     * Определяет, должен ли обработчик сработать для данного запроса.
     * Обработчик срабатывает, если команда запроса соответствует "что ты умеешь".
     *
     * @param request Запрос, который проверяется.
     * @return `true`, если команда запроса соответствует "что ты умеешь";
     *         `false` в противном случае.
     */
    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean {
        return request.message.request.command == "что ты умеешь"
    }

    /**
     * Выполняет обработку запроса и возвращает соответствующий ответ.
     *
     * @param request Запрос, который будет обработан.
     * @return Ответ на запрос в виде `MessageResponse`.
     */
    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse = processRequestBlock(request)
}