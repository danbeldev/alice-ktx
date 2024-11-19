package com.github.alice.ktx.api.dialog.yandex.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.api.common.ApiConstants.YandexDialog
import com.github.alice.ktx.api.common.Response
import com.github.alice.ktx.api.common.extensions.response
import com.github.alice.ktx.api.dialog.DialogApi
import com.github.alice.ktx.api.dialog.yandex.models.image.request.ImageUploadUrl
import com.github.alice.ktx.api.dialog.yandex.models.image.response.ImageUpload
import com.github.alice.ktx.api.dialog.yandex.models.image.response.Images
import com.github.alice.ktx.api.dialog.yandex.models.sounds.response.SoundUpload
import com.github.alice.ktx.api.dialog.yandex.models.sounds.response.Sounds
import com.github.alice.ktx.api.dialog.yandex.models.status.Status
import com.github.alice.ktx.common.AliceDsl
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import java.io.File

@AliceDsl
fun Skill.Builder.ktorYandexDialogApi(body: KtorYandexDialogApi.Builder.() -> Unit): KtorYandexDialogApi {
    val id = skillId
        ?: throw IllegalArgumentException("Skill ID не может быть null. Убедитесь, что ID установлен перед вызовом метода.")
    return KtorYandexDialogApi.Builder().json(json).apply(body).build(id)
}

/**
 * @param oauthToken Токен для загрузки аудио и изображений.
 * [Source](https://yandex.ru/dev/direct/doc/start/token.html)
 * */
class KtorYandexDialogApi internal constructor(
    private val oauthToken: String,
    private val skillId: String,
    private val json: Json,
    private val configuration: HttpClientConfig<CIOEngineConfig>.() -> Unit
): DialogApi {

    @AliceDsl
    class Builder {

        lateinit var oauthToken: String
        lateinit var json: Json

        var configuration: HttpClientConfig<CIOEngineConfig>.() -> Unit = {}

        internal fun json(json: Json): Builder {
            this.json = json
            return this
        }

        fun build(skillId: String): KtorYandexDialogApi {
            return KtorYandexDialogApi(
                oauthToken = oauthToken,
                skillId = skillId,
                json = json,
                configuration = configuration
            )
        }
    }

    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(json)
        }

        configuration()

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
            formData = uploadBaseFormData(file)
        ).response()
    }

    override suspend fun getAllImages(): Response<Images> {
        return client.get(YandexDialog.getAllImagesUrl(skillId)).response()
    }

    override suspend fun deleteImage(id: String): Response<Unit> {
        return client.delete(YandexDialog.deleteImageUrl(skillId, id)).response()
    }

    override suspend fun uploadSound(file: File): Response<SoundUpload> {
        return client.submitFormWithBinaryData(
            url = YandexDialog.uploadSoundUrl(skillId),
            formData = uploadBaseFormData(file)
        ).response()
    }

    override suspend fun getAllSounds(): Response<Sounds> {
        return client.get(YandexDialog.getAllSoundsUrl(skillId)).response()
    }

    override suspend fun deleteSound(id: String): Response<Unit> {
        return client.delete(YandexDialog.deleteSoundUrl(skillId, id)).response()
    }

    private fun uploadBaseFormData(file: File): List<PartData> = formData {
        append("file", file.readBytes(), Headers.build {
            append(HttpHeaders.ContentType, "multipart/form-data")
            append(HttpHeaders.ContentDisposition, "filename=\"${file.name}\"")
        })
    }
}