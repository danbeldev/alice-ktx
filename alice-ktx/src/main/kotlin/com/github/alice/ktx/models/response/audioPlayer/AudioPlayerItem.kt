package com.github.alice.ktx.models.response.audioPlayer

import kotlinx.serialization.Serializable

/**
 * @param stream Описание аудиопотока.
 * @param metadata Метаданные проигрываемого трека.
 * */
@Serializable
data class AudioPlayerItem(
    val stream: AudioPlayerStream,
    val metadata: AudioPlayerMetadata? = null
)