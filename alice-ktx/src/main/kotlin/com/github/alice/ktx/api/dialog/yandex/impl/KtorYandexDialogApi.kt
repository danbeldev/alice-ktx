package com.github.alice.ktx.api.dialog.yandex.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.api.dialog.DialogApi
import com.github.alice.ktx.api.dialog.yandex.models.image.request.ImageUploadUrl
import com.github.alice.ktx.api.dialog.yandex.models.image.response.DeleteImage
import com.github.alice.ktx.api.dialog.yandex.models.image.response.ImageUpload
import com.github.alice.ktx.api.dialog.yandex.models.image.response.Images
import com.github.alice.ktx.api.dialog.yandex.models.status.Status
import io.ktor.client.*
import io.ktor.client.call.*
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

    companion object {
        const val BASE_URL = "https://dialogs.yandex.net/"
        const val SUCCESS_MESSAGE = "ok"
    }

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
            url(BASE_URL)
            headers.appendIfNameAbsent("Authorization", "OAuth $oauthToken")
        }
    }

    override suspend fun getStatus(): Status {
        return client.get("api/v1/status").body()
    }

    override suspend fun uploadImage(url: String): ImageUpload {
        val body = ImageUploadUrl(url = url)
        return client.post("api/v1/skills/$skillId/images") {
            contentType(ContentType.Application.Json)
            setBody(body)
        }.body()
    }

    override suspend fun uploadImage(file: File): ImageUpload {
        return client.submitFormWithBinaryData(
            url = "api/v1/skills/$skillId/images",
            formData = formData {
                append("file", file.readBytes(), Headers.build {
                    append(HttpHeaders.ContentType, "multipart/form-data")
                    append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
                })
            }
        ).body()
    }

    override suspend fun getAllImages(): Images {
        return client.get("api/v1/skills/$skillId/images").body()
    }

    override suspend fun deleteImage(id: String): Boolean {
        val response = client.delete("api/v1/skills/$skillId/images/$id")
        return response.status == HttpStatusCode.OK && response.body<DeleteImage>().result == SUCCESS_MESSAGE
    }
}