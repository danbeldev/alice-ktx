package com.github.alice.ktx.models.response

import com.github.alice.ktx.common.AliceResponseDsl
import com.github.alice.ktx.fsm.MutableFSMContext
import com.github.alice.ktx.fsm.ReadOnlyFSMContext
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.audioPlayer.AudioPlayer
import com.github.alice.ktx.models.button.Button
import com.github.alice.ktx.models.card.Card
import com.github.alice.ktx.models.request.AccountLinking
import com.github.alice.ktx.models.response.analytics.Analytics
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@AliceResponseDsl
suspend fun ProcessRequestEnvironment.response(block: MessageResponse.Builder.() -> Unit): MessageResponse {
    return MessageResponse.Builder(message.version)
        .fsmStrategy(fsmStrategy)
        .enableApiStorage(enableApiStorage)
        .apply(block)
        .build(context)
}

/**
 * [Source](https://yandex.ru/dev/dialogs/alice/doc/ru/auth/when-to-use)
 * */
@AliceResponseDsl
suspend fun ProcessRequestEnvironment.authorization(
    onAlreadyAuthenticated: (suspend () -> MessageResponse)? = null,
    onAuthorizationFailed: (suspend () -> MessageResponse)? = null
): MessageResponse {
    if (message.session.user?.accessToken != null && onAlreadyAuthenticated != null)
        return onAlreadyAuthenticated()

    if (message.meta.interfaces.accountLinking == null && onAuthorizationFailed != null)
        return onAuthorizationFailed()

    return MessageResponse.AuthorizationBuilder(version = message.version).build()
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
    val startAccountLinking: AccountLinking? = null,
    val analytics: Analytics? = null
) {
    @AliceResponseDsl
    class Builder(private val version: String) {
        var text: String = ""
        var tts: String? = null
        var endSession: Boolean = false
        var shouldListen: Boolean? = null
        var analytics: Analytics? = null

        internal var card: Card? = null
        internal var audioPlayer: AudioPlayer? = null

        private val buttons = mutableListOf<Button>()
        private var fSMStrategy = FSMStrategy.USER
        private var enableApiStorage = false

        internal fun addButton(button: Button) {
            buttons.add(button)
        }

        internal fun fsmStrategy(strategy: FSMStrategy): Builder {
            fSMStrategy = strategy
            return this
        }

        internal fun enableApiStorage(enable: Boolean): Builder {
            this.enableApiStorage = enable
            return this
        }

        internal suspend fun build(context: MutableFSMContext): MessageResponse {
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
                analytics = analytics
            )

            if (enableApiStorage) response.setState(fSMStrategy, context)

            return response
        }
    }

    @AliceResponseDsl
    internal class AuthorizationBuilder(
        private val version: String
    ) {
        internal fun build(): MessageResponse {
            return MessageResponse(
                response = null,
                version = version,
                startAccountLinking = AccountLinking()
            )
        }
    }

    private suspend fun setState(fSMStrategy: FSMStrategy, state: ReadOnlyFSMContext) {
        val stateResponse = getStateResponse(state.getState(), state.getData())
        when (fSMStrategy) {
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
