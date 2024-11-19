package com.github.alice.ktx.webhook

/**
 * Интерфейс `WebhookServer` определяет контракт для веб-сервера, который может быть запущен и обрабатывать запросы.
 */
interface WebhookServer {

    /**
     * Запускает веб-сервер и начинает обработку запросов.
     *
     * @param listener Обработчик, который будет вызван при получении запроса.
     */
    fun run(listener: WebhookServerListener)

}