package com.github.alice.ktx.storage.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.storage.Storage
import com.github.alice.ktx.storage.impl.RedisStorage.Builder
import com.github.alice.ktx.storage.key.KeyBuilder
import com.github.alice.ktx.storage.key.impl.baseKeyBuilder
import com.github.alice.ktx.storage.models.*
import io.lettuce.core.RedisClient
import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.future.await
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

fun Skill.Builder.redisStorage(body: Builder.() -> Unit): RedisStorage {
    return Builder().json(json).build(body)
}

class RedisStorage(
    private val redis: RedisAsyncCommands<String, String>,
    private val keyBuilder: KeyBuilder,
    private val json: Json,
    private val stateTtl: Long? = null,
    private val dataTtl: Long? = null
) : Storage {

    class Builder {

        lateinit var redis: RedisAsyncCommands<String, String>

        var keyBuilder: KeyBuilder = baseKeyBuilder()

        lateinit var json: Json

        var stateTtl: Long? = null
        var dataTtl: Long? = null

        internal fun json(json: Json): Builder {
            this.json = json
            return this
        }

        fun connect(host: String, port: Int = 6379, password: String? = null): Builder {
            if (password != null) return connect("redis://:$password@$host:$port")
            return connect("redis://:$host:$port")
        }

        fun connect(uri: String): Builder {
            val redisClient = RedisClient.create(uri)
            redis = redisClient.connect().async()
            return this
        }

        fun build(body: Builder.() -> Unit): RedisStorage {
            body()

            return RedisStorage(
                redis = redis,
                keyBuilder = keyBuilder,
                json = json,
                stateTtl = stateTtl,
                dataTtl = dataTtl
            )
        }
    }

    override suspend fun setState(key: StorageKey, state: StorageState) {
        val storageKey = keyBuilder.build(key, "state")
        redis.set(storageKey, state).await()
        stateTtl?.let { redis.expire(storageKey, it).await() }
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> setTypedData(key: StorageKey, clazz: KClass<V>, vararg pairs: StorageTypedData<V>) {
        val storageKey = keyBuilder.build(key, "data")
        redis.hset(storageKey, pairs.toMap().mapValues { json.encodeToString(clazz.serializer(), it.value) }).await()
        dataTtl?.let { redis.expire(storageKey, it).await() }
    }

    override suspend fun setData(key: StorageKey, vararg pairs: StorageData) {
        val storageKey = keyBuilder.build(key, "data")
        redis.hset(storageKey, pairs.toMap()).await()
        dataTtl?.let { redis.expire(storageKey, it).await() }
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> updateTypedData(
        key: StorageKey,
        clazz: KClass<V>,
        vararg pairs: StorageTypedData<V>
    ): Map<StorageKeyData, String> {
        val result = mutableMapOf<StorageKeyData, String>()
        val storageKey = keyBuilder.build(key, "data")
        pairs.forEach {
            val value = json.encodeToString(clazz.serializer(), it.second)
            redis.hset(storageKey, it.first, value).await()
            result[it.first] = value
        }
        dataTtl?.let { redis.expire(storageKey, it).await() }
        return result
    }

    override suspend fun updateData(key: StorageKey, vararg pairs: StorageData): Map<StorageKeyData, String> {
        val result = mutableMapOf<StorageKeyData, String>()
        val storageKey = keyBuilder.build(key, "data")
        pairs.forEach {
            redis.hset(storageKey, it.first, it.second).await()
            result[it.first] = it.second
        }
        dataTtl?.let { redis.expire(storageKey, it).await() }
        return result
    }

    override suspend fun removeData(key: StorageKey, keyData: StorageKeyData): String? {
        val storageKey = keyBuilder.build(key, "data")
        val value = redis.hget(storageKey, keyData).await()
        redis.hdel(storageKey, keyData).await()
        return value
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> removeTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V? {
        val storageKey = keyBuilder.build(key, "data")
        val value = redis.hget(storageKey, keyData).await() ?: return null
        redis.hdel(storageKey, keyData).await()
        return json.decodeFromString(clazz.serializer(), value)
    }

    override suspend fun clear(key: StorageKey) {
        redis.del(keyBuilder.build(key, "state"), keyBuilder.build(key, "data")).await()
    }

    override suspend fun getState(key: StorageKey): StorageState {
        return redis.get(keyBuilder.build(key, "state")).await()
    }

    override suspend fun getData(key: StorageKey): Map<StorageKeyData, String> {
        return redis.hgetall(keyBuilder.build(key, "data")).await()
    }

    override suspend fun getData(key: StorageKey, keyData: StorageKeyData): String? {
        return redis.hget(keyBuilder.build(key, "data"), keyData).await()
    }

    @OptIn(InternalSerializationApi::class)
    override suspend fun <V : Any> getTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V? {
        val value = redis.hget(keyBuilder.build(key, "data"), keyData).await() ?: return null
        return json.decodeFromString(clazz.serializer(), value)
    }
}