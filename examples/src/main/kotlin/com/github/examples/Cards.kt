package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.handlers.impl.request
import com.github.alice.ktx.models.response.button.mediaButton
import com.github.alice.ktx.models.response.card.*
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.webhook.impl.ktorWebhookServer
import com.github.alice.ktx.skill

// Замените идентификатор изображения на свой собственный
private const val IMAGE_ID = "965417/0a473b7efa3db621e7a7"

fun main() {
    skill {
        webhookServer = ktorWebhookServer {
            port = 8080
            path = "/alice"
        }
        dispatch {

            newSession {
                response {
                    text = "Привет"
                }
            }

            message({ messageText == "card_items_list" }) {
                response {
                    text = "CARD ITEMS LIST"
                    cardItemsList {
                        header = "HEADER"
                        repeat(5) { index ->
                            item {
                                imageId = IMAGE_ID
                                title = "#${index + 1}"
                            }
                        }
                        footer {
                            text = "Footer text"
                            mediaButton {
                                text = "Click"
                            }
                        }
                    }
                }
            }

            message({ messageText == "card_big_image" }) {
                response {
                    cardBigImage {
                        imageId = IMAGE_ID
                        title = "CARD BIG IMAGE"
                        mediaButton {
                            text = "Open url"
                            url = "https://ya.ru"
                        }
                    }
                }
            }

            message({ messageText == "card_image_gallery" }) {
                response {
                    cardImageGallery {
                        repeat(5) { index ->
                            item {
                                imageId = IMAGE_ID
                                title = "#${index + 1}"
                            }
                        }
                    }
                }
            }

            request {
                response {
                    text = "Invalid message: ${message.request.originalUtterance}"
                }
            }
        }
    }.run()
}