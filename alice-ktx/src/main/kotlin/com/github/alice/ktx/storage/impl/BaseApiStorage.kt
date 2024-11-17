package com.github.alice.ktx.storage.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.storage.apiStorage.ApiStorageDetails
import com.github.alice.ktx.storage.apiStorage.EnableApiStorage
import com.github.alice.ktx.storage.key.KeyBuilder
import com.github.alice.ktx.storage.key.impl.baseKeyBuilder
import com.github.alice.ktx.storage.models.StorageKey
import com.github.alice.ktx.storage.models.StorageKeyData
import com.github.alice.ktx.storage.models.StorageState
import kotlinx.serialization.json.Json

/**
 * Создает экземпляр `BaseApiStorage` для сохранения данных в хранилище Алисы.
 *
 * Данный класс реализует сохранение данных в хранилище Алисы, и позволяет работать с данными, предоставленными интерфейсом `Storage`.
 *
 * @param body лямбда-функция для настройки экземпляра `BaseApiStorage` перед его созданием.
 * @return настроенный экземпляр `BaseApiStorage`, который будет использовать хранилище Алисы для сохранения данных.
 */
fun Skill.Builder.apiStorage(body: BaseApiStorage.Builder.() -> Unit = {}): BaseApiStorage {
    return BaseApiStorage.Builder().setJson(json).build(body)
}

@EnableApiStorage
class BaseApiStorage(
    json: Json,
    keyBuilder: KeyBuilder
) : ApiStorageDetails, MemoryStorage(json, keyBuilder) {

    class Builder {

        private lateinit var json: Json

        var keyBuilder: KeyBuilder = baseKeyBuilder()

        fun setJson(json: Json): Builder {
            this.json = json
            return this
        }

        fun build(body: Builder.() -> Unit): BaseApiStorage {
            body()
            return BaseApiStorage(json = json, keyBuilder = keyBuilder)
        }
    }

    override suspend fun setCurrentState(key: StorageKey, state: StorageState) {
        setState(key, state)
    }

    override suspend fun setCurrentData(key: StorageKey, data: Map<StorageKeyData, String>?) {
        setData(key, *(data?.toList() ?: emptyList()).toTypedArray())
    }
}