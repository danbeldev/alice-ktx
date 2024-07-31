package com.github.alice.ktx.api.dialog

import com.github.alice.ktx.api.dialog.yandex.models.image.response.ImageUpload
import com.github.alice.ktx.api.dialog.yandex.models.image.response.Images
import com.github.alice.ktx.api.dialog.yandex.models.status.Status
import java.io.File

interface DialogApi {
    suspend fun getStatus(): Status
    suspend fun uploadImage(url: String): ImageUpload
    suspend fun uploadImage(file: File): ImageUpload
    suspend fun getAllImages(): Images
    suspend fun deleteImage(id: String): Boolean
}