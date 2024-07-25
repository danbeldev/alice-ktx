package com.github.alice.echo

import com.github.alice.dispatch
import com.github.alice.handlers.message
import com.github.alice.models.response.addButton
import com.github.alice.models.response.response
import com.github.alice.server.impl.ktorWebServer
import com.github.alice.skill

fun main() {
    skill {
        id = "2e3e39c3-9fea-4d55-a754-9fa54b0d5502"
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {

            message(event = { request.command?.contains("привет") ?: false }) {
                response {
                    text = "Hi"
                    addButton {
                        title = "Today"
                        payload = mapOf("schedule_type" to "TODAY")
                    }
                    addButton {
                        title = "Tomorrow"
                        payload = mapOf("schedule_type" to "TOMORROW")
                    }
                }
            }

            message(event = { request.type == "ButtonPressed" }) {
                response {
                    text = message.request.payload?.get("schedule_type").toString()
                }
            }

            message(event = { request.command.isNullOrEmpty() }) {
                response {
                    text = "Привет"
                }
            }
        }
    }.run()
}
