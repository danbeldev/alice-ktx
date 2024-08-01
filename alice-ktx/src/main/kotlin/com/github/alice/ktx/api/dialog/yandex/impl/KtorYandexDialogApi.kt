package com.github.alice.ktx.api.dialog.yandex.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.api.common.ApiConstants.YandexDialog
import com.github.alice.ktx.api.common.Response
import com.github.alice.ktx.api.common.extensions.response
import com.github.alice.ktx.api.dialog.DialogApi
import com.github.alice.ktx.api.dialog.yandex.models.image.request.ImageUploadUrl
import com.github.alice.ktx.api.dialog.yandex.models.image.response.ImageUpload
import com.github.alice.ktx.api.dialog.yandex.models.image.response.Images
import com.github.alice.ktx.api.dialog.yandex.models.status.Status
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import java.io.File

fun Skill.Builder.ktorYandexDialogApi(body: KtorYandexDialogApi.Builder.() -> Unit): KtorYandexDialogApi =
    KtorYandexDialogApi.Builder().apply(body).build(id)

class KtorYandexDialogApi(
    private val oauthToken: String,
    private val skillId: String
): DialogApi {

    class Builder {

        lateinit var oauthToken: String

        fun build(skillId: String): KtorYandexDialogApi {
            return KtorYandexDialogApi(
                oauthToken = oauthToken,
                skillId = skillId
            )
        }
    }

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
            })
        }

        defaultRequest {
            url(YandexDialog.BASE_URL)
            headers.appendIfNameAbsent("Authorization", "OAuth $oauthToken")
        }
    }

    override suspend fun getStatus(): Response<Status> {
        return client.get(YandexDialog.GET_STATUS_URL).response()
    }

    override suspend fun uploadImage(url: String): Response<ImageUpload> {
        val body = ImageUploadUrl(url = url)
        return client.post(YandexDialog.uploadImageUrl(skillId)) {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.response()
    }

    override suspend fun uploadImage(file: File): Response<ImageUpload> {
        return client.submitFormWithBinaryData(
            url = YandexDialog.uploadImageUrl(skillId),
            formData = formData {
                append("file", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "multipart/form-data")
                    append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                })
            }
        ).response()
    }

    override suspend fun getAllImages(): Response<Images> {
        return client.get(YandexDialog.getAllImagesUrl(skillId)).response()
    }

    override suspend fun deleteImage(id: String): Response<Unit> {
        return client.delete(YandexDialog.deleteImageUrl(skillId, id)).response()
    }
}