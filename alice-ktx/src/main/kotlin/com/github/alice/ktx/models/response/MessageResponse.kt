package com.github.alice.ktx.models.response

import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.Request
import com.github.alice.ktx.models.audioPlayer.AudioPlayer
import com.github.alice.ktx.models.button.Button
import com.github.alice.ktx.models.card.Card
import com.github.alice.ktx.models.request.AccountLinking
import com.github.alice.ktx.state.FSMContext
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

fun Request.response(body: MessageResponse.Builder.() -> Unit): MessageResponse {
    return MessageResponse.Builder(this).build(body)
}

/**
 * [Source](https://yandex.ru/dev/dialogs/alice/doc/ru/auth/when-to-use)
 * */
suspend fun Request.authorization(
    onAlreadyAuthenticated: (suspend () -> MessageResponse)? = null,
    onAuthorizationFailed: (suspend () -> MessageResponse)? = null
): MessageResponse {
    if(message.session.user?.accessToken != null && onAlreadyAuthenticated != null)
        return onAlreadyAuthenticated()

    if(message.meta.interfaces.accountLinking == null && onAuthorizationFailed != null)
        return onAuthorizationFailed()

    return MessageResponse.AuthorizationBuilder(request = this).build()
}

/**
 * [Source](https://yandex.ru/dev/dialogs/alice/doc/ru/response)
 * */
@Serializable
data class MessageResponse internal constructor(
    val response: Response?,
    val version: String,
    @SerialName("user_state_update")
    var userState: StateResponse? = null,
    @SerialName("session_state")
    var sessionState: StateResponse? = null,
    @SerialName("application_state")
    var applicationState: StateResponse? = null,
    @SerialName("start_account_linking")
    val startAccountLinking: AccountLinking? = null
) {
    class Builder(private val request: Request) {
        var text: String = ""
        var tts: String? = null
        var endSession: Boolean = false
        var shouldListen: Boolean? = null
        var version: String = request.message.version
        internal var card: Card? = null
        internal var audioPlayer: AudioPlayer? = null
        private val buttons = mutableListOf<Button>()

        fun addButton(button: Button) {
            buttons.add(button)
        }

        fun build(body: Builder.() -> Unit): MessageResponse {
            body()

            val response = MessageResponse(
                response = Response(
                    text = text,
                    tts = tts,
                    endSession = endSession,
                    shouldListen = shouldListen,
                    buttons = buttons,
                    card = card,
                    directives = Directives(
                        audioPlayer = audioPlayer
                    )
                ),
                version = version,
            )

            response.setState(request.state)

            return response
        }
    }

    class AuthorizationBuilder(
        private val request: Request
    ) {
        fun build(): MessageResponse {
            return MessageResponse(
                response = null,
                version = request.message.version,
                startAccountLinking = AccountLinking()
            )
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
