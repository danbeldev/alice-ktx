package com.github.alice.models.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Session(
    @SerialName("message_id")
    val messageId: Int,
    @SerialName("session_id")
    val sessionId: String,
    @SerialName("skill_id")
    val skillId: String,
    @SerialName("user_id")
    val userId: String,
    val user: User?,
    val application: Application,
    val new: Boolean
)
