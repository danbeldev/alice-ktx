package com.github.alice.echo

import com.github.alice.dispatch
import com.github.alice.handlers.message
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
            message(event = { it.request.command.isEmpty() }) {
                response {
                    text = "Привет"
                }
            }

            message(event = { it.request.command.contains("привет") }) {
                response {
                    text = "Hi"
                }
            }
        }
    }.run()
}
