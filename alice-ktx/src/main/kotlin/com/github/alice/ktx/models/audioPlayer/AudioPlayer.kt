package com.github.alice.ktx.models.audioPlayer

import com.github.alice.ktx.models.response.MessageResponse
import kotlinx.serialization.Serializable
import java.util.*

fun MessageResponse.Builder.audioPlayer(body: AudioPlayer.Builder.() -> Unit) {
    val player = AudioPlayer.Builder().build(body)
    audioPlayer = player
}

/**
 * [Source](https://yandex.ru/dev/dialogs/alice/doc/ru/request-audioplayer)
 * */
@Serializable
data class AudioPlayer internal constructor(
    val action: AudioPlayerAction,
    val item: AudioPlayerItem
) {
   class Builder {
       var action = AudioPlayerAction.Play

       lateinit var url: String
       var token = UUID.randomUUID().toString()
       var offsetMs = 0

       var title: String? = null
       var subTitle: String? = null
       var artUrl: String? = null
       var backgroundImageUrl: String? = null

       fun build(body: Builder.() -> Unit): AudioPlayer {
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