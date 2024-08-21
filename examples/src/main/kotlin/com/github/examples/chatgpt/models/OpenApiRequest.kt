package com.github.examples.chatgpt.models

import kotlinx.serialization.Serializable

@Serializable
data class OpenApiRequest(
    val model: String,
    val messages: List<Message>
)

@Serializable
data class Message(
    val role: String,
    val content: String
)