package com.github.alice.ktx.api.common.extensions

import com.github.alice.ktx.api.common.Response
import com.github.alice.ktx.api.dialog.yandex.models.error.ErrorBody
import io.ktor.client.call.*
import io.ktor.client.statement.*

/**
 * Расширение для класса HttpResponse, которое преобразует HTTP-ответ в экземпляр класса Response.
 * Функция обрабатывает успешные и неудачные HTTP-ответы, возвращая соответствующий результат.
 *
 * @param T Тип данных, ожидаемый в успешном ответе.
 * @return Экземпляр Response, представляющий либо успешный результат с данными типа T, либо неудачный результат с сообщением об ошибке.
 */
suspend inline fun <reified T> HttpResponse.response(): Response<T> {
    return if(status.value in 200..299)
        Response.Success(this.body())
    else
        Response.Failed(this.body<ErrorBody>().message)
}