package com.github.alice.ktx.models.response.audioPlayer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param title Описание трека. Например, название композиции.
 * @param subTitle Дополнительное описание трека. Например, имя артиста.
 * @param art Обложка альбома трека.
 * @param backgroundImage Фоновое изображение.
 * */
@Serializable
data class AudioPlayerMetadata(
    val title: String? = null,
    @SerialName("sub_title")
    val subTitle: String? = null,
    val art: AudioPlayerMetadataArt? = null,
    @SerialName("background_image")
    val backgroundImage: AudioPlayerMetadataBackgroundImage? = null
)

/**
 * @param url URL обложки альбома.
 * */
@Serializable
data class AudioPlayerMetadataArt(
    val url: String?
)

/**
 * @param url URL фонового изображения.
 * */
@Serializable
data class AudioPlayerMetadataBackgroundImage(
    val url: String?
)