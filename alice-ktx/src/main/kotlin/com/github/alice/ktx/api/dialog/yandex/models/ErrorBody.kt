package com.github.alice.ktx.api.dialog.yandex.models

import kotlinx.serialization.Serializable

@Serializable
data class ErrorBody(
    val message: String
)
