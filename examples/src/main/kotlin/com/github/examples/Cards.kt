package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.models.button.mediaButton
import com.github.alice.ktx.models.card.*
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill

// Замените идентификатор изображения на свой собственный
private const val IMAGE_ID = "1521359/48faa5e0f3d3842a6329"

fun main() {
    skill {
        skillId = "..."
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            message({ message.request.command == "card_items_list" }) {
                response {
                    text = "CARD ITEMS LIST"
                    cardItemsList {
                        header = "HEADER"
                        repeat(10) { index ->
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

            message({ message.request.command == "card_big_image" }) {
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

            message({ message.request.command == "card_image_gallery" }) {
                response {
                    cardImageGallery {
                        repeat(10) { index ->
                            item {
                                imageId = IMAGE_ID
                                title = "#${index + 1}"
                            }
                        }
                    }
                }
            }
        }
    }.run()
}