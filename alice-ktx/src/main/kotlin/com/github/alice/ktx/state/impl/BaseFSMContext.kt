package com.github.alice.ktx.state.impl

import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.state.FSMContext
import com.github.alice.ktx.storage.Storage
import com.github.alice.ktx.storage.apiStorage.ApiStorageDetails
import com.github.alice.ktx.storage.models.StorageKey
import kotlin.reflect.KClass

/**
 * Класс `BaseFSMContext` предоставляет реализацию интерфейса `FSMContext` для управления состоянием и данными пользователя.
 *
 * @param message Сообщение запроса, содержащего начальное состояние и данные пользователя.
 * @param strategy Стратегия конечного автомата состояний (FSM), используемая для управления состояниями.
 */
class BaseFSMContext(
    private val storage: Storage,

    skillId: String?,
    private val strategy: FSMStrategy,
    private val message: MessageRequest,
) : FSMContext {

    private val storageKey = StorageKey.Builder().build(skillId, message, strategy)

    override suspend fun init() {
        (storage as? ApiStorageDetails)?.apply {
            message.state?.getCurrentStateAndData(strategy)?.let { stateAndData ->
                setCurrentState(storageKey, stateAndData.first)
                setCurrentData(storageKey, stateAndData.second)
            }
        }
    }

    override suspend fun setState(key: String) {
        storage.setState(storageKey, key)
    }

    override suspend fun <V : Any> setTypedData(vararg pairs: Pair<String, V>, clazz: KClass<V>) {
        storage.setTypedData(storageKey, clazz, *pairs)
    }

    override suspend fun setData(vararg pairs: Pair<String, String>) {
       storage.setData(storageKey, *pairs)
    }

    override suspend fun <V : Any> updateTypedData(vararg pairs: Pair<String, V>, clazz: KClass<V>): Map<String, String> {
        return storage.updateTypedData(storageKey, clazz, *pairs)
    }

    override suspend fun updateData(vararg pairs: Pair<String, String>): Map<String, String> {
        return storage.updateData(storageKey, *pairs)
    }

    override suspend fun removeData(key: String): String? {
        return storage.removeData(storageKey, key)
    }

    override suspend fun <V : Any> removeTypedData(key: String, clazz: KClass<V>): V? {
        return storage.removeTypedData(storageKey, key, clazz)
    }

    override suspend fun clear() {
        storage.clear(storageKey)
    }

    override suspend fun getState(): String? {
       return storage.getState(storageKey)
    }

    override suspend fun getData(): Map<String, String> {
        return storage.getData(storageKey)
    }

    override suspend fun getData(key: String): String? {
        return storage.getData(storageKey, key)
    }

    override suspend fun <V : Any> getTypedData(key: String, clazz: KClass<V>): V? {
        return storage.getTypedData(storageKey, key, clazz)
    }
}