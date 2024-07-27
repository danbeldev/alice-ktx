package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.message
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill

fun main() {
    skill {
        id = "2e3e39c3-9fea-4d55-a754-9fa54b0d5502"
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            message({ message.session.new }) {
                response {
                    text = "Привет!"
                }
            }

            message {
                response {
                    text = message.request.originalUtterance.toString()
                }
            }
        }
    }.run()
}