package com.github.alice

import com.github.alice.models.request.MessageRequest
import com.github.alice.models.response.MessageResponse
import com.github.alice.server.WebServer
import com.github.alice.server.WebServerResponseCallback

fun skill(body: Skill.Builder.() -> Unit): Skill = Skill.Builder().build(body)

class Skill internal constructor(
    private val id: String,
    private val webServer: WebServer,
    private val dispatcher: Dispatcher
) {
    class Builder {
        lateinit var id: String
        lateinit var webServer: WebServer
        internal var dispatcherConfiguration: Dispatcher.() -> Unit = { }

        fun build(body: Builder.() -> Unit): Skill {
            body()

            val dispatcher = Dispatcher().apply(dispatcherConfiguration)

            return Skill(
                id = id,
                webServer = webServer,
                dispatcher = dispatcher
            )
        }
    }

    fun run() {
        val webServerCallback = webServerResponseCallback()
        webServer.run(webServerCallback)
    }

    private fun webServerResponseCallback(): WebServerResponseCallback = object : WebServerResponseCallback {
        override fun message(model: MessageRequest): MessageResponse? {
            dispatcher.commandHandlers.forEach { handler ->
                if(handler.event(model)) return handler.response(model)
            }
            return null
        }
    }
}