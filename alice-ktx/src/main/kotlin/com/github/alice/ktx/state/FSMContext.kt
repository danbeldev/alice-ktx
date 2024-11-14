package com.github.alice.ktx.state

/**
 * Интерфейс для доступа к информации состояния конкретного пользователя.
 * Создаётся и передаётся в обработчики при каждом событии.
 * */
interface FSMContext: MutableFSMContext {

    suspend fun init()
}