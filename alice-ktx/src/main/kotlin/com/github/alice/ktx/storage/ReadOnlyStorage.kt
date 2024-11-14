package com.github.alice.ktx.storage

import com.github.alice.ktx.storage.models.StorageState
import com.github.alice.ktx.storage.models.StorageKey
import com.github.alice.ktx.storage.models.StorageKeyData
import kotlin.reflect.KClass

interface ReadOnlyStorage {

    /**
     * Получить текущее состояние.
     * */
    suspend fun getState(key: StorageKey): StorageState

    /**
     * Получить все данные.
     * */
    suspend fun getData(key: StorageKey): Map<StorageKeyData, String>

    /**
     * Получить данные по ключу с произвольным типом.
     * */
    suspend fun <V : Any> getTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V?

    /**
     * Получить данные по ключу.
     * */
    suspend fun getData(key: StorageKey, keyData: StorageKeyData): String?
}