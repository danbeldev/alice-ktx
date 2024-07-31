package com.github.alice.ktx.api.dialog.yandex.models.status

import kotlinx.serialization.Serializable

@Serializable
data class Status(
    val images: ImageStatus,
    val sounds: SoundStatus
)
