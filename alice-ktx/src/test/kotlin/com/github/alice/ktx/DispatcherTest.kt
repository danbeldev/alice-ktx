package com.github.alice.ktx

import com.github.alice.ktx.handlers.Handler
import com.github.alice.ktx.handlers.error.NetworkErrorHandler
import com.github.alice.ktx.middleware.Middleware
import com.github.alice.ktx.middleware.MiddlewareType
import io.mockk.mockk
import org.junit.Assert
import org.junit.Test

class DispatcherTest {

    @Test
    fun `should configuration dispatcher with handlers`() {
        val handler = mockk<Handler>()
        val middleware = mockk<Middleware>()
        val errorHandler = mockk<NetworkErrorHandler>()

        val dispatcher = Dispatcher()

        dispatcher.addHandler(handler)
        dispatcher.addMiddleware(middleware, MiddlewareType.INNER)
        dispatcher.addMiddleware(middleware, MiddlewareType.OUTER)
        dispatcher.addNetworkErrorHandler(errorHandler)

        Assert.assertEquals(1, dispatcher.commandHandlers.size)
        Assert.assertEquals(1, dispatcher.middlewares[MiddlewareType.INNER]?.size)
        Assert.assertEquals(1, dispatcher.middlewares[MiddlewareType.OUTER]?.size)
        Assert.assertEquals(1, dispatcher.networkErrorHandlers.size)
    }
}