package com.github.alice.ktx.server

/**
 * Интерфейс `WebServer` определяет контракт для веб-сервера, который может быть запущен и обрабатывать запросы.
 */
interface WebServer {

    /**
     * Запускает веб-сервер и начинает обработку запросов.
     *
     * @param listener Обработчик, который будет вызван при получении запроса.
     */
    fun run(listener: WebServerResponseListener)

}