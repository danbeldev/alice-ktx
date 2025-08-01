package com.github.alice.ktx

import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.environments.ProcessRequestEnvironment
import com.github.alice.ktx.handlers.error.NetworkErrorHandler
import com.github.alice.ktx.middleware.Middleware
import com.github.alice.ktx.middleware.MiddlewareType
import com.github.alice.ktx.middleware.PostMiddleware
import com.github.alice.ktx.models.response.MessageResponse

/**
 * Расширение для `Skill.Builder`, позволяющее настроить `Dispatcher`.
 *
 * @param body Функция, принимающая объект `Dispatcher` и позволяющая настроить его.
 * Эта функция будет вызвана в контексте `Dispatcher`.
 */
@AliceDsl
fun Skill.Builder.dispatch(body: Dispatcher.() -> Unit) {
    dispatcherConfiguration = body
}

@AliceDsl
class Dispatcher internal constructor() {
    internal val commandHandlers = linkedSetOf<Handler>()
    internal val networkErrorHandlers = linkedSetOf<NetworkErrorHandler>()
    internal val middlewares = mutableMapOf<MiddlewareType, LinkedHashSet<Middleware>>()
    internal val postMiddlewares = linkedSetOf<PostMiddleware>()

    init {
        MiddlewareType.entries.forEach { middlewares[it] = linkedSetOf() }
    }

    fun addHandler(handler: Handler) {
        commandHandlers.add(handler)
    }

    fun addMiddleware(middleware: Middleware, type: MiddlewareType) {
        middlewares[type]?.add(middleware)
    }

    fun addPostMiddlewares(postMiddleware: PostMiddleware) {
        postMiddlewares.add(postMiddleware)
    }

    fun addNetworkErrorHandler(handler: NetworkErrorHandler) {
        networkErrorHandlers.add(handler)
    }

    internal suspend fun findHandler(environment: ProcessRequestEnvironment): Handler? {
        return commandHandlers.firstOrNull { it.shouldHandle(environment) }
    }

    internal suspend fun resolveErrorToResponse(
        environment: ProcessRequestEnvironment,
        exception: Exception
    ): MessageResponse? {
        networkErrorHandlers.forEach { errorHandler ->
            errorHandler.responseFailure(environment, exception)?.let { response ->
                return response
            }
        }
        return null
    }
}
