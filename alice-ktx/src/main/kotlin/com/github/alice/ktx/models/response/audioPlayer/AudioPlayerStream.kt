package com.github.alice.ktx.models.response.audioPlayer

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/***
 * @param url URL аудиопотока.
 * @param offsetMs Временная метка, с которой необходимо проигрывать трек. Чтобы проиграть трек с начала, нужно передать значение 0.
 * @param token Идентификатор потока. Может быть использован для кеширования изображений или для постановки трека в очередь на стороне навыка.
 */
@Serializable
data class AudioPlayerStream(
    val url: String,
    @SerialName("offset_ms")
    val offsetMs: Int = 0,
    val token: String
)