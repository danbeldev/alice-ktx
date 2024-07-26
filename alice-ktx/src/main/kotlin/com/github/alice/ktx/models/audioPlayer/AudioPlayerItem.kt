package com.github.alice.ktx.models.audioPlayer

import kotlinx.serialization.Serializable

@Serializable
data class AudioPlayerItem(
    val stream: AudioPlayerStream,
    val metadata: AudioPlayerMetadata? = null
)