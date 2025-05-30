package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.audioPlayer
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.models.response.audioPlayer.audioPlayer
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.webhook.impl.ktorWebhookServer
import com.github.alice.ktx.skill

fun main() {
    skill {
        webhookServer = ktorWebhookServer {
            port = 8080
            path = "/alice"
        }
        dispatch {

            audioPlayer {
                response {
                    text = type.name
                }
            }

            newSession {
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