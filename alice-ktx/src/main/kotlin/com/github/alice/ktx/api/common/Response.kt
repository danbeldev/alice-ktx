package com.github.alice.ktx.api.common

sealed interface Response<T>{
    data class Failed<T>(val message: String): Response<T>
    data class Success<T>(val data: T): Response<T>
}