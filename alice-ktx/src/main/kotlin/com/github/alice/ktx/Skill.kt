package com.github.alice.ktx

import com.github.alice.ktx.middleware.MiddlewareType
import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.models.response.MessageResponse
import com.github.alice.ktx.server.WebServer
import com.github.alice.ktx.server.WebServerResponseCallback

fun skill(body: Skill.Builder.() -> Unit): Skill = Skill.Builder().build(body)

class Skill internal constructor(
    private val webServer: WebServer,
    private val dispatcher: Dispatcher
) {
    class Builder {
        lateinit var webServer: WebServer
        var fsmStrategy: FSMStrategy = FSMStrategy.USER
        internal var dispatcherConfiguration: Dispatcher.() -> Unit = { }

        fun build(body: Builder.() -> Unit): Skill {
            body()

            val dispatcher = Dispatcher(fsmStrategy).apply(dispatcherConfiguration)

            return Skill(
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
        override suspend fun message(model: MessageRequest): MessageResponse? {
            runMiddlewares(model, MiddlewareType.OUTER)?.let { return it }
            dispatcher.commandHandlers.forEach { handler ->
                if(handler.event(model)) {
                    runMiddlewares(model, MiddlewareType.INNER)?.let { return it }
                    return handler.response(model)
                }
            }
            return null
        }

        override suspend fun responseFailure(model: MessageRequest, throwable: Throwable): MessageResponse? {
            dispatcher.networkErrorHandlers.forEach { errorHandler ->
                errorHandler.responseFailure(model, throwable)?.let { response ->
                    return response
                }
            }
            return null
        }

        private suspend fun runMiddlewares(model: MessageRequest, type: MiddlewareType): MessageResponse? {
            dispatcher.middlewares[type]?.forEach { middleware ->
                middleware.invoke(model)?.let { response ->
                    return response
                }
            }
            return null
        }
    }
}