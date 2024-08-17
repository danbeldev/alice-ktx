package com.github.alice.ktx.server.impl

import com.github.alice.ktx.Skill
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.server.WebServer
import com.github.alice.ktx.server.WebServerResponseListener
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
 * Создает экземпляр `KtorWebServer` с заданной конфигурацией.
 *
 * @param body Функция, принимающая объект `KtorWebServer.Builder` и выполняющая настройку.
 * Эта функция будет вызвана в контексте `KtorWebServer.Builder`.
 * @return Настроенный объект `KtorWebServer`.
 */
fun Skill.Builder.ktorWebServer(body: KtorWebServer.Builder.() -> Unit): KtorWebServer {
    return KtorWebServer.Builder().setJson(json).build(body)
}

/**
 * Класс `KtorWebServer` представляет собой веб-сервер, работающий на основе Ktor.
 *
 * @property port Порт, на котором будет запущен веб-сервер.
 * @property path Путь, по которому будет доступен веб-сервер.
 * @property json Конфигурация для обработки JSON данных.
 * @property configuration Конфигурация для приложения Ktor.
 */
class KtorWebServer(
    private val port: Int,
    private val path: String,
    private val json: Json,
    private val configuration: Application.() -> Unit
): WebServer {

    /**
     * Класс `Builder` предназначен для настройки и создания экземпляра `KtorWebServer`.
     */
    class Builder {
        var port: Int = 8080
        var path: String = "/"
        private lateinit var json: Json
        var configuration: Application.() -> Unit = {}

        internal fun setJson(json: Json): Builder {
            this.json = json
            return this
        }

        fun build(body: Builder.() -> Unit): KtorWebServer {
            body.invoke(this)
            return KtorWebServer(
                port = port,
                path = path,
                json = json,
                configuration = configuration
            )
        }
    }

    override fun run(listener: WebServerResponseListener) {
        embeddedServer(Netty, port = port) {

            install(ContentNegotiation) {
                json(json)
            }

            configuration()

            routing {
                post(path) {
                    val model = call.receive<MessageRequest>()
                    try {
                        listener.messageHandle(model)?.let { response ->
                            call.respond(response)
                        }
                    }catch (ex: Exception) {
                        listener.responseFailure(model, ex)?.let { response ->
                            call.respond(response)
                            return@post
                        }
                        throw ex
                    }
                }
            }
        }.start(wait = true)
    }
}