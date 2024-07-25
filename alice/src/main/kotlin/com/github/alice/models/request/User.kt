package com.github.alice.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class User(
    @SerialName("user_id")
    val userId: String,
    @SerialName("access_token")
    val accessToken: String? = null
)
