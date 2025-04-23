package com.github.alice.ktx.webhook.impl

import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.common.serializer.impl.defaultJson
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.webhook.WebhookServer
import com.github.alice.ktx.webhook.WebhookServerListener
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

/**
 * Создает экземпляр `ktorWebhookServer` с заданной конфигурацией.
 *
 * @param body Функция, принимающая объект `KtorWebhookServer.Builder` и выполняющая настройку.
 * Эта функция будет вызвана в контексте `KtorWebhookServer.Builder`.
 * @return Настроенный объект `KtorWebhookServer`.
 */
@AliceDsl
fun ktorWebhookServer(body: KtorWebhookServer.Builder.() -> Unit): KtorWebhookServer {
    return KtorWebhookServer.Builder().json(defaultJson).build(body)
}

/**
 * Класс `KtorWebhookServer` представляет собой веб-сервер, работающий на основе Ktor.
 *
 * @property port Порт, на котором будет запущен веб-сервер.
 * @property path Путь, по которому будет доступен веб-сервер.
 * @property json Конфигурация для обработки JSON данных.
 * @property configuration Конфигурация для приложения Ktor.
 */
class KtorWebhookServer internal constructor(
    private val port: Int,
    private val path: String,
    private val host: String,
    private val json: Json,
    private val configuration: Application.() -> Unit
): WebhookServer {

    /**
     * Класс `Builder` предназначен для настройки и создания экземпляра `KtorWebhookServer`.
     */
    @AliceDsl
    class Builder {
        var port: Int = 8080
        var path: String = "/"
        var host: String = "0.0.0.0"
        lateinit var json: Json
        var configuration: Application.() -> Unit = {}

        internal fun json(json: Json): Builder {
            this.json = json
            return this
        }

        fun build(body: Builder.() -> Unit): KtorWebhookServer {
            body.invoke(this)
            return KtorWebhookServer(
                port = port,
                path = path,
                host = host,
                json = json,
                configuration = configuration
            )
        }
    }

    override fun run(listener: WebhookServerListener) {
        embeddedServer(Netty, port = port, host = host) {

            install(ContentNegotiation) {
                json(json)
            }

            configuration()

            routing {
                post(path) {
                    val model = call.receive<MessageRequest>()
                    try {
                        listener.handleRequest(model)?.let { response ->
                            call.respond(response)
                        }
                    }catch (exception: Exception) {
                        listener.handleError(model, exception)?.let { response ->
                            call.respond(response)
                            return@post
                        }
                        throw exception
                    }
                }
            }
        }.start(wait = true)
    }
}