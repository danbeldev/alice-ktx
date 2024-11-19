package com.github.alice.ktx.models.response.analytics

import kotlinx.serialization.Serializable

/**
 * Массив событий.
 * */
@Serializable
data class Analytics(
    val events: List<AnalyticsEvent>
)