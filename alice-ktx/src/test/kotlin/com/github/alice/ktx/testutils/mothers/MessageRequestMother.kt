package com.github.alice.ktx.testutils.mothers

import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.request.content.Markup
import com.github.alice.ktx.models.request.content.RequestContent
import com.github.alice.ktx.models.request.content.RequestContentType
import com.github.alice.ktx.models.request.content.error.RequestError
import com.github.alice.ktx.models.request.content.show.ShowType
import com.github.alice.ktx.models.request.meta.*
import com.github.alice.ktx.models.request.nlu.Nlu
import com.github.alice.ktx.models.request.session.Application
import com.github.alice.ktx.models.request.session.Session
import com.github.alice.ktx.models.request.session.User
import com.github.alice.ktx.models.request.state.State

object MessageRequestMother {

    // Базовые значения по умолчанию
    private const val DEFAULT_VERSION = "1.0"
    private const val DEFAULT_LOCALE = "ru-RU"
    private const val DEFAULT_TIMEZONE = "Europe/Moscow"
    private const val DEFAULT_CLIENT_ID = "test-client-id"
    private const val DEFAULT_MESSAGE_ID = 123
    private const val DEFAULT_SESSION_ID = "test-session-id"
    private const val DEFAULT_SKILL_ID = "test-skill-id"
    private const val DEFAULT_USER_ID = "test-user-id"
    private const val DEFAULT_APPLICATION_ID = "test-app-id"
    private const val DEFAULT_COMMAND = "тестовая команда"

    // Основной метод для создания MessageRequest
    fun createMessageRequest(
        meta: Metadata = createMetadata(),
        version: String = DEFAULT_VERSION,
        session: Session = createSession(),
        request: RequestContent = createRequestContent(),
        state: State? = null,
        accountLinkingCompleteEvent: AccountLinking? = null
    ): MessageRequest {
        return MessageRequest(
            meta = meta,
            version = version,
            session = session,
            request = request,
            state = state,
            accountLinkingCompleteEvent = accountLinkingCompleteEvent
        )
    }

    // Методы для создания компонентов

    fun createMetadata(
        locale: String = DEFAULT_LOCALE,
        timezone: String = DEFAULT_TIMEZONE,
        clientId: String = DEFAULT_CLIENT_ID,
        interfaces: MetadataInterfaces = createMetadataInterfaces()
    ): Metadata {
        return Metadata(
            locale = locale,
            timezone = timezone,
            clientId = clientId,
            interfaces = interfaces
        )
    }

    fun createMetadataInterfaces(
        accountLinking: AccountLinking? = null,
        audioPlayer: AudioPlayer? = null,
        screen: Screen? = Screen,
        payments: Payments? = null
    ): MetadataInterfaces {
        return MetadataInterfaces(
            accountLinking = accountLinking,
            audioPlayer = audioPlayer,
            screen = screen,
            payments = payments
        )
    }

    fun createSession(
        messageId: Int = DEFAULT_MESSAGE_ID,
        sessionId: String = DEFAULT_SESSION_ID,
        skillId: String = DEFAULT_SKILL_ID,
        userId: String = DEFAULT_USER_ID,
        user: User? = null,
        application: Application = createApplication(),
        new: Boolean = true
    ): Session {
        return Session(
            messageId = messageId,
            sessionId = sessionId,
            skillId = skillId,
            userId = userId,
            user = user,
            application = application,
            new = new
        )
    }

    fun createApplication(
        applicationId: String = DEFAULT_APPLICATION_ID
    ): Application {
        return Application(applicationId = applicationId)
    }

    fun createRequestContent(
        command: String? = DEFAULT_COMMAND,
        originalUtterance: String? = DEFAULT_COMMAND,
        type: RequestContentType = RequestContentType.SimpleUtterance,
        markup: Markup? = null,
        nlu: Nlu = Nlu(),
        tokens: Map<String, String> = emptyMap(),
        payload: Map<String, String> = emptyMap(),
        showType: ShowType? = null,
        purchaseRequestId: String? = null,
        purchaseToken: String? = null,
        orderId: String? = null,
        purchaseTimestamp: Long? = null,
        purchasePayload: Map<String, String> = emptyMap(),
        signedData: String? = null,
        signature: String? = null,
        error: RequestError? = null
    ): RequestContent {
        return RequestContent(
            command = command,
            originalUtterance = originalUtterance,
            type = type,
            markup = markup,
            nlu = nlu,
            tokens = tokens,
            payload = payload,
            showType = showType,
            purchaseRequestId = purchaseRequestId,
            purchaseToken = purchaseToken,
            orderId = orderId,
            purchaseTimestamp = purchaseTimestamp,
            purchasePayload = purchasePayload,
            signedData = signedData,
            signature = signature,
            error = error
        )
    }

    // Предопределенные сценарии

    fun createSimpleUtteranceRequest(command: String = DEFAULT_COMMAND): MessageRequest {
        return createMessageRequest(
            request = createRequestContent(
                command = command,
                originalUtterance = command,
                type = RequestContentType.SimpleUtterance
            )
        )
    }

    fun createButtonPressedRequest(payload: Map<String, String>): MessageRequest {
        return createMessageRequest(
            request = createRequestContent(
                type = RequestContentType.ButtonPressed,
                payload = payload
            )
        )
    }

    fun createAudioPlayerRequest(): MessageRequest {
        return createMessageRequest(
            meta = createMetadata(
                interfaces = createMetadataInterfaces(audioPlayer = AudioPlayer)
            ),
            request = createRequestContent(type = RequestContentType.AudioPlayerPlaybackStarted)
        )
    }

    fun createScreenRequest(): MessageRequest {
        return createMessageRequest(
            meta = createMetadata(
                interfaces = createMetadataInterfaces(screen = Screen)
            )
        )
    }

    fun createAccountLinkingRequest(): MessageRequest {
        return createMessageRequest(
            meta = createMetadata(
                interfaces = createMetadataInterfaces(accountLinking = AccountLinking)
            ),
            accountLinkingCompleteEvent = AccountLinking
        )
    }
}