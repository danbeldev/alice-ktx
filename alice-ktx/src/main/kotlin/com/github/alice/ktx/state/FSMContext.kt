package com.github.alice.ktx.state

import com.github.alice.ktx.models.FSMStrategy

/**
 * Интерфейс для доступа к информации состояния конкретного пользователя.
 * Создаётся и передаётся в обработчики при каждом событии.
 * */
interface FSMContext {

    fun getStrategy(): FSMStrategy

    fun getState(): String?

    fun setState(name: String)

    fun getData(): Map<String, String>

    fun getData(key: String): String?

    fun setData(vararg pairs: Pair<String, String>)

    fun updateData(vararg pairs: Pair<String, String>)

    fun clear()
}