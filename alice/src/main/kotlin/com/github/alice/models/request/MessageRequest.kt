package com.github.alice.models.request

import kotlinx.serialization.Serializable

@Serializable
data class MessageRequest(
    val meta: Metadata,
    val version: String,
    val session: Session,
    val request: RequestContent,
    val state: State? = null
)
