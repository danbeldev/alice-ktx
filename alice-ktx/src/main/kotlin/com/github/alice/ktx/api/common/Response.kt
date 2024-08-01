package com.github.alice.ktx.api.common

/**
 * Запечатанный интерфейс, представляющий ответ, который может быть либо успешным, либо неудачным.
 *
 * @param T Тип данных, возвращаемых в успешном ответе.
 */
sealed interface Response<T> {

    /**
     * Представляет неудачный ответ.
     *
     * @param T Тип данных, которые были бы возвращены в успешном ответе.
     * @param message Сообщение об ошибке, объясняющее причину неудачи.
     */
    data class Failed<T>(val message: String): Response<T>

    /**
     * Представляет успешный ответ.
     *
     * @param T Тип данных, возвращаемых в ответе.
     * @param data Данные, возвращаемые в успешном ответе.
     */
    data class Success<T>(val data: T): Response<T>
}
