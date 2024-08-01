package com.github.alice.ktx.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * [Source](https://yandex.ru/dev/dialogs/alice/doc/ru/request#request-desc)
 * */
@Serializable
enum class RequestContentType {
    SimpleUtterance,
    ButtonPressed,
    @SerialName("AudioPlayer.PlaybackStarted")
    AudioPlayerPlaybackStarted,
    @SerialName("AudioPlayer.PlaybackFinished")
    AudioPlayerPlaybackFinished,
    @SerialName("AudioPlayer.PlaybackNearlyFinished")
    AudioPlayerPlaybackNearlyFinished,
    @SerialName("AudioPlayer.PlaybackStopped")
    AudioPlayerPlaybackStopped,
    @SerialName("AudioPlayer.PlaybackFailed")
    AudioPlayerPlaybackFailed,
    @SerialName("Purchase.Confirmation")
    PurchaseConfirmation,
    @SerialName("Show.Pull")
    ShowPull
}