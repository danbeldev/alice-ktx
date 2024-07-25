package com.github.alice

import com.github.alice.handlers.Handler

fun Skill.Builder.dispatch(body: Dispatcher.() -> Unit) {
    dispatcherConfiguration = body
}

class Dispatcher internal constructor() {
    internal val commandHandlers = linkedSetOf<Handler>()

    internal fun addHandler(handler: Handler) {
        commandHandlers.add(handler)
    }

    internal fun removeHandler(handler: Handler) {
        commandHandlers.remove(handler)
    }
}
