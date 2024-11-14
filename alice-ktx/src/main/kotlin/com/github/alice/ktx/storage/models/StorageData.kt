package com.github.alice.ktx.storage.models

typealias StorageKeyData = String
typealias StorageData = Pair<StorageKeyData, String>
typealias StorageTypedData<V> = Pair<StorageKeyData, V>