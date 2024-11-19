package com.github.alice.ktx.models.request

import kotlinx.serialization.Serializable

@Serializable
data class Nlu(
    val tokens: List<String> = emptyList(),
    val intents: Map<String, String> = emptyMap(),
)