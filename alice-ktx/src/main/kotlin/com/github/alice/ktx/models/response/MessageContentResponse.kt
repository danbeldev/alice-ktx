package com.github.alice.ktx.models.response

import com.github.alice.ktx.models.response.button.Button
import com.github.alice.ktx.models.response.card.Card
import com.github.alice.ktx.models.response.show.ShowItemMeta
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param text Текст, который следует показать и озвучить пользователю.
 * Максимум 1024 символа. Может быть пустым (text: ""), если заполнено свойство tts.
 * Текст также используется, если у Алисы не получилось отобразить включенную в ответ карточку (свойство response.card).
 * На устройствах, которые поддерживают только голосовое общение с навыком, это будет происходить каждый раз, когда навык присылает карточку в ответе.
 * В тексте ответа можно указать переводы строк последовательностью «\n», например: "Отдых напрасен. Дорога крута.\nВечер прекрасен. Стучу в ворота."
 * @param tts Ответ в формате TTS (text-to-speech).
 * @param endSession Признак конца разговора.
 * @param shouldListen Чтобы начать проигрывание трека без ожидания запроса пользователя, перед описанием директивы укажите [shouldListen] = false.
 * @param buttons Кнопки, которые следует показать пользователю.
 * @param card Описание карточки — сообщения с поддержкой изображений.
 * Если приложению удается отобразить карточку для пользователя, свойство response.text не используется.
 * @param directives Директивы.
 * @param showItemMeta Параметр историй утреннего шоу Алисы.
 */
@Serializable
data class MessageContentResponse(
    val text: String = "",
    val tts: String? = null,
    @SerialName("end_session")
    val endSession: Boolean = false,
    @SerialName("should_listen")
    val shouldListen: Boolean? = null,
    @SerialName("show_item_meta")
    val showItemMeta : ShowItemMeta? = null,
    val buttons: List<Button> = emptyList(),
    val card: Card? = null,
    val directives: Directives
)
