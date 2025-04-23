package com.github.alice.ktx.storage.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.common.serializer.Serializer
import com.github.alice.ktx.storage.Storage
import com.github.alice.ktx.storage.impl.RedisStorage.Builder
import com.github.alice.ktx.storage.key.KeyBuilder
import com.github.alice.ktx.storage.key.impl.baseKeyBuilder
import com.github.alice.ktx.storage.models.*
import io.lettuce.core.api.StatefulRedisConnection
import io.lettuce.core.api.async.RedisAsyncCommands
import kotlinx.coroutines.future.await
import kotlin.reflect.KClass

@AliceDsl
fun Skill.Builder.redisStorage(body: Builder.() -> Unit): RedisStorage {
    return Builder().serializer(serializer).build(body)
}

class RedisStorage internal constructor(
    private val redis: RedisAsyncCommands<String, String>,
    private val keyBuilder: KeyBuilder,
    private val serializer: Serializer,
    private val stateTtl: Long? = null,
    private val dataTtl: Long? = null
) : Storage {

    @AliceDsl
    class Builder {

        lateinit var redis: StatefulRedisConnection<String, String>
        private lateinit var serializer: Serializer

        var keyBuilder: KeyBuilder = baseKeyBuilder()

        var stateTtl: Long? = null
        var dataTtl: Long? = null

        fun serializer(serializer: Serializer): Builder {
            this.serializer = serializer
            return this
        }

        fun build(body: Builder.() -> Unit): RedisStorage {
            body()

            return RedisStorage(
                redis = redis.async(),
                keyBuilder = keyBuilder,
                serializer = serializer,
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

    override suspend fun <V : Any> setTypedData(key: StorageKey, clazz: KClass<V>, vararg pairs: StorageTypedData<V>) {
        val storageKey = keyBuilder.build(key, "data")
        redis.hset(storageKey, pairs.toMap().mapValues { serializer.serialize(it.value, clazz) }).await()
        dataTtl?.let { redis.expire(storageKey, it).await() }
    }

    override suspend fun setData(key: StorageKey, vararg pairs: StorageData) {
        val storageKey = keyBuilder.build(key, "data")
        redis.hset(storageKey, pairs.toMap()).await()
        dataTtl?.let { redis.expire(storageKey, it).await() }
    }

    override suspend fun <V : Any> updateTypedData(
        key: StorageKey,
        clazz: KClass<V>,
        vararg pairs: StorageTypedData<V>
    ): Map<StorageKeyData, String> {
        val result = mutableMapOf<StorageKeyData, String>()
        val storageKey = keyBuilder.build(key, "data")
        pairs.forEach {
            val value = serializer.serialize(it.second, clazz)
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

    override suspend fun <V : Any> removeTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V? {
        val storageKey = keyBuilder.build(key, "data")
        val value = redis.hget(storageKey, keyData).await() ?: return null
        redis.hdel(storageKey, keyData).await()
        return serializer.deserialize(value, clazz)
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

    override suspend fun <V : Any> getTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V? {
        val value = redis.hget(keyBuilder.build(key, "data"), keyData).await() ?: return null
        return serializer.deserialize(value, clazz)
    }
}