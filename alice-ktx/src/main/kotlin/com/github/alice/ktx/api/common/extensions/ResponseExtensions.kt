package com.github.alice.ktx.api.common.extensions

import com.github.alice.ktx.api.common.Response
import com.github.alice.ktx.api.dialog.yandex.models.ErrorBody
import io.ktor.client.call.*
import io.ktor.client.statement.*

suspend inline fun <reified T> HttpResponse.response(): Response<T> {
    return if(status.value in 200..299)
        Response.Success(this.body())
    else
        Response.Failed(this.body<ErrorBody>().message)
}