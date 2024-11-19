package com.github.alice.ktx.models.request.meta

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param locale Язык в POSIX-формате, максимум 64 символа.
 * @param timezone Название часового пояса, включая алиасы, максимум 64 символа.
 * @param clientId Не рекомендуется к использованию. Интерфейсы, доступные на клиентском устройстве, перечислены в свойстве [interfaces].
 * Идентификатор устройства и приложения, в котором идет разговор, максимум 1024 символа.
 * @param interfaces Интерфейсы, доступные на устройстве пользователя.
 * */
@Serializable
data class Metadata(
    val locale: String,
    val timezone: String,
    @SerialName("client_id")
    val clientId: String,
    val interfaces: MetadataInterfaces
)