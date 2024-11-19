package com.github.examples.cities

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.middleware.outerMiddleware
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.webhook.impl.ktorWebhookServer
import com.github.alice.ktx.skill
import com.github.examples.cities.data.CitiesGameService

private enum class RegisterUserState {
    INPUT_FIST_NAME
}

fun main() {

    val citiesGameService = CitiesGameService()

    skill {
        skillId = "..."
        webhookServer = ktorWebhookServer {
            port = 8080
            path = "/alice"
        }
        dispatch {

            outerMiddleware {
                if(message.session.user == null) {
                    return@outerMiddleware response {
                        text = "У вас нет аккаунта в Яндексе."
                    }
                }
                null
            }

            newSession {
                val userId = message.session.user!!.userId
                val isUserExisting = citiesGameService.isUserExisting(userId)
                if(!isUserExisting) {
                    context.setState(RegisterUserState.INPUT_FIST_NAME.name)
                    response { text = "Как тебя зовут?" }
                }else {
                    response { text = citiesGameService.startGame(userId).name }
                }
            }

            message({ context.getState() == RegisterUserState.INPUT_FIST_NAME.name }) {
                val userId = message.session.user!!.userId
                val firstName = message.request.originalUtterance!!
                citiesGameService.createUser(id = userId, firstName = firstName)
                context.clear()
                response { text = citiesGameService.startGame(userId).name }
            }

            message {
                val userId = message.session.user!!.userId
                val cityName = message.request.command!!
                response {
                    text = citiesGameService.processUserAnswer(userId, cityName)
                }
            }
        }
    }.run()
}