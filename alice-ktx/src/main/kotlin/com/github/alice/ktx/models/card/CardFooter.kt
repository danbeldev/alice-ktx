package com.github.alice.ktx.models.card

import com.github.alice.ktx.models.button.MediaButton
import kotlinx.serialization.Serializable

fun Card.ItemsListBuilder.footer(body: CardFooter.Builder.() -> Unit) {
    val footer = CardFooter.Builder().build(body)
    this.footer = footer
}

@Serializable
data class CardFooter(
    val text: String,
    val button: MediaButton? = null
){
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