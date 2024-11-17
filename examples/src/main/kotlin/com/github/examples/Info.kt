package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill

private enum class InfoState {
    SET_NAME,
    SET_AGE,
    SET_INFO
}

fun main() {

    skill {
        id = "..."
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            newSession {
                context.setState(InfoState.SET_NAME.name)
                response {
                    text = "Добро пожаловать в навык, как вас зовут?"
                }
            }
            message({ context.getState() == InfoState.SET_NAME.name }) {
                val username = message.request.originalUtterance.toString()
                context.updateData("name" to username)
                context.setState(InfoState.SET_AGE.name)
                response {
                    text = "Рад познакомиться $username, сколько вам лет?"
                }
            }
            message({ context.getState() == InfoState.SET_AGE.name }) {
                val age = message.request.originalUtterance.toString()
                context.updateData("age" to age)
                context.setState(InfoState.SET_INFO.name)
                response {
                    text = "Супер, расскажите о себе"
                }
            }
            message({context.getState() == InfoState.SET_INFO.name}) {
                val info = message.request.originalUtterance.toString()
                val data = context.getData()
                context.clear()
                response {
                    text = "Вот что мне удалось узнать\n\nИмя-${data["name"]}\nВозраст-${data["age"]}\nИнформация-$info"
                    endSession = true
                }
            }
        }
    }.run()
}