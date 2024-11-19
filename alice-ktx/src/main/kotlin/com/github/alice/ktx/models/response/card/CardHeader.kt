package com.github.alice.ktx.models.response.card

import kotlinx.serialization.Serializable

/**
 * @param text Текст заголовка.
 * */
@Serializable
data class CardHeader(
    val text: String
)