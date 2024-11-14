package com.github.alice.ktx.storage.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.storage.apiStorage.ApiStorageDetails
import com.github.alice.ktx.storage.apiStorage.EnableApiStorage
import com.github.alice.ktx.storage.models.StorageKey
import com.github.alice.ktx.storage.models.StorageKeyData
import com.github.alice.ktx.storage.models.StorageState
import kotlinx.serialization.json.Json

fun Skill.Builder.apiStorage(body: BaseApiStorage.Builder.() -> Unit): BaseApiStorage {
    return BaseApiStorage.Builder().setJson(json).build(body)
}

@EnableApiStorage
class BaseApiStorage(
    json: Json
) : ApiStorageDetails, MemoryStorage(json) {

    class Builder {

        private lateinit var json: Json

        fun setJson(json: Json): Builder {
            this.json = json
            return this
        }

        fun build(body: Builder.() -> Unit): BaseApiStorage {
            body()
            return BaseApiStorage(json = json)
        }
    }

    override suspend fun setCurrentState(key: StorageKey, state: StorageState) {
        setState(key, state)
    }

    override suspend fun setCurrentData(key: StorageKey, data: Map<StorageKeyData, String>?) {
        setData(key, *(data?.toList() ?: emptyList()).toTypedArray())
    }
}