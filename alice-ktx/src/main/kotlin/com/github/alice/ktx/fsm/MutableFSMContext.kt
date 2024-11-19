package com.github.alice.ktx.fsm

import com.github.alice.ktx.fsm.models.FSMStrategy
import kotlin.reflect.KClass

interface MutableFSMContext : ReadOnlyFSMContext {

    /**
     * Установить состояние по ключу.
     * */
    suspend fun setState(key: String)

    suspend fun setState(key: String, strategy: FSMStrategy)

    /**
     * Записать данные (перезапись) с произвольным типом.
     * */
    suspend fun <V : Any> setTypedData(clazz: KClass<V>, vararg pairs: Pair<String, V>)

    suspend fun <V : Any> setTypedData(clazz: KClass<V>, strategy: FSMStrategy, vararg pairs: Pair<String, V>)

    /**
     * Записать данные (перезапись).
     * */
    suspend fun setData(vararg pairs: Pair<String, String>)

    suspend fun setData(strategy: FSMStrategy, vararg pairs: Pair<String, String>)

    /**
     * Обновление данных в хранилище по ключу с произвольным типом.
     * @return Обновленные данные.
     * */
    suspend fun <V : Any> updateTypedData(clazz: KClass<V>, vararg pairs: Pair<String, V>): Map<String, String>

    suspend fun <V : Any> updateTypedData(
        clazz: KClass<V>,
        strategy: FSMStrategy,
        vararg pairs: Pair<String, V>
    ): Map<String, String>

    /**
     * Обновление данные в хранилище по ключу.
     * @return Обновленные данные.
     * */
    suspend fun updateData(vararg pairs: Pair<String, String>): Map<String, String>

    suspend fun updateData(strategy: FSMStrategy, vararg pairs: Pair<String, String>): Map<String, String>

    /**
     * Удалить данные по ключу.
     * */
    suspend fun removeData(key: String): String?

    suspend fun removeData(key: String, strategy: FSMStrategy): String?

    /**
     * Удалить данные по ключу с произвольным типом.
     * */
    suspend fun <V : Any> removeTypedData(key: String, clazz: KClass<V>): V?

    suspend fun <V : Any> removeTypedData(key: String, clazz: KClass<V>, strategy: FSMStrategy): V?

    /**
     * Очистить состояние и данные.
     * */
    suspend fun clear()

    suspend fun clear(strategy: FSMStrategy)
}