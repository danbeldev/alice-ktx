package com.github.alice.ktx.models.request.content.error

import kotlinx.serialization.Serializable

@Serializable
data class RequestError(
    val message: String = "",
    val type: RequestErrorType
)

/**
 * @property MEDIA_ERROR_UNKNOWN неизвестная ошибка.
 * @property MEDIA_ERROR_SERVICE_UNAVAILABLE указанный URL трека недоступен или некорректен. Детальная информация об ошибке — в поле message
 * */
enum class RequestErrorType {
    MEDIA_ERROR_UNKNOWN,
    MEDIA_ERROR_SERVICE_UNAVAILABLE
}
