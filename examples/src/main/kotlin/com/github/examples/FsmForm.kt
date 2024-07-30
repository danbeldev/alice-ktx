package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.message
import com.github.alice.ktx.models.button.button
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill

enum class FormState {
    NAME,
    LINK_SKILLS,
    DEVICE
}

fun main() {
    skill {
        id = "..."
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            message({ message.session.new }) {
                state.setState(FormState.NAME.name)
                response {
                    text = "Привет! Как тебя зовут?"
                }
            }

            message({ message.request.command == "отмена" }) {
                state.clear()
                response {
                    text = "Окей, стою. Пока-пока!"
                    endSession = true
                }
            }

            message({ state == FormState.NAME.name }) {
                val name = message.request.originalUtterance.toString()
                state.updateData("name" to name)
                state.setState(FormState.LINK_SKILLS.name)
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

            message({ state == FormState.LINK_SKILLS.name && message.request.command == "нет" }) {
                val data = state.getData()
                state.clear()
                response {
                    text = "Ну, бывает.\n ${showSummary(data=data, positive=false)}"
                    endSession = true
                }
            }

            message({ state == FormState.LINK_SKILLS.name && message.request.command == "да" }) {
                state.setState(FormState.DEVICE.name)
                response {
                    text = "Класс! Мне тоже!\nЧерез какое устройство ты обычно их используешь?"
                }
            }

            message({ state == FormState.LINK_SKILLS.name }) {
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

            message({ state == FormState.DEVICE.name }) {
                val device = message.request.originalUtterance.toString()
                state.updateData("device" to device)
                val data = state.getData()
                state.clear()

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