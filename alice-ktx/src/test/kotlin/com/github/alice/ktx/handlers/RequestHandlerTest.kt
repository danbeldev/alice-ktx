package com.github.alice.ktx.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.impl.request
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.testutils.builders.ProcessRequestEnvironmentBuilder.Companion.processRequestEnvironment
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class RequestHandlerTest {

    @Test
    fun `should return handler when condition is true`() = runTest {
        val dispatcher = Dispatcher()
        val environment = processRequestEnvironment()

        dispatcher.request(shouldHandle = { true }) { response { } }

        assertNotNull(dispatcher.findHandler(environment))
    }

    @Test
    fun `should not return handler when condition is false`() = runTest {
        val dispatcher = Dispatcher()
        val environment = processRequestEnvironment()

        dispatcher.request(shouldHandle = { false }) { response { } }

        assertNull(dispatcher.findHandler(environment))
    }
}