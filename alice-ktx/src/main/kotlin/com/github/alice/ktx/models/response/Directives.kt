package com.github.alice.ktx.models.response

import com.github.alice.ktx.models.response.audioPlayer.AudioPlayer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/***
 * @param audioPlayer Позволяет управлять плеером.
 * @param startAccountLinking Директива запуска авторизации для связки аккаунтов.
 */
@Serializable
data class Directives(
    @SerialName("audio_player")
    val audioPlayer: AudioPlayer? = null,
    @SerialName("start_account_linking")
    val startAccountLinking: StartAccountLinking? = null
)

@Serializable
data object StartAccountLinking
