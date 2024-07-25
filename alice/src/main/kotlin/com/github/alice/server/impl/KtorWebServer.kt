package com.github.alice.server.impl

import com.github.alice.models.request.MessageRequest
import com.github.alice.server.WebServer
import com.github.alice.server.WebServerResponseCallback
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
    private val path: String
): WebServer {

    class Builder {
        var port: Int = 8080
        var path: String = "/"

        fun build(body: Builder.() -> Unit): KtorWebServer {
            body.invoke(this)
            return KtorWebServer(
                port = port,
                path = path
            )
        }
    }

    override fun run(callback: WebServerResponseCallback) {
        embeddedServer(Netty, port = port) {

            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }

            routing {
                post(path) {
                    val model = call.receive<MessageRequest>()
                    callback.message(model)?.let { response ->
                        call.respond(response)
                    }
                }
            }
        }.start(wait = true)
    }
}