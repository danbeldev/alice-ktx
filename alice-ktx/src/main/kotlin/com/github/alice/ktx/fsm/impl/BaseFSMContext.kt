package com.github.alice.ktx.fsm.impl

import com.github.alice.ktx.fsm.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.fsm.FSMContext
import com.github.alice.ktx.storage.Storage
import com.github.alice.ktx.storage.apiStorage.ApiStorageDetails
import com.github.alice.ktx.storage.models.StorageKey
import kotlin.reflect.KClass

/**
 * Класс `BaseFSMContext` предоставляет реализацию интерфейса `FSMContext` для управления состоянием и данными пользователя.
 *
 * @param message Сообщение запроса, содержащего начальное состояние и данные пользователя.
 * @param defaultStrategy Стратегия конечного автомата состояний (FSM), используемая для управления состояниями.
 */
class BaseFSMContext internal constructor(
    private val storage: Storage,
    private val defaultStrategy: FSMStrategy,
    private val message: MessageRequest,
    skillId: String?
) : FSMContext {

    private val storageKey = StorageKey.Builder().build(skillId, message, defaultStrategy)

    override suspend fun init() {
        (storage as? ApiStorageDetails)?.apply {
            FSMStrategy.entries.forEach { strategy ->
                message.state?.getCurrentStateAndData(strategy)?.let { stateAndData ->
                    setCurrentState(storageKey.copy(strategy = strategy), stateAndData.first)
                    setCurrentData(storageKey.copy(strategy = strategy), stateAndData.second)
                }
            }
        }
    }

    override suspend fun setState(key: String) {
        storage.setState(storageKey, key)
    }

    override suspend fun setState(key: String, strategy: FSMStrategy) {
        storage.setState(storageKey.copy(strategy = strategy), key)
    }

    override suspend fun <V : Any> setTypedData(clazz: KClass<V>, vararg pairs: Pair<String, V>) {
        storage.setTypedData(storageKey, clazz, *pairs)
    }

    override suspend fun <V : Any> setTypedData(
        clazz: KClass<V>,
        strategy: FSMStrategy,
        vararg pairs: Pair<String, V>
    ) {
        storage.setTypedData(storageKey.copy(strategy = strategy), clazz, *pairs)
    }

    override suspend fun setData(vararg pairs: Pair<String, String>) {
        storage.setData(storageKey, *pairs)
    }

    override suspend fun setData(strategy: FSMStrategy, vararg pairs: Pair<String, String>) {
        storage.setData(storageKey.copy(strategy = strategy), *pairs)
    }

    override suspend fun <V : Any> updateTypedData(
        clazz: KClass<V>,
        vararg pairs: Pair<String, V>
    ): Map<String, String> {
        return storage.updateTypedData(storageKey, clazz, *pairs)
    }

    override suspend fun <V : Any> updateTypedData(
        clazz: KClass<V>,
        strategy: FSMStrategy,
        vararg pairs: Pair<String, V>
    ): Map<String, String> {
        return storage.updateTypedData(storageKey.copy(strategy = strategy), clazz, *pairs)
    }

    override suspend fun updateData(vararg pairs: Pair<String, String>): Map<String, String> {
        return storage.updateData(storageKey, *pairs)
    }

    override suspend fun updateData(strategy: FSMStrategy, vararg pairs: Pair<String, String>): Map<String, String> {
        return storage.updateData(storageKey.copy(strategy = strategy), *pairs)
    }

    override suspend fun removeData(key: String): String? {
        return storage.removeData(storageKey, key)
    }

    override suspend fun removeData(key: String, strategy: FSMStrategy): String? {
        return storage.removeData(storageKey.copy(strategy = strategy), key)
    }

    override suspend fun <V : Any> removeTypedData(key: String, clazz: KClass<V>): V? {
        return storage.removeTypedData(storageKey, key, clazz)
    }

    override suspend fun <V : Any> removeTypedData(key: String, clazz: KClass<V>, strategy: FSMStrategy): V? {
        return storage.removeTypedData(storageKey.copy(strategy = strategy), key, clazz)
    }

    override suspend fun clear() {
        storage.clear(storageKey)
    }

    override suspend fun clear(strategy: FSMStrategy) {
        storage.clear(storageKey.copy(strategy = strategy))
    }

    override suspend fun getState(): String? {
        return storage.getState(storageKey)
    }

    override suspend fun getState(strategy: FSMStrategy): String? {
        return storage.getState(storageKey.copy(strategy = strategy))
    }

    override suspend fun getData(): Map<String, String> {
        return storage.getData(storageKey)
    }

    override suspend fun getData(strategy: FSMStrategy): Map<String, String> {
        return storage.getData(storageKey.copy(strategy = strategy))
    }

    override suspend fun getData(key: String): String? {
        return storage.getData(storageKey, key)
    }

    override suspend fun getData(key: String, strategy: FSMStrategy): String? {
        return storage.getData(storageKey.copy(strategy = strategy), key)
    }

    override suspend fun <V : Any> getTypedData(key: String, clazz: KClass<V>): V? {
        return storage.getTypedData(storageKey, key, clazz)
    }

    override suspend fun <V : Any> getTypedData(key: String, clazz: KClass<V>, strategy: FSMStrategy): V? {
        return storage.getTypedData(storageKey.copy(strategy = strategy), key, clazz)
    }
}