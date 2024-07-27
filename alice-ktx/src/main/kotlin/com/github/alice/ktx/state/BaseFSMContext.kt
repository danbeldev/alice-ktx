package com.github.alice.ktx.state

import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest

/**
 * Класс `BaseFSMContext` предоставляет реализацию интерфейса `FSMContext` для управления состоянием и данными конкретного пользователя.
 *
 * @param message Сообщение запроса, содержащего начальное состояние и данные пользователя.
 * @param strategy Стратегия конечного автомата состояний (FSM), используемая для управления состояниями.
 */
class BaseFSMContext(
    message: MessageRequest,
    private val strategy: FSMStrategy
): FSMContext {

    private val data = mutableMapOf<String, String>()
    private var currentState: String? = null

    init {
        message.state?.let { state ->
            val (s, d) = state.getCurrentStateAndData(strategy)
            currentState = s
            d?.let { data.putAll(it) }
        }
    }

    override fun getStrategy(): FSMStrategy = strategy

    override fun getState(): String? = currentState

    override fun setState(key: String) {
        currentState = key
    }

    override fun getData(): Map<String, String> = data.toMap()

    override fun getData(key: String): String? = data[key]

    override fun setData(vararg pairs: Pair<String, String>) {
        data.clear()
        data.putAll(pairs)
    }

    override fun updateData(vararg pairs: Pair<String, String>): Map<String, String> {
        data.putAll(pairs)
        return data.toMap()
    }

    override fun clear() {
        currentState = null
        data.clear()
    }
}