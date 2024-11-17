package com.github.alice.ktx.storage.apiStorage

import com.github.alice.ktx.storage.models.StorageKey
import com.github.alice.ktx.storage.models.StorageKeyData
import com.github.alice.ktx.storage.models.StorageState

/**
 * Интерфейс, обеспечивающий синхронизацию состояния и данных с хранилищем Алисы для реализации [com.github.alice.ktx.storage.Storage].
 *
 * Реализации [com.github.alice.ktx.storage.Storage], которые дополнительно реализуют интерфейс `ApiStorageDetails`, могут автоматически
 * получать актуальные данные и состояние из хранилища Алисы при каждом создании запроса.
 *
 * - Метод [setCurrentState] обновляет текущее состояние (`state`) для заданного ключа [StorageKey],
 * полученное из хранилища Алисы.
 * - Метод [setCurrentData] обновляет текущие данные (`data`) для заданного ключа [StorageKey],
 * полученные из хранилища Алисы.
 *
 * Эти методы вызываются при каждом создании нового запроса, обеспечивая синхронизацию данных
 * между локальным хранилищем и хранилищем Алисы.
 */
interface ApiStorageDetails {

    /**
     * Устанавливает текущее состояние, полученное из хранилища Алисы, для заданного ключа.
     *
     * @param key ключ, идентифицирующий сохраняемое состояние.
     * @param state состояние, полученное из хранилища Алисы.
     */
    suspend fun setCurrentState(key: StorageKey, state: StorageState) {}

    /**
     * Устанавливает текущие данные, полученные из хранилища Алисы, для заданного ключа.
     *
     * @param key ключ, идентифицирующий сохраняемые данные.
     * @param data данные, полученные из хранилища Алисы, в виде карты пар (ключ данных - значение).
     */
    suspend fun setCurrentData(key: StorageKey, data: Map<StorageKeyData, String>?) {}
}