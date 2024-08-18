package com.github.alice.ktx.models.response.analytics

import kotlinx.serialization.Serializable

@Serializable
data class AnalyticsEvent(
    val name: String,
    val value: Map<String, String>? = null
)