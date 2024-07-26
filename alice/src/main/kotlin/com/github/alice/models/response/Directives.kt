package com.github.alice.models.response

import com.github.alice.models.audioPlayer.AudioPlayer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Directives(
    @SerialName("audio_player")
    val audioPlayer: AudioPlayer?
)
