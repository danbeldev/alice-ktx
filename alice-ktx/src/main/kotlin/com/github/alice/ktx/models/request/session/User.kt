package com.github.alice.ktx.models.request.session

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param userId Идентификатор пользователя Яндекса, единый для всех приложений и устройств.
 * @param accessToken Токен для OAuth-авторизации, который также передается в заголовке Authorization для навыков с настроенной
 * [связкой аккаунтов](https://yandex.ru/dev/dialogs/alice/doc/ru/auth/when-to-use).
 * */
@Serializable
data class User(
    @SerialName("user_id")
    val userId: String,
    @SerialName("access_token")
    val accessToken: String? = null
)
