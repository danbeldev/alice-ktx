package com.github.alice.models.audioPlayer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AudioPlayerMetadata(
    val title: String? = null,
    @SerialName("sub_title")
    val subTitle: String? = null,
    val art: AudioPlayerMetadataArt? = null,
    @SerialName("background_image")
    val backgroundImage: AudioPlayerMetadataBackgroundImage? = null
)

@Serializable
data class AudioPlayerMetadataArt(
    val url: String?
)

@Serializable
data class AudioPlayerMetadataBackgroundImage(
    val url: String?
)