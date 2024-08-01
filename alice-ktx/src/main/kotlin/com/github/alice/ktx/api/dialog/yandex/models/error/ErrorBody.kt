package com.github.alice.ktx.api.dialog.yandex.models.error

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    val message: String
)
