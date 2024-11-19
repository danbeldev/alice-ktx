package com.github.alice.ktx.models.response.show

import com.github.alice.ktx.common.AliceResponseDsl
import com.github.alice.ktx.models.response.MessageResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@AliceResponseDsl
fun MessageResponse.Builder.show(block: ShowItemMeta.Builder.() -> Unit) {
    showItemMeta = ShowItemMeta.Builder().apply(block).build()
}

/**
 * @param contentId Уникальный идентификатор истории. Формат UUID предпочтителен, но не является обязательным.
 * @param title Заголовок истории для экрана.
 * @param titleTts Заголовок истории с голосовой разметкой (text-to-speech).
 * @param publicationDate Дата и время создания истории. Алиса игнорирует истории старше 7 дней.
 * @param expirationDate Дата и время, до которого история будет актуальна. Алиса не добавит в шоу историю,
 * у которой значение expiration_date раньше, чем время запуска шоу.
 * */
@Serializable
data class ShowItemMeta internal constructor(
    @SerialName("content_id")
    val contentId: String,
    val title: String? = null,
    @SerialName("title_tts")
    val titleTts: String? = null,
    @SerialName("publication_date")
    val publicationDate: String? = null,
    @SerialName("expiration_date")
    val expirationDate: String? = null
) {
    @AliceResponseDsl
    class Builder {
        lateinit var contentId: String

        var title: String? = null
        var titleTts: String? = null
        var publicationDate: String? = null
        var expirationDate: String? = null

        fun build(): ShowItemMeta {
            return ShowItemMeta(
                contentId = contentId,
                title = title,
                titleTts = titleTts,
                publicationDate = publicationDate,
                expirationDate = expirationDate
            )
        }
    }
}
