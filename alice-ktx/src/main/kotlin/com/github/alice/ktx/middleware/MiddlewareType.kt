package com.github.alice.ktx.middleware

/**
 * [OUTER] будут вызываться при каждом входящем событиию
 * [INNER] удут вызываться только при прохождении фильтров
 * */
enum class MiddlewareType {
    OUTER,
    INNER
}