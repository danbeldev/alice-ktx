package com.github.alice.ktx.fsm

import com.github.alice.ktx.fsm.models.FSMStrategy
import kotlin.reflect.KClass

interface ReadOnlyFSMContext {

    /**
     * Получить текущее состояние.
     * */
    suspend fun getState(): String?

    suspend fun getState(strategy: FSMStrategy): String?

    /**
     * Получить все данные.
     * */
    suspend fun getData(): Map<String, String>

    suspend fun getData(strategy: FSMStrategy): Map<String, String>

    /**
     * Получить данные по ключу с произвольным типом.
     * */
    suspend fun <V : Any> getTypedData(key: String, clazz: KClass<V>): V?

    suspend fun <V : Any> getTypedData(key: String, clazz: KClass<V>, strategy: FSMStrategy): V?

    /**
     * Получить данные по ключу.
     * */
    suspend fun getData(key: String): String?

    suspend fun getData(key: String, strategy: FSMStrategy): String?
}