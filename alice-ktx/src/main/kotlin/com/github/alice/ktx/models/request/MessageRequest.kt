package com.github.alice.ktx.models.request

import com.github.alice.ktx.models.request.content.RequestContent
import com.github.alice.ktx.models.request.meta.AccountLinking
import com.github.alice.ktx.models.request.meta.Metadata
import com.github.alice.ktx.models.request.session.Session
import com.github.alice.ktx.models.request.state.State
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [Source](https://yandex.ru/dev/dialogs/alice/doc/ru/request)
 *
 * @param meta Информация об устройстве, с помощью которого пользователь разговаривает с Алисой.
 * @param version Версия протокола.
 * @param session Данные о сессии. Сессия — это период относительно непрерывного взаимодействия пользователя с навыком.
 * @param request Данные, полученные от пользователя.
 * @param state Данные о сохраненном состоянии.
 * */
@Serializable
data class MessageRequest(
    val meta: Metadata,
    val version: String,
    val session: Session,
    val request: RequestContent,
    val state: State? = null,
    @SerialName("account_linking_complete_event")
    val accountLinkingCompleteEvent: AccountLinking? = null
)
