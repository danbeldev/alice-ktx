package com.github.examples

import com.github.alice.ktx.api.common.Response
import com.github.alice.ktx.api.dialog.yandex.impl.ktorYandexDialogApi
import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.message
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill
import java.io.File

fun main() {
    skill {
        id = "..."
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dialogApi = ktorYandexDialogApi {
            oauthToken = "..."
        }
        dispatch {
            message({ message.request.originalUtterance == "upload_sound_file" }) {
                val file = File("examples/src/main/resources/file_example.mp3")
                val response = dialogApi?.uploadSound(file)
                response {
                    text = when(response) {
                        is Response.Failed, null -> "Failed"
                        is Response.Success -> response.data.sound.toString()
                    }
                }
            }
            message({ message.session.new }) {
                when(val response = dialogApi?.getAllSounds()) {
                    is Response.Failed, null -> response { text = "Failed" }
                    is Response.Success -> response { text = response.data.sounds.toString() }
                }
            }
        }
    }.run()
}