package com.github.alice.ktx.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.models.request.content.RequestContentType
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.testutils.builders.ProcessRequestEnvironmentBuilder.Companion.processRequestEnvironment
import com.github.alice.ktx.testutils.mothers.MessageRequestMother
import com.github.alice.ktx.testutils.mothers.MessageRequestMother.createRequestContent
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class MessageHandlerTest {

    @Test
    fun `should return handler for simple utterance request`() = runTest {
        val dispatcher = Dispatcher()
        val environment = processRequestEnvironment {
            message = MessageRequestMother.createMessageRequest(
                request = createRequestContent(
                    type = RequestContentType.SimpleUtterance
                )
            )
        }

        dispatcher.message {
            response {}
        }

        assertNotNull(dispatcher.findHandler(environment))
    }

    @Test
    fun `should return handler when shouldHandle is true`() = runTest {
        val dispatcher = Dispatcher()
        val environment = processRequestEnvironment {
            message = MessageRequestMother.createMessageRequest(
                request = createRequestContent(
                    type = RequestContentType.SimpleUtterance
                )
            )
        }

        dispatcher.message(
            shouldHandle = { true }
        ) {
            response {}
        }

        assertNotNull(dispatcher.findHandler(environment))
    }

    @Test
    fun `should not return handler when shouldHandle is false`() = runTest {
        val dispatcher = Dispatcher()
        val environment = processRequestEnvironment {
            message = MessageRequestMother.createMessageRequest(
                request = createRequestContent(
                    type = RequestContentType.SimpleUtterance
                )
            )
        }

        dispatcher.message(
            shouldHandle = { false }
        ) {
            response {}
        }

        assertNull(dispatcher.findHandler(environment))
    }

    @Test
    fun `should not return handler for button pressed request`() = runTest {
        val dispatcher = Dispatcher()
        val environment = processRequestEnvironment {
            message = MessageRequestMother.createMessageRequest(
                request = createRequestContent(
                    type = RequestContentType.ButtonPressed
                )
            )
        }

        dispatcher.message(
            shouldHandle = { true }
        ) {
            response {}
        }

        assertNull(dispatcher.findHandler(environment))
    }
}