package com.github.alice.ktx.models.response

import com.github.alice.ktx.models.audioPlayer.AudioPlayer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Directives(
    @SerialName("audio_player")
    val audioPlayer: AudioPlayer?
)
