package com.github.alice.ktx.models.response.card

import com.github.alice.ktx.common.AliceResponseDsl
import com.github.alice.ktx.models.response.button.MediaButton
import kotlinx.serialization.Serializable

@AliceResponseDsl
fun Card.ItemsListBuilder.footer(body: CardFooter.Builder.() -> Unit) {
    val footer = CardFooter.Builder().build(body)
    this.footer = footer
}

/**
 * @param text Текст первой кнопки.
 * @param button Дополнительная кнопка для списка изображений.
 * */
@Serializable
data class CardFooter internal constructor(
    val text: String,
    val button: MediaButton? = null
){
    @AliceResponseDsl
    class Builder {
        lateinit var text: String
        var button: MediaButton? = null

        internal fun build(body: Builder.() -> Unit): CardFooter {
            body()
            return CardFooter(
                text = text,
                button = button
            )
        }
    }
}