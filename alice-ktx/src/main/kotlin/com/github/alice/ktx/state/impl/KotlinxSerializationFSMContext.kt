package com.github.alice.ktx.state.impl

import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.state.FSMContext
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

/**
 * Класс `KotlinxSerializationFSMContext` предоставляет реализацию интерфейса `FSMContext` для управления состоянием и данными пользователя.
 *
 * @param message Сообщение запроса, содержащего начальное состояние и данные пользователя.
 * @param strategy Стратегия конечного автомата состояний (FSM), используемая для управления состояниями.
 */
class KotlinxSerializationFSMContext(
    message: MessageRequest,
    private val strategy: FSMStrategy,
    private val json: Json
) : FSMContext {

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

    @OptIn(InternalSerializationApi::class)
    override fun <V : Any> getTypedData(key: String, clazz: KClass<V>): V? {
        val value = data[key] ?: return null
        return json.decodeFromString(clazz.serializer(), value)
    }

    @OptIn(InternalSerializationApi::class)
    override fun <V: Any> setTypedData(vararg pairs: Pair<String, V>, clazz: KClass<V>) {
        setData(
            *pairs.map { it.first to json.encodeToString(clazz.serializer(), it.second) }.toTypedArray()
        )
    }

    override fun setData(vararg pairs: Pair<String, String>) {
        data.clear()
        data.putAll(pairs)
    }

    @OptIn(InternalSerializationApi::class)
    override fun <V : Any> updateTypedData(vararg pairs: Pair<String, V>, clazz: KClass<V>): Map<String, String> {
        return updateData(
            *pairs.map { it.first to json.encodeToString(clazz.serializer(), it.second) }.toTypedArray()
        )
    }

    override fun updateData(vararg pairs: Pair<String, String>): Map<String, String> {
        data.putAll(pairs)
        return data.toMap()
    }

    override fun removeData(key: String): String? {
        return data.remove(key)
    }

    @OptIn(InternalSerializationApi::class)
    override fun <V : Any> removeTypedData(key: String, clazz: KClass<V>): V? {
        val value = data[key] ?: return null
        data.remove(key)
        return json.decodeFromString(clazz.serializer(), value)
    }

    override fun clear() {
        currentState = null
        data.clear()
    }
}