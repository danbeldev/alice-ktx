package com.github.alice.ktx.models.card

import com.github.alice.ktx.models.button.MediaButton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Card.ImageGalleryBuilder.item(body: CardItem.ImageGalleryBuilder.() -> Unit) {
    val item = CardItem.ImageGalleryBuilder().build(body)
    addItem(item)
}

fun Card.ItemsListBuilder.item(body: CardItem.ItemsListBuilder.() -> Unit) {
    val item = CardItem.ItemsListBuilder().build(body)
    addItem(item)
}

@Serializable
data class CardItem(
    @SerialName("image_id")
    val imageId: String,
    val title: String? = null,
    val description: String? = null,
    val button: MediaButton? = null
) {
    class ImageGalleryBuilder {
        lateinit var imageId: String
        var title: String? = null
        var button: MediaButton? = null

        internal fun build(body: ImageGalleryBuilder.() -> Unit): CardItem {
            body()
            return CardItem(
                imageId = imageId,
                title = title,
                button = button
            )
        }
    }

    class ItemsListBuilder {
        lateinit var imageId: String
        var title: String? = null
        var description: String? = null
        var button: MediaButton? = null

        internal fun build(body: ItemsListBuilder.() -> Unit): CardItem {
            body()
            return CardItem(
                imageId = imageId,
                title = title,
                description = description,
                button = button
            )
        }
    }
}
