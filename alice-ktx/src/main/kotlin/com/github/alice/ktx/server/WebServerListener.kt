package com.github.alice.ktx.server

import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse
import java.lang.Exception

/**
 * Интерфейс `WebServerListener` представляет собой обработчик, который будет вызываться при получении запроса веб-сервером.
 */
interface WebServerListener {

    /**
     * Вызывается при получении запроса от Яндекс Диалогов
     *
     * @param model Модель запроса, полученного от Яндекс Диалогов.
     * */
    suspend fun handleRequest(model: MessageRequest): MessageResponse?

    /**
     * Вызывается при возникновении ошибки
     * @param model Модель запроса, при обработке которого произошла ошибка.
     * @param exception Исключение, которое возникло.
     * */
    suspend fun handleError(model: MessageRequest, exception: Exception): MessageResponse?
}