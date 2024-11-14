package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.handlers.message
import com.github.alice.ktx.models.button.button
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill
import com.github.alice.ktx.state.FSMContext
import com.github.alice.ktx.state.impl.BaseFSMContext
import kotlinx.serialization.Serializable

@Serializable
data class User(
    val username: String
)

enum class UserState {
    SET_NAME,
    GET_NAME
}
/**
 * Демонстрация работы с новыми методами (getTypedData, setTypedData, updateTypedData, removeTypedDate)
 * [FSMContext], [BaseFSMContext]
 * */
fun main() {
    skill {
        /**
         * Теперь можно заменить стандартный fsmContext на свою реализацию, если требуется
         * */
        fsmContext = { message ->
            BaseFSMContext(storage, id, fsmStrategy, message)
        }
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            newSession {
                context.setState(UserState.SET_NAME.name)
                response {
                    text = "Привет! Как тебя зовут?"
                }
            }
            message({ context.getState() == UserState.SET_NAME.name }) {
                val user = User(username = message.request.originalUtterance!!)
                context.setTypedData("user" to user, clazz = User::class)
                context.setState(UserState.GET_NAME.name)
                response {
                    text = "Рад познакомиться, ${user.username}!"
                    button {
                        title = "Узнать имя"
                    }
                }
            }
            message({ message.request.command == "узнать имя" }) {
                val user = context.getTypedData("user", User::class)
                response {
                    text = user?.username.toString()
                    endSession = true
                }
            }
        }
    }.run()
}