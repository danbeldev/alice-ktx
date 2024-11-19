package com.github.alice.ktx.models.request.nlu

import com.github.alice.ktx.common.serializers.NluEntitySerializer
import kotlinx.serialization.Serializable

/**
 * @param tokens Массив слов из произнесенной пользователем фразы.
 * @param entities Массив именованных сущностей.
 * @param intents Объект с данными, извлеченными из пользовательского запроса.
 * */
@Serializable
data class Nlu(
    val tokens: List<String> = emptyList(),
    val entities: List<NluEntity> = emptyList(),
    val intents: Map<String, String> = emptyMap(),
)

/**
 * @param type Тип именованной сущности.
 * @param value Формальное описание именованной сущности.
 * */
@Serializable(with = NluEntitySerializer::class)
data class NluEntity(
    val tokens: NluEntityToken,
    val type: NluEntityType,
    val value: NluValue
)

/**
 * @param start Первое слово именованной сущности.
 * @param end Первое слово после именованной сущности.
 * */
@Serializable
data class NluEntityToken(
    val start: Int,
    val end: Int
)
