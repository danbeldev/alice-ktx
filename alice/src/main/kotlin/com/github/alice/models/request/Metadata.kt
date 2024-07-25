package com.github.alice.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Metadata(
    val locale: String,
    val timezone: String,
    @SerialName("client_id")
    val clientId: String
)