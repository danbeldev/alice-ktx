package com.github.alice.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val command: String,
    @SerialName("original_utterance")
    val originalUtterance: String,
    val type: String
)
