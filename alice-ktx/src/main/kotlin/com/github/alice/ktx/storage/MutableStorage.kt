package com.github.alice.ktx.storage

import com.github.alice.ktx.storage.models.*
import kotlin.reflect.KClass

interface MutableStorage : ReadOnlyStorage {

    /**
     * Установить состояние по ключу.
     * */
    suspend fun setState(key: StorageKey, state: StorageState)

    /**
     * Записать данные (перезапись) с произвольным типом.
     * */
    suspend fun <V : Any> setTypedData(key: StorageKey, clazz: KClass<V>, vararg pairs: StorageTypedData<V>)

    /**
     * Записать данные (перезапись).
     * */
    suspend fun setData(key: StorageKey, vararg pairs: StorageData)

    /**
     * Обновление данных в хранилище по ключу с произвольным типом.
     * @return Обновленные данные.
     * */
    suspend fun <V : Any> updateTypedData(
        key: StorageKey,
        clazz: KClass<V>,
        vararg pairs: StorageTypedData<V>
    ): Map<StorageKeyData, String>

    /**
     * Обновление данные в хранилище по ключу.
     * @return Обновленные данные.
     * */
    suspend fun updateData(key: StorageKey, vararg pairs: StorageData): Map<StorageKeyData, String>

    /**
     * Удалить данные по ключу.
     * */
    suspend fun removeData(key: StorageKey, keyData: StorageKeyData): String?

    /**
     * Удалить данные по ключу с произвольным типом.
     * */
    suspend fun <V : Any> removeTypedData(key: StorageKey, keyData: StorageKeyData, clazz: KClass<V>): V?

    /**
     * Очистить состояние и данные.
     * */
    suspend fun clear(key: StorageKey)
}