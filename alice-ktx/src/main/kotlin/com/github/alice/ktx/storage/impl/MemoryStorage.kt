package com.github.alice.ktx.storage.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.storage.Storage
import com.github.alice.ktx.storage.key.KeyBuilder
import com.github.alice.ktx.storage.key.impl.baseKeyBuilder
import com.github.alice.ktx.storage.models.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Создает экземпляр `MemoryStorage` для хранения данных в памяти.
 *
 * `MemoryStorage` сохраняет все данные в `HashMap` в оперативной памяти и теряет их при остановке или перезапуске.
 *
 * **Внимание:** не рекомендуется для использования в продакшн-окружении, так как данные теряются при каждом перезапуске навыка.
 *
 * @param body лямбда-функция для настройки экземпляра `MemoryStorage` перед его созданием.
 * @return настроенный экземпляр `MemoryStorage`.
 */
@AliceDsl
fun Skill.Builder.memoryStorage(body: MemoryStorage.Builder.() -> Unit = {}): MemoryStorage {
    return MemoryStorage.Builder().json(json).build(body)
}

open class MemoryStorage(
    private val json: Json,
    private val keyBuilder: KeyBuilder
) : Storage {

    private val currentState = mutableMapOf<String, StorageState>()
    private val currentData = mutableMapOf<String, MutableMap<StorageKeyData, String>>()

    @AliceDsl
    class Builder {

        lateinit var json: Json
        var keyBuilder: KeyBuilder = baseKeyBuilder()

        fun json(json: Json): Builder {
            this.json = json
            return this
        }

        fun build(body: Builder.() -> Unit = {}): MemoryStorage {
            body()
            return MemoryStorage(json = json, keyBuilder = keyBuilder)
        }
    }

    override suspend fun setState(key: StorageKey, state: StorageState) {
        currentState[keyBuilder.build(key, "state")] = state
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> setTypedData(key: StorageKey, clazz: KClass<V>, vararg pairs: StorageTypedData<V>) {
        setData(
            key,
            *pairs.map { it.first to json.encodeToString(clazz.serializer(), it.second) }.toTypedArray()
        )
    }

    override suspend fun setData(key: StorageKey, vararg pairs: StorageData) {
        val storageKey = keyBuilder.build(key, "data")
        val data = currentData.getOrDefault(storageKey, mutableMapOf())
        data.clear()
        data.putAll(pairs)
        currentData[storageKey] = data
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> updateTypedData(
        key: StorageKey,
        clazz: KClass<V>,
        vararg pairs: StorageTypedData<V>
    ): Map<StorageKeyData, String> {
        return updateData(
            key,
            *pairs.map { it.first to json.encodeToString(clazz.serializer(), it.second) }.toTypedArray()
        )
    }

    override suspend fun updateData(key: StorageKey, vararg pairs: StorageData): Map<StorageKeyData, String> {
        val storageKey = keyBuilder.build(key, "data")
        val data = currentData.getOrDefault(storageKey, mutableMapOf())
        data.putAll(pairs)
        currentData[storageKey] = data
        return data.toMap()
    }

    override suspend fun removeData(key: StorageKey, keyData: StorageKeyData): String? {
        val storageKey = keyBuilder.build(key, "data")
        val data = currentData.getOrDefault(storageKey, mutableMapOf())
        return data.remove(keyData)
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> removeTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V? {
        val storageKey = keyBuilder.build(key, "data")
        val data = currentData.getOrDefault(storageKey, mutableMapOf())
        val value = data[keyData] ?: return null
        data.remove(keyData)
        return json.decodeFromString(clazz.serializer(), value)
    }

    override suspend fun clear(key: StorageKey) {
        currentState.remove(keyBuilder.build(key, "state"))
        currentData.remove(keyBuilder.build(key, "data"))
    }

    override suspend fun getState(key: StorageKey): StorageState {
        return currentState[keyBuilder.build(key, "state")]
    }

    override suspend fun getData(key: StorageKey): Map<StorageKeyData, String> {
        return currentData.getOrDefault(keyBuilder.build(key, "data"), mutableMapOf())
    }

    override suspend fun getData(key: StorageKey, keyData: StorageKeyData): String? {
        return getData(key)[keyData]
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> getTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V? {
        val data = currentData.getOrDefault(keyBuilder.build(key, "data"), mutableMapOf())
        val value = data[keyData] ?: return null
        return json.decodeFromString(clazz.serializer(), value)
    }
}