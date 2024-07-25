package com.github.alice.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Request(
    val command: String? = null,
    @SerialName("original_utterance")
    val originalUtterance: String? = null,
    val type: String,
    val payload: Map<String, String>? = null
)
