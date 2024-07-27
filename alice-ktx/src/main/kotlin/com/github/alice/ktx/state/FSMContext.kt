package com.github.alice.ktx.state

import com.github.alice.ktx.models.FSMStrategy

/**
 * Интерфейс для доступа к информации состояния конкретного пользователя.
 * Создаётся и передаётся в обработчики при каждом событии.
 * */
interface FSMContext {

    /**
     * Получить стратегию FSM.
     * */
    fun getStrategy(): FSMStrategy

    /**
     * Получить текущее состояние.
     * */
    fun getState(): String?

    /**
     * Установить состояние по ключу.
     * */
    fun setState(key: String)

    /**
     * Получить все данные.
     * */
    fun getData(): Map<String, String>

    /**
     * Получить данные по ключу.
     * */
    fun getData(key: String): String?

    /**
     * Записать данные (перезапись).
     * */
    fun setData(vararg pairs: Pair<String, String>)

    /**
     * Обновление данные в хранилище по ключу.
     * @return Обновленные данные.
     * */
    fun updateData(vararg pairs: Pair<String, String>): Map<String, String>

    /**
     * Очистить состояние и данные.
     * */
    fun clear()
}