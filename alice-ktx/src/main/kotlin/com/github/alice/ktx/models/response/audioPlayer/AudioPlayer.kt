package com.github.alice.ktx.models.response.audioPlayer

import com.github.alice.ktx.common.AliceResponseDsl
import com.github.alice.ktx.models.response.MessageResponse
import kotlinx.serialization.Serializable
import java.util.*

@AliceResponseDsl
fun MessageResponse.Builder.audioPlayer(body: AudioPlayer.Builder.() -> Unit) {
    val player = AudioPlayer.Builder().build(body)
    audioPlayer = player
}

/**
 * [Source 1](https://yandex.ru/dev/dialogs/alice/doc/ru/request-audioplayer)
 * [Source 2](https://yandex.ru/dev/dialogs/alice/doc/ru/response-audio-player)
 * @param action Команда директиве.
 * Play — проигрывание трека, начинается сразу после отправки директивы. Проигрывание текущего трека приостанавливается.
 * Stop — остановка трека.
 * @param item Описание трека и аудиопотока.
 * */
@Serializable
data class AudioPlayer internal constructor(
    val action: AudioPlayerAction,
    val item: AudioPlayerItem
) {
    @AliceResponseDsl
    class Builder {
        var action = AudioPlayerAction.Play

        lateinit var url: String
        var token = UUID.randomUUID().toString()
        var offsetMs = 0

        var title: String? = null
        var subTitle: String? = null
        var artUrl: String? = null
        var backgroundImageUrl: String? = null

        internal fun build(body: Builder.() -> Unit): AudioPlayer {
            body()
            return AudioPlayer(
                action = action,
                item = AudioPlayerItem(
                    stream = AudioPlayerStream(
                        url = url,
                        offsetMs = offsetMs,
                        token = token
                    ),
                    metadata = AudioPlayerMetadata(
                        title = title,
                        subTitle = subTitle,
                        art = AudioPlayerMetadataArt(
                            url = artUrl
                        ),
                        backgroundImage = AudioPlayerMetadataBackgroundImage(
                            url = backgroundImageUrl
                        )
                    )
                )
            )
        }
    }
}