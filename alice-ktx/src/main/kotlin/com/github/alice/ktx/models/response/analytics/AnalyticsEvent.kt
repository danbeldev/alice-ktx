package com.github.alice.ktx.models.response.analytics

import kotlinx.serialization.Serializable

/**
 * @param name Название события.
 * @param value JSON-объект для многоуровневых событий. Допустимо не более пяти уровней вложенности события.
 * */
@Serializable
data class AnalyticsEvent(
    val name: String,
    val value: Map<String, String>? = null
)