package com.github.alice.ktx.models.request.meta

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param accountLinking У пользователя есть возможность запросить связку аккаунтов.
 * @param audioPlayer На устройстве пользователя есть аудиоплеер.
 * @param screen Пользователь может видеть ответ навыка на экране и открывать ссылки в браузере.
 * */
@Serializable
data class MetadataInterfaces(
    @SerialName("account_linking")
    val accountLinking: AccountLinking? = null,
    @SerialName("audio_player")
    val audioPlayer: AudioPlayer? = null,
    val screen: Screen? = null,
    val payments: Payments? = null
)

@Serializable
data object AccountLinking

@Serializable
data object Screen

@Serializable
data object Payments

@Serializable
data object AudioPlayer