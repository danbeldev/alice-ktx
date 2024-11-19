package com.github.alice.ktx.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Markup(
    @SerialName("dangerous_context")
    val dangerousContext: Boolean
)