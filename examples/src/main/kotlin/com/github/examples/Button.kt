package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.models.button.button
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill

private enum class SchedulesType(val title: String) {
    LAST_DAY("Последний день"),
    TODAY("Сегодня"),
    NEXT_DAY("Следующий день")
}

fun main() {
    skill {
        skillId = "..."
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            newSession {
                response {
                    text = "Выберите тип"
                    SchedulesType.entries.forEach {
                        button {
                            title = it.name
                            payload = mapOf("schedule_type" to it.name)
                        }
                    }
                }
            }
            message({ message.request.payload.keys.contains("schedule_type") }) {
                val scheduleType = SchedulesType.valueOf(message.request.payload["schedule_type"]!!.toString())
                response {
                    text = "Result: ${scheduleType.title}"
                }
            }
        }
    }.run()
}