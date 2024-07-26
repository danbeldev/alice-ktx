package com.github.alice.models.response

import kotlinx.serialization.Serializable

fun MessageResponse.Builder.button(body: Button.Builder.() -> Unit) {
    val button = Button.Builder().build(body)
    button(button)
}

@Serializable
data class Button(
    val title: String,
    val payload: Map<String, String>?,
    val url: String?,
    val hide: Boolean
) {
    class Builder {
        lateinit var title: String
        var url: String? = null
        var hide: Boolean = true
        var payload: Map<String, String>? = null

        fun build(body: Builder.() -> Unit): Button {
            body()
            return Button(
                title = title,
                url = url,
                hide = hide,
                payload = payload
            )
        }
    }
}
