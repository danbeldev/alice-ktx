package com.github.alice.ktx.api.common

object ApiConstants {
    object YandexDialog {
        const val BASE_URL = "https://dialogs.yandex.net/"
        const val GET_STATUS_URL = "api/v1/status"
        fun uploadImageUrl(skillId: String) = "api/v1/skills/$skillId/images"
        fun getAllImagesUrl(skillId: String) = "api/v1/skills/$skillId/images"
        fun deleteImageUrl(skillId: String, imageId: String) = "api/v1/skills/$skillId/images/$imageId"

        fun uploadSoundUrl(skillId: String) = "api/v1/skills/$skillId/sounds"
        fun getAllSoundsUrl(skillId: String) = "api/v1/skills/$skillId/sounds"
        fun deleteSoundUrl(skillId: String, soundId: String) = "/api/v1/skills/$skillId/sounds/$soundId"
    }
}