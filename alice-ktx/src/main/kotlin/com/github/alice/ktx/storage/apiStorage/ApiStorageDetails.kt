package com.github.alice.ktx.storage.apiStorage

import com.github.alice.ktx.storage.models.StorageKey
import com.github.alice.ktx.storage.models.StorageKeyData
import com.github.alice.ktx.storage.models.StorageState

interface ApiStorageDetails {

    suspend fun setCurrentState(key: StorageKey, state: StorageState) {}

    suspend fun setCurrentData(key: StorageKey, data: Map<StorageKeyData, String>?) {}
}