package com.github.alice.models.response

import com.github.alice.models.button.Button
import com.github.alice.models.card.Card
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val text: String,
    val tts: String? = null,
    @SerialName("end_session")
    val endSession: Boolean,
    val buttons: List<Button>,
    val card: Card? = null
)
