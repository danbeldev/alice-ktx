package com.github.alice.ktx.state

import kotlin.reflect.KClass

interface ReadOnlyFSMContext {

    /**
     * Получить текущее состояние.
     * */
    suspend fun getState(): String?

    /**
     * Получить все данные.
     * */
    suspend fun getData(): Map<String, String>

    /**
     * Получить данные по ключу с произвольным типом.
     * */
    suspend fun <V: Any> getTypedData(key: String, clazz: KClass<V>): V?

    /**
     * Получить данные по ключу.
     * */
    suspend fun getData(key: String): String?
}