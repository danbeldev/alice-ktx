package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.request
import com.github.alice.ktx.middleware.afterHandle
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.skill
import com.github.alice.ktx.webhook.impl.ktorWebhookServer

fun main() {
    skill {
        webhookServer = ktorWebhookServer {
            port = 8080
            path = "/alice"
        }

        dispatch {
            afterHandle { processRequestEnvironment, messageResponse ->
                messageResponse.copy(
                    response = messageResponse.response?.copy(
                        text = messageResponse.response?.text + processRequestEnvironment.message.version
                    )
                )
            }

            request {
                response {
                    text = "test after handel, version alice: "
                }
            }
        }
    }.run()
}