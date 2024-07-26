package com.github.alice

import com.github.alice.handlers.Handler
import com.github.alice.handlers.error.NetworkErrorHandler
import com.github.alice.middleware.Middleware
import com.github.alice.middleware.MiddlewareType
import com.github.alice.models.FSMStrategy

fun Skill.Builder.dispatch(body: Dispatcher.() -> Unit) {
    dispatcherConfiguration = body
}

class Dispatcher internal constructor(
    internal val fsmStrategy: FSMStrategy
) {
    internal val commandHandlers = linkedSetOf<Handler>()
    internal val networkErrorHandlers = linkedSetOf<NetworkErrorHandler>()
    internal val middlewares = linkedMapOf<MiddlewareType, MutableList<Middleware>>()

    init {
        MiddlewareType.entries.forEach { middlewares[it] = mutableListOf() }
    }

    fun addHandler(handler: Handler) {
        commandHandlers.add(handler)
    }

    fun removeHandler(handler: Handler) {
        commandHandlers.remove(handler)
    }

    fun addMiddleware(middleware: Middleware, type: MiddlewareType) {
        middlewares[type]?.add(middleware)
    }

    fun addNetworkErrorHandler(handler: NetworkErrorHandler) {
        networkErrorHandlers.add(handler)
    }
}
