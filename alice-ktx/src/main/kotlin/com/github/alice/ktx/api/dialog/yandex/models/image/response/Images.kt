package com.github.alice.ktx.api.dialog.yandex.models.image.response

import com.github.alice.ktx.api.dialog.yandex.models.image.Image
import kotlinx.serialization.Serializable

@Serializable
data class Images(
    val total: Int,
    val images: List<Image>
)