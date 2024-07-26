package com.github.alice.models.card

import com.github.alice.models.button.MediaButton
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

        fun build(body: Builder.() -> Unit): CardFooter {
            body()
            return CardFooter(
                text = text,
                button = button
            )
        }
    }
}