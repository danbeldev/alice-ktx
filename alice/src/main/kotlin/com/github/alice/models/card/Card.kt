package com.github.alice.models.card

import com.github.alice.models.button.MediaButton
import com.github.alice.models.response.MessageResponse
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun MessageResponse.Builder.bigImage(body: Card.BigImageBuilder.() -> Unit) {
    val card = Card.BigImageBuilder().build(body)
    this.card = card
}

fun MessageResponse.Builder.imageGallery(body: Card.ImageGalleryBuilder.() -> Unit) {
    val card = Card.ImageGalleryBuilder().build(body)
    this.card = card
}

fun MessageResponse.Builder.itemsList(body: Card.ItemsListBuilder.() -> Unit) {
    val card = Card.ItemsListBuilder().build(body)
    this.card = card
}

@Serializable
data class Card internal constructor(
    val type: CardType,
    @SerialName("image_id")
    val imageId: String? = null,
    val title: String? = null,
    val description: String? = null,
    val button: MediaButton? = null,
    val header: CardHeader? = null,
    val items: List<CardItem>? = null,
    val footer: CardFooter? = null
) {
    class BigImageBuilder {
        lateinit var imageId: String
        var title: String? = null
        var description: String? = null
        var button: MediaButton? = null

        fun build(body: BigImageBuilder.() -> Unit): Card {
            body()
            return Card(
                type = CardType.BigImage,
                imageId = imageId,
                title = title,
                description = description,
                button = button
            )
        }
    }

    class ImageGalleryBuilder {
        private val items = mutableListOf<CardItem>()

        fun addItem(item: CardItem) {
            items.add(item)
        }

        fun build(body: ImageGalleryBuilder.() -> Unit): Card {
            body()
            return Card(
                type = CardType.ImageGallery,
                items = items
            )
        }
    }

    class ItemsListBuilder {
        private val items = mutableListOf<CardItem>()
        lateinit var header: String
        var footer: CardFooter? = null

        fun addItem(item: CardItem) {
            items.add(item)
        }

        fun build(body: ItemsListBuilder.() -> Unit): Card {
            body()
            return Card(
                type = CardType.ItemsList,
                items = items,
                footer = footer,
                header = CardHeader(text = header)
            )
        }
    }
}