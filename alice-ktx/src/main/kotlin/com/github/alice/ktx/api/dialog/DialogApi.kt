package com.github.alice.ktx.api.dialog

import com.github.alice.ktx.api.common.Response
import com.github.alice.ktx.api.dialog.yandex.models.image.response.ImageUpload
import com.github.alice.ktx.api.dialog.yandex.models.image.response.Images
import com.github.alice.ktx.api.dialog.yandex.models.status.Status
import java.io.File

interface DialogApi {
    suspend fun getStatus(): Response<Status>
    suspend fun uploadImage(url: String): Response<ImageUpload>
    suspend fun uploadImage(file: File): Response<ImageUpload>
    suspend fun getAllImages(): Response<Images>
    suspend fun deleteImage(id: String): Response<Unit>
}