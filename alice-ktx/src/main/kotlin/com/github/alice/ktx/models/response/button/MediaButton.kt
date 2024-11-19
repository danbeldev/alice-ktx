package com.github.alice.ktx.models.response.button

import com.github.alice.ktx.common.AliceResponseDsl
import com.github.alice.ktx.models.response.card.Card
import com.github.alice.ktx.models.response.card.CardFooter
import com.github.alice.ktx.models.response.card.CardItem
import kotlinx.serialization.Serializable

@AliceResponseDsl
fun Card.BigImageBuilder.mediaButton(body: MediaButton.Builder.() -> Unit) {
    val mediaButton = MediaButton.Builder().build(body)
    button = mediaButton
}

@AliceResponseDsl
fun CardItem.ImageGalleryBuilder.mediaButton(body: MediaButton.Builder.() -> Unit) {
    val mediaButton = MediaButton.Builder().build(body)
    button = mediaButton
}

@AliceResponseDsl
fun CardItem.ItemsListBuilder.mediaButton(body: MediaButton.Builder.() -> Unit) {
    val mediaButton = MediaButton.Builder().build(body)
    button = mediaButton
}

@AliceResponseDsl
fun CardFooter.Builder.mediaButton(body: MediaButton.Builder.() -> Unit) {
    val mediaButton = MediaButton.Builder().build(body)
    button = mediaButton
}

/**
 * @param text Текст, который будет отправлен навыку по нажатию на изображение в качестве команды пользователя.
 * Максимум 64 символа.
 * Если это свойство передано с пустым значением, свойство request.command в запросе будет отправлено пустым.
 * Если это свойство не передано в ответе, Диалоги используют вместо него свойство response.card.title.
 * @param url URL, который должен открываться по нажатию изображения.
 * @param payload Произвольный JSON-объект, который Яндекс Диалоги должны отправить обработчику, если пользователь нажмет изображение. Максимум 4096 байт.
 * */
@Serializable
data class MediaButton internal constructor(
    val text: String? = null,
    val url: String? = null,
    val payload: Map<String, String>? = null
) {
    @AliceResponseDsl
    class Builder {
        var text: String? = null
        var url: String? = null
        var payload: Map<String, String>? = null

        internal fun build(body: Builder.() -> Unit): MediaButton {
            body()
            return MediaButton(
                text = text,
                url = url,
                payload = payload
            )
        }
    }
}