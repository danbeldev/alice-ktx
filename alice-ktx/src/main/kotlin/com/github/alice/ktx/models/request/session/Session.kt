package com.github.alice.ktx.models.request.session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param messageId Идентификатор сообщения в рамках сессии, максимум 8 символов.
 * @param sessionId Уникальный идентификатор сессии, максимум 64 символа.
 * @param skillId Идентификатор вызываемого навыка, присвоенный при создании.
 * @param userId Идентификатор экземпляра приложения, в котором пользователь общается с Алисой.
 * @param user Атрибуты пользователя Яндекса, который взаимодействует с навыком. Если пользователь не авторизован в приложении, свойства [user] в запросе не будет.
 * @param application Данные о приложении, с помощью которого пользователь взаимодействует с навыком.
 * @param new Признак новой сессии
 * */
@Serializable
data class Session(
    @SerialName("message_id")
    val messageId: Int,
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("skill_id")
    val skillId: String,
    @Deprecated("Свойство не поддерживается — вместо него следует использовать новое, полностью аналогичное свойство session.application.application_id")
    @SerialName("user_id")
    val userId: String,
    val user: User? = null,
    val application: Application,
    val new: Boolean
)
