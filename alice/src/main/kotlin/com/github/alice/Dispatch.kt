package com.github.alice

import com.github.alice.handlers.Handler

fun Skill.Builder.dispatch(body: Dispatcher.() -> Unit) {
    dispatcherConfiguration = body
}

class Dispatcher internal constructor() {
    val commandHandlers = linkedSetOf<Handler>()

    fun addHandler(handler: Handler) {
        commandHandlers.add(handler)
    }

    fun removeHandler(handler: Handler) {
        commandHandlers.remove(handler)
    }
}
