package com.github.alice.ktx

import com.github.alice.ktx.api.dialog.DialogApi
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.error.NetworkErrorHandler
import com.github.alice.ktx.middleware.Middleware
import com.github.alice.ktx.middleware.MiddlewareType
import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest
import com.github.alice.ktx.context.FSMContext

/**
 * Расширение для `Skill.Builder`, позволяющее настроить `Dispatcher`.
 *
 * @param body Функция, принимающая объект `Dispatcher` и позволяющая настроить его.
 * Эта функция будет вызвана в контексте `Dispatcher`.
 */
fun Skill.Builder.dispatch(body: Dispatcher.() -> Unit) {
    dispatcherConfiguration = body
}

/**
 * Класс `Dispatcher` управляет обработчиками команд, обработчиками сетевых ошибок и мидлварами.
 *
 * @param fsmStrategy Стратегия конечного автомата состояний (FSM), используемая для управления состояниями.
 */
class Dispatcher internal constructor(
    internal val fsmStrategy: FSMStrategy,
    val dialogApi: DialogApi? = null,
    internal val fsmContext: (message: MessageRequest) -> FSMContext,
    internal val enableApiStorage: Boolean = false
) {
    internal val commandHandlers = linkedSetOf<Handler>()
    internal val networkErrorHandlers = linkedSetOf<NetworkErrorHandler>()
    internal val middlewares = mutableMapOf<MiddlewareType, LinkedHashSet<Middleware>>()

    init {
        MiddlewareType.entries.forEach { middlewares[it] = linkedSetOf() }
    }

    fun addHandler(handler: Handler) {
        commandHandlers.add(handler)
    }

    fun addMiddleware(middleware: Middleware, type: MiddlewareType) {
        middlewares[type]?.add(middleware)
    }

    fun addNetworkErrorHandler(handler: NetworkErrorHandler) {
        networkErrorHandlers.add(handler)
    }
}
