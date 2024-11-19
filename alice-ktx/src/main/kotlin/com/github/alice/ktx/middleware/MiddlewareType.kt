package com.github.alice.ktx.middleware

/**
 * [OUTER] будут вызываться при каждом входящем событиию
 * [INNER] будут вызываться только при прохождении фильтров
 * */
enum class MiddlewareType {
    OUTER,
    INNER
}