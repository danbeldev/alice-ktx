package com.github.alice.ktx.models.response

import com.github.alice.ktx.common.AliceResponseDsl
import com.github.alice.ktx.fsm.MutableFSMContext
import com.github.alice.ktx.fsm.ReadOnlyFSMContext
import com.github.alice.ktx.fsm.models.FSMStrategy
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.models.response.analytics.Analytics
import com.github.alice.ktx.models.response.audioPlayer.AudioPlayer
import com.github.alice.ktx.models.response.button.Button
import com.github.alice.ktx.models.response.card.Card
import com.github.alice.ktx.models.response.show.ShowItemMeta
import com.github.alice.ktx.models.response.state.StateResponse
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
    onAlreadyAuthenticated: suspend () -> MessageResponse,
    onAuthorizationFailed: suspend () -> MessageResponse
): MessageResponse {
    if (message.session.user?.accessToken != null)
        return onAlreadyAuthenticated()

    if (message.meta.interfaces.accountLinking == null)
        return onAuthorizationFailed()

    return MessageResponse.AuthorizationBuilder(version = message.version).build()
}

/**
 * [Source](https://yandex.ru/dev/dialogs/alice/doc/ru/response)
 *
 * Время ожидания ответа от навыка — 4,5 секунды. Если Диалоги не получат ответ в течение этого времени,
 * сессия навыка завершится. Алиса сообщит пользователю, что навык не отвечает.
 *
 * @param response Данные для ответа пользователю.
 * @param sessionState Объект, содержащий состояние навыка для хранения в контексте сессии.
 * @param userState Объект, содержащий состояние навыка для хранения в контексте авторизованного пользователя.
 * @param applicationState Объект, содержащий состояние навыка для хранения в контексте экземпляра приложения пользователя.
 * @param analytics Объект с данными для аналитики.
 * @param version Версия протокола.
 * */
@Serializable
data class MessageResponse internal constructor(
    val response: MessageContentResponse?,
    val version: String,
    @SerialName("user_state_update")
    var userState: StateResponse? = null,
    @SerialName("session_state")
    var sessionState: StateResponse? = null,
    @SerialName("application_state")
    var applicationState: StateResponse? = null,
    @SerialName("start_account_linking")
    val startAccountLinking: StartAccountLinking? = null,
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
        internal var showItemMeta: ShowItemMeta? = null

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
                response = MessageContentResponse(
                    text = text,
                    tts = tts,
                    endSession = endSession,
                    shouldListen = shouldListen,
                    buttons = buttons,
                    card = card,
                    showItemMeta = showItemMeta,
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
    class AuthorizationBuilder(
        private val version: String
    ) {
        internal fun build(): MessageResponse {
            return MessageResponse(
                response = null,
                startAccountLinking = StartAccountLinking,
                version = version
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
