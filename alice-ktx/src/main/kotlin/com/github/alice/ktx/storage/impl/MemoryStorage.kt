package com.github.alice.ktx.storage.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.storage.Storage
import com.github.alice.ktx.storage.models.*
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

fun Skill.Builder.memoryStorage(body: MemoryStorage.Builder.() -> Unit): MemoryStorage {
    return MemoryStorage.Builder().setJson(json).build(body)
}

open class MemoryStorage(
    private val json: Json
) : Storage {

    private val currentState = mutableMapOf<StorageKey, StorageState>()
    private val currentData = mutableMapOf<StorageKey, MutableMap<StorageKeyData, String>>()

    class Builder {

        private lateinit var json: Json

        fun setJson(json: Json): Builder {
            this.json = json
            return this
        }

        fun build(body: Builder.() -> Unit): MemoryStorage {
            body()
            return MemoryStorage(json = json)
        }
    }

    override suspend fun setState(key: StorageKey, state: StorageState) {
        currentState[key] = state
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> setTypedData(key: StorageKey, clazz: KClass<V>, vararg pairs: StorageTypedData<V>) {
        setData(
            key,
            *pairs.map { it.first to json.encodeToString(clazz.serializer(), it.second) }.toTypedArray()
        )
    }

    override suspend fun setData(key: StorageKey, vararg pairs: StorageData) {
        val data = currentData.getOrDefault(key, mutableMapOf())
        data.clear()
        data.putAll(pairs)
        currentData[key] = data
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
        val data = currentData.getOrDefault(key, mutableMapOf())
        data.putAll(pairs)
        currentData[key] = data
        return data.toMap()
    }

    override suspend fun removeData(key: StorageKey, keyData: StorageKeyData): String? {
        val data = currentData.getOrDefault(key, mutableMapOf())
        return data.remove(keyData)
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> removeTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V? {
        val data = currentData.getOrDefault(key, mutableMapOf())
        val value = data[keyData] ?: return null
        data.remove(keyData)
        return json.decodeFromString(clazz.serializer(), value)
    }

    override suspend fun clear(key: StorageKey) {
        currentState.remove(key)
        currentData.remove(key)
    }

    override suspend fun close() = Unit

    override suspend fun getState(key: StorageKey): StorageState {
        return currentState[key]
    }

    override suspend fun getData(key: StorageKey): Map<StorageKeyData, String> {
        return currentData.getOrDefault(key, mutableMapOf())
    }

    override suspend fun getData(key: StorageKey, keyData: StorageKeyData): String? {
        return getData(key)[keyData]
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> getTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V? {
        val data = currentData.getOrDefault(key, mutableMapOf())
        val value = data[keyData] ?: return null
        return json.decodeFromString(clazz.serializer(), value)
    }
}