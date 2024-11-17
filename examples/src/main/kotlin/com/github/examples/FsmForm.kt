package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.models.button.button
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill
import com.github.alice.ktx.storage.impl.redisStorage
import com.github.alice.ktx.storage.key.impl.baseKeyBuilder

enum class FormState {
    NAME,
    LINK_SKILLS,
    DEVICE
}

fun main() {
    skill {
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        storage = redisStorage {
            connect(host = "danbel.ru", password = "ggtt1234redis")
            keyBuilder = baseKeyBuilder {
                prefix = "alice"
            }
        }
        dispatch {
            newSession {
                context.clear()
                context.setState(FormState.NAME.name)
                response {
                    text = "Привет! Как тебя зовут?"
                }
            }

            message({ message.request.command == "отмена" }) {
                context.clear()
                response {
                    text = "Окей, стою. Пока-пока!"
                    endSession = true
                }
            }

            message({ context.getState() == FormState.NAME.name }) {
                val name = message.request.originalUtterance.toString()
                context.updateData("name" to name)
                context.setState(FormState.LINK_SKILLS.name)
                response {
                    text = "Рад познакомиться, $name!\nТебе нравятся навыки Алисы?"
                    button {
                        title = "Да"
                    }
                    button {
                        title = "Нет"
                    }
                }
            }

            message({ context.getState() == FormState.LINK_SKILLS.name && message.request.command == "нет" }) {
                val data = context.getData()
                context.clear()
                response {
                    text = "Ну, бывает.\n ${showSummary(data=data, positive=false)}"
                    endSession = true
                }
            }

            message({ context.getState() == FormState.LINK_SKILLS.name && message.request.command == "да" }) {
                context.setState(FormState.DEVICE.name)
                response {
                    text = "Класс! Мне тоже!\nЧерез какое устройство ты обычно их используешь?"
                }
            }

            message({ context.getState() == FormState.LINK_SKILLS.name }) {
                response {
                    text = "Не могу понять тебя... Можешь повторить, пожалуйста?"
                    button {
                        title = "Да"
                    }
                    button {
                        title = "Нет"
                    }
                }
            }

            message({ context.getState() == FormState.DEVICE.name }) {
                val device = message.request.originalUtterance.toString()
                context.updateData("device" to device)
                val data = context.getData()
                context.clear()

                val text = if(device == "телефон")
                    "С телефона? Да, это самое удобное, с чего можно пользоваться Алисой.\n${showSummary(data)}"
                else
                    showSummary(data)

                response {
                    this.text = text
                    endSession = true
                }
            }
        }
    }.run()
}

private fun showSummary(data: Map<String, String>, positive: Boolean = true): String {
    val name = data["name"]
    val device = data.getOrDefault("device", "чём-то непонятном")
    var text = "Я буду помнить, $name, что тебе нравятся навыки Алисы на $device."
    if(positive) text += " тебе не нравятся навыки Алисы..."
    return text
}