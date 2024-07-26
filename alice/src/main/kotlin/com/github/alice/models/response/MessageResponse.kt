package com.github.alice.models.response

import com.github.alice.models.FSMStrategy
import com.github.alice.models.Request
import com.github.alice.models.button.Button
import com.github.alice.models.card.Card
import com.github.alice.state.FSMContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Request.response(body: MessageResponse.Builder.() -> Unit): MessageResponse {
    return MessageResponse.Builder(this).build(body)
}

@Serializable
data class MessageResponse internal constructor(
    val response: Response,
    val version: String,
    @SerialName("user_state_update")
    var userState: StateResponse? = null,
    @SerialName("session_state")
    var sessionState: StateResponse? = null,
    @SerialName("application_state")
    var applicationState: StateResponse? = null,
) {
    class Builder(private val request: Request) {
        lateinit var text: String
        var endSession: Boolean = false
        var version: String = request.message.version
        internal var card: Card? = null
        private val buttons = mutableListOf<Button>()

        fun addButton(button: Button) {
            buttons.add(button)
        }

        fun build(body: Builder.() -> Unit): MessageResponse {
            body()

            val response = MessageResponse(
                response = Response(
                    text = text,
                    endSession = endSession,
                    buttons = buttons,
                    card = card
                ),
                version = version,
            )

            response.setState(request.state)

            return response
        }
    }

    private fun setState(state: FSMContext) {
        val stateResponse = getStateResponse(state.getState(), state.getData())
        when (state.getStrategy()) {
            FSMStrategy.USER -> userState = stateResponse
            FSMStrategy.SESSION -> sessionState = stateResponse
            FSMStrategy.APPLICATION -> applicationState = stateResponse
        }
    }

    private fun getStateResponse(state: String?, data: Map<String, String>?): StateResponse {
        return StateResponse(
            state = state,
            data = data
        )
    }
}
