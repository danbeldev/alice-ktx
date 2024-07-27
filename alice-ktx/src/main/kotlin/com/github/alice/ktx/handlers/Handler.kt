package com.github.alice.ktx.handlers

import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Интерфейс `Handler` предназначен для обработки событий, связанных с запросами сообщений.
 * Реализация этого интерфейса позволяет задать логику для определения, когда обработчик должен сработать,
 * и для обработки самого запроса.
 */
interface Handler {

    /**
     * Определяет, сработает ли обработчик для данного сообщения.
     *
     * @param message Сообщение, которое проверяется на соответствие условиям обработчика.
     * @return `true`, если обработчик должен сработать для данного сообщения; `false` в противном случае.
     */
    suspend fun event(message: MessageRequest): Boolean

    /**
     * Выполняет обработку запроса сообщения и возвращает ответ.
     *
     * @param request Запрос сообщения, который будет обработан.
     * @return Ответ на запрос в виде `MessageResponse`.
     */
    suspend fun handle(request: MessageRequest): MessageResponse
}