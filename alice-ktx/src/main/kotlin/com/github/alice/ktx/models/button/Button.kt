package com.github.alice.ktx.models.button

import com.github.alice.ktx.models.response.MessageResponse
import kotlinx.serialization.Serializable

fun MessageResponse.Builder.button(body: Button.Builder.() -> Unit) {
    val button = Button.Builder().build(body)
    addButton(button)
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

        internal fun build(body: Builder.() -> Unit): Button {
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
