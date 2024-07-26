package com.github.alice.ktx.server.impl

import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.server.WebServer
import com.github.alice.ktx.server.WebServerResponseCallback
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json

fun ktorWebServer(body: KtorWebServer.Builder.() -> Unit): KtorWebServer = KtorWebServer.Builder().build(body)

class KtorWebServer(
    private val port: Int,
    private val path: String,
    private val json: Json,
    var configuration: Application.() -> Unit
): WebServer {

    class Builder {
        var port: Int = 8080
        var path: String = "/"
        var json: Json = Json {
            ignoreUnknownKeys = true
        }
        var configuration: Application.() -> Unit = {}

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

    override fun run(callback: WebServerResponseCallback) {
        embeddedServer(Netty, port = port) {

            install(ContentNegotiation) {
                json(json)
            }

            configuration()

            routing {
                post(path) {
                    val model = call.receive<MessageRequest>()
                    try {
                        callback.message(model)?.let { response ->
                            call.respond(response)
                        }
                    }catch (e: Throwable) {
                        callback.responseFailure(model, e)?.let { response ->
                            call.respond(response)
                        }
                    }
                }
            }
        }.start(wait = true)
    }
}