package com.github.alice.ktx.state

import com.github.alice.ktx.models.FSMStrategy
import kotlin.reflect.KClass

interface ReadOnlyFSMContext {

    /**
     * Получить стратегию FSM.
     * */
    fun getStrategy(): FSMStrategy

    /**
     * Получить текущее состояние.
     * */
    fun getState(): String?

    /**
     * Получить все данные.
     * */
    fun getData(): Map<String, String>

    /**
     * Получить данные по ключу с произвольным типом.
     * */
    fun <V: Any> getTypedData(key: String, clazz: KClass<V>): V?

    /**
     * Получить данные по ключу.
     * */
    fun getData(key: String): String?
}