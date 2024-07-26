package com.github.alice.state

import com.github.alice.models.FSMStrategy
import com.github.alice.models.request.MessageRequest

class FSMContextImpl(
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

    override fun setState(name: String) {
        currentState = name
    }

    override fun getData(): Map<String, String> = data

    override fun getData(key: String): String? = data[key]

    override fun setData(vararg pairs: Pair<String, String>) {
        data.clear()
        data.putAll(pairs)
    }

    override fun updateData(vararg pairs: Pair<String, String>) {
        data.putAll(pairs)
    }

    override fun clear() {
        currentState = null
        data.clear()
    }
}