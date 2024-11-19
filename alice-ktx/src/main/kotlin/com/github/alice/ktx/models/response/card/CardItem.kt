package com.github.alice.ktx.models.response.card

import com.github.alice.ktx.common.AliceResponseDsl
import com.github.alice.ktx.models.response.button.MediaButton
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@AliceResponseDsl
fun Card.ImageGalleryBuilder.item(body: CardItem.ImageGalleryBuilder.() -> Unit) {
    val item = CardItem.ImageGalleryBuilder().build(body)
    addItem(item)
}

@AliceResponseDsl
fun Card.ItemsListBuilder.item(body: CardItem.ItemsListBuilder.() -> Unit) {
    val item = CardItem.ItemsListBuilder().build(body)
    addItem(item)
}

/**
 * @param imageId Идентификатор изображения.
 * @param title Заголовок для изображения. Максимум 128 символов.
 * @param description Описание изображения.
 * @param button Свойства кликабельного изображения.
 * */
@Serializable
data class CardItem(
    @SerialName("image_id")
    val imageId: String,
    val title: String? = null,
    val description: String? = null,
    val button: MediaButton? = null
) {
    @AliceResponseDsl
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

    @AliceResponseDsl
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
