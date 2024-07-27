package com.github.alice.ktx.server

import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Интерфейс `WebServerResponseListener` представляет собой обработчик, который будет вызываться при получении запроса веб-сервером.
 */
interface WebServerResponseListener {

    /**
     * Вызывается при получении запроса от Яндекс Диалогов
     *
     * @param model Модель запроса, полученного от Яндекс Диалогов.
     * */
    suspend fun messageHandle(model: MessageRequest): MessageResponse?

    /**
     * Вызывается при возникновении ошибки
     * @param model Модель запроса, при обработке которого произошла ошибка.
     * @param throwable Исключение, которое возникло.
     * */
    suspend fun responseFailure(model: MessageRequest, throwable: Throwable): MessageResponse?
}