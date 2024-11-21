package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.webhook.impl.ktorWebhookServer
import com.github.alice.ktx.skill

fun main() {
    skill {
        webhookServer = ktorWebhookServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            newSession {
                response {
                    text = "Привет!"
                }
            }

            message {
                response {
                    text = messageText
                }
            }
        }
    }.run()
}