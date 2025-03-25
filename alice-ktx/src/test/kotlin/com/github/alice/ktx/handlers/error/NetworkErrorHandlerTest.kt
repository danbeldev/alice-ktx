package com.github.alice.ktx.handlers.error

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.response.response
import org.junit.Assert
import org.junit.Test

class NetworkErrorHandlerTest {

    @Test
    fun `should register network error handler via DSL`() {
        val dispatcher = Dispatcher()

        dispatcher.responseFailure { response {  } }

        Assert.assertEquals(1, dispatcher.networkErrorHandlers.size)
    }
}