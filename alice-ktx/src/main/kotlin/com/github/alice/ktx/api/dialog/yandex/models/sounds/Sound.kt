package com.github.alice.ktx.api.dialog.yandex.models.sounds

import com.github.alice.ktx.common.LocalDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.LocalDateTime

@Serializable
data class Sound(
    val id: String,
    val skillId: String,
    val size: Int? = null,
    val originalName: String,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAt: LocalDateTime,
    val isProcessed: Boolean,
    val error: String? = null
)