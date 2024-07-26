package com.github.alice.ktx.models.response

import com.github.alice.ktx.models.button.Button
import com.github.alice.ktx.models.card.Card
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Response(
    val text: String,
    val tts: String? = null,
    @SerialName("end_session")
    val endSession: Boolean,
    @SerialName("should_listen")
    val shouldListen: Boolean? = null,
    val buttons: List<Button>,
    val card: Card? = null,
    val directives: Directives? = null
)
