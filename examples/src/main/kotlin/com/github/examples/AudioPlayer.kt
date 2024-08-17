package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.message
import com.github.alice.ktx.models.audioPlayer.audioPlayer
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill

fun main() {
    skill {
        id = "..."
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            message({ message.session.new }) {
                response {
                    shouldListen = false
                    audioPlayer {
                        url = "https://example.com/stream-audio-url"
                        title = "Песня"
                        subTitle = "Артист"
                        artUrl = "https://example.com/art.png"
                        backgroundImageUrl = "https://example.com/background-image.png"
                    }
                }
            }
        }
    }.run()
}