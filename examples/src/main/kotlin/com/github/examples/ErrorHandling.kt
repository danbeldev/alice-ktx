package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.error.responseFailure
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill

fun main() {
    skill {
        skillId = "..."
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            responseFailure({ message.session.new }) {
                response {
                    text = "В начале сессии произошла ошибка"
                }
            }
            responseFailure(ArithmeticException::class) {
                response {
                    text = "Произошла арифметическая ошибка"
                }
            }
            responseFailure {
                response {
                    text = "Произошла ошибка"
                }
            }
            message {
                response {
                    text = (10 / 0).toString()
                }
            }
        }
    }.run()
}

