package com.github.alice.echo

import com.github.alice.dispatch
import com.github.alice.handlers.message
import com.github.alice.models.response.button
import com.github.alice.models.response.response
import com.github.alice.server.impl.ktorWebServer
import com.github.alice.skill

enum class Form {
    NAME, LINK_SKILLS, DEVICE
}

fun main() {
    skill {
        id = "2e3e39c3-9fea-4d55-a754-9fa54b0d5502"
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {

            message({ message.request.command == "test" }) {
                response {
                    text = "OK.."
                }
            }

            message({ message.request.command == "да" }) {
                val name = state.getData("name")
                state.clear()
                response {
                    text = "Yes, $name!"
                    button {
                        title = "TEST"
                    }
                }
            }

            message({ message.request.command == "нет" }) {
                state.clear()
                response {
                    text = "No.."
                    button {
                        title = "TEST"
                    }
                }
            }

            message({ state == Form.NAME.name }) {
                state.setData("name" to message.request.originalUtterance.toString())
                state.setState(Form.LINK_SKILLS.name)
                response {
                    text = "Рад познакомиться, ${message.request.originalUtterance}!\nТебе нравятся навыки Алисы?"
                    button {
                        title = "Да"
                    }
                    button {
                        title = "Нет"
                    }
                }
            }

            message {
                state.setState(Form.NAME.name)
                response {
                    text = "Привет! Как тебя зовут ?"
                }
            }
        }
    }.run()
}
