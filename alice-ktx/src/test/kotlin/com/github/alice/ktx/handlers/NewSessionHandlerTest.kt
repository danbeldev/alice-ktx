package com.github.alice.ktx.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.testutils.builders.ProcessRequestEnvironmentBuilder.Companion.processRequestEnvironment
import com.github.alice.ktx.testutils.mothers.MessageRequestMother
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class NewSessionHandlerTest {

    @Test
    fun `should return handler for new session`() = runTest {
        val dispatcher = Dispatcher()
        val environment = processRequestEnvironment {
            message = MessageRequestMother.createMessageRequest(
                session = MessageRequestMother.createSession(
                    new = true
                )
            )
        }

        dispatcher.newSession {
            response {  }
        }

        assertNotNull(dispatcher.findHandler(environment))
    }

    @Test
    fun `should not return handler for existing session`() = runTest {
        val dispatcher = Dispatcher()
        val environment = processRequestEnvironment {
            message = MessageRequestMother.createMessageRequest(
                session = MessageRequestMother.createSession(
                    new = false
                )
            )
        }

        dispatcher.newSession {
            response {  }
        }

        assertNull(dispatcher.findHandler(environment))
    }
}