package com.github.alice.ktx.models.audioPlayer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioPlayerStream(
    val url: String,
    @SerialName("offset_ms")
    val offsetMs: Int = 0,
    val token: String
)