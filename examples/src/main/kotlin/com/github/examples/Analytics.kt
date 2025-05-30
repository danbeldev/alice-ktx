package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.models.response.analytics.Analytics
import com.github.alice.ktx.models.response.analytics.AnalyticsEvent
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
                    analytics = Analytics(
                        events = listOf(
                            AnalyticsEvent(
                                name = "new_session"
                            )
                        )
                    )
                }
            }

            message {
                response {
                    text = command
                    analytics = Analytics(
                        events = listOf(
                            AnalyticsEvent(
                                name = "echo",
                                value = mapOf("text_command" to command)
                            )
                        )
                    )
                }
            }
        }
    }.run()
}