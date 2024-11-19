package com.github.alice.ktx.models.response.state

import kotlinx.serialization.Serializable

@Serializable
data class StateResponse(
    val state: String?,
    val data: Map<String, String>?
)