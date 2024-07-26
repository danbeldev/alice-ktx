package com.github.alice.models.response

import kotlinx.serialization.Serializable

@Serializable
data class StateResponse(
    val state: String?,
    val data: Map<String, String>?
)