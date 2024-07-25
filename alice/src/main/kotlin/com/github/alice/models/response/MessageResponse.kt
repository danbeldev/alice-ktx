package com.github.alice.models.response

import com.github.alice.handlers.MessageHandlerEnvironment
import com.github.alice.models.request.MessageRequest
import kotlinx.serialization.Serializable

fun MessageHandlerEnvironment.response(body: MessageResponse.Builder.() -> Unit): MessageResponse {
    return MessageResponse.Builder(message).build(body)
}

@Serializable
data class MessageResponse internal constructor(
    val response: Response,
    val version: String,
) {
    class Builder(request: MessageRequest) {
        lateinit var text: String
        var endSession: Boolean = false
        var version: String = request.version
        private val buttons = mutableListOf<Button>()

        internal fun addButton(button: Button) {
            buttons.add(button)
        }

        fun build(body: Builder.() -> Unit): MessageResponse {
            body()
            return MessageResponse(
                response = Response(
                    text = text,
                    endSession = endSession,
                    buttons = buttons
                ),
                version = version
            )
        }
    }
}
