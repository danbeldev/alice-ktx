package com.github.alice.ktx.common

import io.lettuce.core.RedisClient
import io.lettuce.core.api.StatefulRedisConnection

/**
 * Формирует строку URI для подключения к Redis на основе заданных параметров.
 *
 * @param host Хост Redis-сервера. По умолчанию "localhost".
 * @param port Порт Redis-сервера. По умолчанию 6379.
 * @param username Необязательное имя пользователя для аутентификации. Если null, имя пользователя не указывается.
 * @param password Необязательный пароль для аутентификации. Если null, пароль не указывается.
 * @return Строка URI для подключения к Redis.
 *
 * Примеры:
 * - Если указаны только `host` и `port`: "redis://localhost:6379"
 * - Если указан `password` без `username`: "redis://:password@localhost:6379"
 * - Если указаны и `username`, и `password`: "redis://username:password@localhost:6379"
 */
fun redisUri(
    host: String = "localhost",
    port: Int = 6379,
    username: String? = null,
    password: String? = null
): String {
    return if (password != null) {
        if (username != null) {
            "redis://$username:$password@$host:$port"
        } else {
            "redis://:$password@$host:$port"
        }
    } else {
        "redis://$host:$port"
    }
}

/**
 * Устанавливает соединение с Redis-сервером, используя параметры подключения.
 *
 * Этот метод создает подключение к Redis с использованием клиента, полученного
 * из URI, сформированного на основе переданных параметров.
 *
 * @param host Хост Redis-сервера. По умолчанию "localhost".
 * @param port Порт Redis-сервера. По умолчанию 6379.
 * @param username Необязательное имя пользователя для аутентификации. Если null, имя пользователя не указывается.
 * @param password Необязательный пароль для аутентификации. Если null, пароль не указывается.
 * @return Объект StatefulRedisConnection, представляющий подключение к Redis.
 */
fun connectToRedis(
    host: String = "localhost",
    port: Int = 6379,
    username: String? = null,
    password: String? = null
): StatefulRedisConnection<String, String> {
    val redisClient = RedisClient.create(redisUri(host, port, username, password))
    return redisClient.connect()
}