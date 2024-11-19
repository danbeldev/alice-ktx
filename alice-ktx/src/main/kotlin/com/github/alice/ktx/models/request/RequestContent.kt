package com.github.alice.ktx.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestContent(
    val command: String? = null,
    @SerialName("original_utterance")
    val originalUtterance: String? = null,
    val type: RequestContentType,
    val payload: Map<String, String> = emptyMap(),
    val markup: Markup? = null,
    val nlu: Nlu = Nlu(),
    val tokens: Map<String, String> = emptyMap()
)
