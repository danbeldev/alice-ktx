package com.github.alice.models.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val text: String,
    @SerialName("end_session")
    val endSession: Boolean,
    val buttons: List<Button>
)
