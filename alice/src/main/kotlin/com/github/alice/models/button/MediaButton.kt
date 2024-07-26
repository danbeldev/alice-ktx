package com.github.alice.models.button

import com.github.alice.models.card.Card
import com.github.alice.models.card.CardFooter
import com.github.alice.models.card.CardItem
import kotlinx.serialization.Serializable

fun Card.BigImageBuilder.mediaButton(body: MediaButton.Builder.() -> Unit) {
    val mediaButton = MediaButton.Builder().build(body)
    button = mediaButton
}

fun CardItem.ImageGalleryBuilder.mediaButton(body: MediaButton.Builder.() -> Unit) {
    val mediaButton = MediaButton.Builder().build(body)
    button = mediaButton
}

fun CardItem.ItemsListBuilder.mediaButton(body: MediaButton.Builder.() -> Unit) {
    val mediaButton = MediaButton.Builder().build(body)
    button = mediaButton
}

fun CardFooter.Builder.mediaButton(body: MediaButton.Builder.() -> Unit) {
    val mediaButton = MediaButton.Builder().build(body)
    button = mediaButton
}

@Serializable
data class MediaButton(
    val text: String? = null,
    val url: String? = null,
    val payload: Map<String, String>? = null
) {
    class Builder {
        var text: String? = null
        var url: String? = null
        var payload: Map<String, String>? = null

        fun build(body: Builder.() -> Unit): MediaButton {
            body()
            return MediaButton(
                text = text,
                url = url,
                payload = payload
            )
        }
    }
}