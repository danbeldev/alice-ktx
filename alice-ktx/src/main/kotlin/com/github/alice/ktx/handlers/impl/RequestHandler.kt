package com.github.alice.ktx.handlers.impl

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.environments.ShouldRequestEnvironment
import com.github.alice.ktx.handlers.filters.Filter
import com.github.alice.ktx.models.response.MessageResponse

@AliceDsl
fun Dispatcher.request(
    filler: Filter,
    processRequest: suspend ProcessRequestEnvironment.() -> MessageResponse
) {
    request(
        shouldHandle = { filler.checkFor(this) },
        processRequest = processRequest
    )
}

/**
 * Регистрация обработчика для всех типов запросов.
 * Этот метод позволяет настроить обработчик для любых запросов,
 * независимо от их типа.
 *
 * @param shouldHandle Логика, определяющая, должен ли обработчик сработать для данного запроса.
 * @param processRequest Логика обработки запроса, которая выполняется, если запрос должен быть обработан.
 */
@AliceDsl
fun Dispatcher.request(
    shouldHandle: suspend ShouldRequestEnvironment.() -> Boolean = { true },
    processRequest: suspend ProcessRequestEnvironment.() -> MessageResponse
) {
    addHandler(
        RequestHandler(
            shouldHandleBlock = shouldHandle,
            processRequestBlock = processRequest
        )
    )
}

internal class RequestHandler(
    private val shouldHandleBlock: suspend ShouldRequestEnvironment.() -> Boolean,
    private val processRequestBlock: suspend ProcessRequestEnvironment.() -> MessageResponse
) : Handler {

    /**
     * Метод, который проверяет, должен ли обработчик сработать для данного запроса.
     *
     * @param request Запрос, который будет проверен.
     * @return true, если обработчик должен сработать, иначе false.
     */
    override suspend fun shouldHandle(request: ShouldRequestEnvironment): Boolean = shouldHandleBlock(request)

    /**
     * Метод, который выполняет обработку запроса и возвращает ответ.
     *
     * @param request Запрос, который нужно обработать.
     * @return Ответ на запрос в виде MessageResponse.
     */
    override suspend fun processRequest(request: ProcessRequestEnvironment): MessageResponse = processRequestBlock(request)
}