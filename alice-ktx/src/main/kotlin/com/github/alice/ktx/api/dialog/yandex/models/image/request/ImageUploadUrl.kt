package com.github.alice.ktx.api.dialog.yandex.models.image.request

import kotlinx.serialization.Serializable

@Serializable
data class ImageUploadUrl(
    val url: String
)