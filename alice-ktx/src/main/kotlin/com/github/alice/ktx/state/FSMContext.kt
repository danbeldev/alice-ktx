package com.github.alice.ktx.state

import kotlin.reflect.KClass

/**
 * Интерфейс для доступа к информации состояния конкретного пользователя.
 * Создаётся и передаётся в обработчики при каждом событии.
 * */
interface FSMContext: ReadOnlyFSMContext {

    /**
     * Установить состояние по ключу.
     * */
    fun setState(key: String)

    /**
     * Записать данные (перезапись) с произвольным типом.
     * */
    fun <V: Any> setTypedData(vararg pairs: Pair<String, V>, clazz: KClass<V>)

    /**
     * Записать данные (перезапись).
     * */
    fun setData(vararg pairs: Pair<String, String>)

    /**
     * Обновление данных в хранилище по ключу с произвольным типом.
     * @return Обновленные данные.
     * */
    fun <V: Any> updateTypedData(vararg pairs: Pair<String, V>, clazz: KClass<V>): Map<String, String>

    /**
     * Обновление данные в хранилище по ключу.
     * @return Обновленные данные.
     * */
    fun updateData(vararg pairs: Pair<String, String>): Map<String, String>

    /**
     * Удалить данные по ключу.
     * */
    fun removeData(key: String): String?

    /**
     * Удалить данные по ключу с произвольным типом.
     * */
    fun <V: Any> removeTypedData(key: String, clazz: KClass<V>): V?

    /**
     * Очистить состояние и данные.
     * */
    fun clear()
}