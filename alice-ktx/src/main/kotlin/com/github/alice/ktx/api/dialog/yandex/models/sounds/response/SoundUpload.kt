package com.github.alice.ktx.api.dialog.yandex.models.sounds.response

import com.github.alice.ktx.api.dialog.yandex.models.sounds.Sound
import kotlinx.serialization.Serializable

@Serializable
data class SoundUpload(
    val sound: Sound
)