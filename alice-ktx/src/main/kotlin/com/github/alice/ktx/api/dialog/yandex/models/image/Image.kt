package com.github.alice.ktx.api.dialog.yandex.models.image

import com.github.alice.ktx.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Image(
    val id: String,
    val origUrl: String? = null,
    val size: Int,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime
)