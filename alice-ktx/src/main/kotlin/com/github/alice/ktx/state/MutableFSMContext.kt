package com.github.alice.ktx.state

import kotlin.reflect.KClass

interface MutableFSMContext : ReadOnlyFSMContext {

    /**
     * Установить состояние по ключу.
     * */
    suspend fun setState(key: String)

    /**
     * Записать данные (перезапись) с произвольным типом.
     * */
    suspend fun <V: Any> setTypedData(vararg pairs: Pair<String, V>, clazz: KClass<V>)

    /**
     * Записать данные (перезапись).
     * */
    suspend fun setData(vararg pairs: Pair<String, String>)

    /**
     * Обновление данных в хранилище по ключу с произвольным типом.
     * @return Обновленные данные.
     * */
    suspend fun <V: Any> updateTypedData(vararg pairs: Pair<String, V>, clazz: KClass<V>): Map<String, String>

    /**
     * Обновление данные в хранилище по ключу.
     * @return Обновленные данные.
     * */
    suspend fun updateData(vararg pairs: Pair<String, String>): Map<String, String>

    /**
     * Удалить данные по ключу.
     * */
    suspend fun removeData(key: String): String?

    /**
     * Удалить данные по ключу с произвольным типом.
     * */
    suspend fun <V: Any> removeTypedData(key: String, clazz: KClass<V>): V?

    /**
     * Очистить состояние и данные.
     * */
    suspend fun clear()
}