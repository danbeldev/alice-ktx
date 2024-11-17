package com.github.alice.ktx.storage.key

import com.github.alice.ktx.storage.models.StorageKey

interface KeyBuilder {

    suspend fun build(key: StorageKey, part: String): String
}