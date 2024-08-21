package com.github.examples.chatgpt.models

import kotlinx.serialization.Serializable

@Serializable
data class OpenApiResponse(
    val choices: List<Choices>
)

@Serializable
data class Choices(
    val message: Message
)