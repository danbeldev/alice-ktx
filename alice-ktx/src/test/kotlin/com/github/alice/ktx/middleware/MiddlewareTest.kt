package com.github.alice.ktx.middleware

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.response.response
import org.junit.Assert
import org.junit.Test

class MiddlewareTest {

    @Test
    fun `should register inner middleware via DSL`()  {
        val dispatcher = Dispatcher()

        dispatcher.innerMiddleware { response {  } }

        Assert.assertEquals(1, dispatcher.middlewares[MiddlewareType.INNER]?.size)
    }

    @Test
    fun `should register outer middleware via DSL`() {
        val dispatcher = Dispatcher()

        dispatcher.outerMiddleware { response {  } }

        Assert.assertEquals(1, dispatcher.middlewares[MiddlewareType.OUTER]?.size)
    }
}