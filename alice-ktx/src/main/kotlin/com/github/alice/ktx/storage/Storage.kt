package com.github.alice.ktx.storage


interface Storage : MutableStorage {

    /**
     * Закрыть хранилище (подключение к бд, файлу итп.)
     * */
    suspend fun close()
}