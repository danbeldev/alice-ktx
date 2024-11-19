package com.github.alice.ktx.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MetadataInterfaces(
    @SerialName("account_linking")
    val accountLinking: AccountLinking? = null,
    @SerialName("audio_player")
    val audioPlayer: AudioPlayer? = null,
    val screen: Screen? = null,
    val payments: Payments? = null
)

@Serializable
class AccountLinking

@Serializable
class Screen

@Serializable
class Payments

@Serializable
class AudioPlayer