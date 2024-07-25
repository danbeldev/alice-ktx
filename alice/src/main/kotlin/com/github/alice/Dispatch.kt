package com.github.alice

import com.github.alice.handlers.Handler
import com.github.alice.middleware.Middleware
import com.github.alice.middleware.MiddlewareType

fun Skill.Builder.dispatch(body: Dispatcher.() -> Unit) {
    dispatcherConfiguration = body
}

class Dispatcher internal constructor() {
    internal val commandHandlers = linkedSetOf<Handler>()
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
}
