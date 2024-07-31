package com.github.alice.ktx.api.dialog.yandex.models.image

import kotlinx.serialization.Serializable

@Serializable
data class Image(
    val id: String,
    val origUrl: String? = null,
    val size: Int,
    val createdAt: String
)