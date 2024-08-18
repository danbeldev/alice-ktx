package com.github.alice.ktx.models.response.analytics

import kotlinx.serialization.Serializable

@Serializable
data class Analytics(
    val events: List<AnalyticsEvent>
)