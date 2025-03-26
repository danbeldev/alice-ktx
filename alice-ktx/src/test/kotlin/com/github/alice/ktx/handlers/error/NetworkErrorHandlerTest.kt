package com.github.alice.ktx.handlers.error

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.testutils.builders.ProcessRequestEnvironmentBuilder.Companion.processRequestEnvironment
import com.github.alice.ktx.testutils.mothers.MessageRequestMother
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Test

class NetworkErrorHandlerTest {

    @Test
    fun `should register network error handler via DSL`() {
        val dispatcher = Dispatcher()

        dispatcher.responseFailure { response {  } }

        Assert.assertEquals(1, dispatcher.networkErrorHandlers.size)
    }

    @Test
    fun `resolveErrorToResponse should return response when generic error handler registered`() = runTest {
        val dispatcher = Dispatcher()
        val processRequestEnvironment = processRequestEnvironment()

        dispatcher.responseFailure { response {  } }

        Assert.assertNotNull(dispatcher.resolveErrorToResponse(processRequestEnvironment, Exception()))
    }

    @Test
    fun `resolveErrorToResponse should return null when no handlers registered`() = runTest {
        val dispatcher = Dispatcher()
        val processRequestEnvironment = processRequestEnvironment()

        Assert.assertNull(dispatcher.resolveErrorToResponse(processRequestEnvironment, Exception()))
    }

    @Test
    fun `resolveErrorToResponse should return null when exception type doesn't match`() = runTest {
        val dispatcher = Dispatcher()
        val processRequestEnvironment = processRequestEnvironment()

        dispatcher.responseFailure(ArithmeticException::class) { response {  } }

        Assert.assertNull(dispatcher.resolveErrorToResponse(processRequestEnvironment, Exception()))
    }

    @Test
    fun `resolveErrorToResponse should return response when exception type matches`() = runTest {
        val dispatcher = Dispatcher()
        val processRequestEnvironment = processRequestEnvironment()

        dispatcher.responseFailure(ArithmeticException::class) { response {  } }

        Assert.assertNotNull(dispatcher.resolveErrorToResponse(processRequestEnvironment, ArithmeticException()))
    }

    @Test
    fun `resolveErrorToResponse should return response when predicate condition matches`() = runTest {
        val dispatcher = Dispatcher()
        val processRequestEnvironment = processRequestEnvironment {
            message = MessageRequestMother.createMessageRequest(
                session = MessageRequestMother.createSession(
                    new = true
                )
            )
        }

        dispatcher.responseFailure({ message.session.new }) { response {  } }

        Assert.assertNotNull(dispatcher.resolveErrorToResponse(processRequestEnvironment, Exception()))
    }

    @Test
    fun `resolveErrorToResponse should return null when predicate condition doesn't match`() = runTest {
        val dispatcher = Dispatcher()
        val processRequestEnvironment = processRequestEnvironment {
            message = MessageRequestMother.createMessageRequest(
                session = MessageRequestMother.createSession(
                    new = false
                )
            )
        }

        dispatcher.responseFailure({ message.session.new }) { response {  } }

        Assert.assertNull(dispatcher.resolveErrorToResponse(processRequestEnvironment, Exception()))
    }
}