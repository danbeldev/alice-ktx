package com.github.alice.ktx.models.request.session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param applicationId Идентификатор экземпляра приложения, в котором пользователь общается с Алисой, максимум 64 символа.
 * Даже если пользователь вошел в один и тот же аккаунт в приложение Яндекс для Android и iOS, Яндекс Диалоги присвоят отдельный application_id каждому из этих приложений.
 * Этот идентификатор уникален для пары «приложение — навык»: в разных навыках значение свойства application_id для одного и того же пользователя будет различаться.
 * */
@Serializable
data class Application(
    @SerialName("application_id")
    val applicationId: String
)