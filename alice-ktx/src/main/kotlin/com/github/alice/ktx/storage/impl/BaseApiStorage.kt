package com.github.alice.ktx.storage.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.common.serializer.Serializer
import com.github.alice.ktx.storage.apiStorage.ApiStorageDetails
import com.github.alice.ktx.storage.apiStorage.EnableApiStorage
import com.github.alice.ktx.storage.key.KeyBuilder
import com.github.alice.ktx.storage.key.impl.baseKeyBuilder
import com.github.alice.ktx.storage.models.StorageKey
import com.github.alice.ktx.storage.models.StorageKeyData
import com.github.alice.ktx.storage.models.StorageState

/**
 * Создает экземпляр `BaseApiStorage` для сохранения данных в хранилище Алисы.
 *
 * Данный класс реализует сохранение данных в хранилище Алисы, и позволяет работать с данными, предоставленными интерфейсом `Storage`.
 *
 * @param body лямбда-функция для настройки экземпляра `BaseApiStorage` перед его созданием.
 * @return настроенный экземпляр `BaseApiStorage`, который будет использовать хранилище Алисы для сохранения данных.
 */
@AliceDsl
fun Skill.Builder.apiStorage(body: BaseApiStorage.Builder.() -> Unit = {}): BaseApiStorage {
    return BaseApiStorage.Builder().serializer(serializer).build(body)
}

@EnableApiStorage
class BaseApiStorage internal constructor(
    serializer: Serializer,
    keyBuilder: KeyBuilder
) : ApiStorageDetails, MemoryStorage(serializer, keyBuilder) {

    @AliceDsl
    class Builder {

        private lateinit var serializer: Serializer

        var keyBuilder: KeyBuilder = baseKeyBuilder()

        fun serializer(serializer: Serializer): Builder {
            this.serializer = serializer
            return this
        }

        fun build(body: Builder.() -> Unit): BaseApiStorage {
            body()
            return BaseApiStorage(serializer = serializer, keyBuilder = keyBuilder)
        }
    }

    override suspend fun setCurrentState(key: StorageKey, state: StorageState) {
        setState(key, state)
    }

    override suspend fun setCurrentData(key: StorageKey, data: Map<StorageKeyData, String>?) {
        setData(key, *(data?.toList() ?: emptyList()).toTypedArray())
    }
}