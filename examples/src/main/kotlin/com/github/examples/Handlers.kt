package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.buttonPressed
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.models.response.button.button
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.webhook.impl.ktorWebhookServer
import com.github.alice.ktx.skill

/**
 * Демонстрация новых хендлеров [newSession], [buttonPressed]
 * */
fun main() {
    skill {
        webhookServer = ktorWebhookServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            newSession {
                response {
                    text = "Start"
                    button {
                        title = "Test"
                        payload = mapOf("test" to "1")
                    }
                }
            }

            buttonPressed({ payload["test"] == "1" }) {
                response {
                    text = "Click"
                }
            }
        }
    }.run()
}