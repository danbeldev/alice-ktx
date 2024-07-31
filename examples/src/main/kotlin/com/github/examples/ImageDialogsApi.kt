package com.github.examples

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.api.dialog.yandex.impl.ktorYandexDialogApi
import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.message
import com.github.alice.ktx.models.button.mediaButton
import com.github.alice.ktx.models.card.cardItemsList
import com.github.alice.ktx.models.card.item
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
            messageCardImages()
            message({ message.request.payload?.keys?.contains("delete_image_id") == true }) {

                val imageId = message.request.payload!!["delete_image_id"].toString()
                val result = dialogApi?.deleteImage(imageId) ?: false
                response {
                    text = if(result) "Success" else "Failure"
                }
            }

            message({ message.request.originalUtterance == "upload_image_file" }) {
                val file = File("ktor_icon.png")
                val result = dialogApi?.uploadImage(file)
                response {
                    text = result?.image.toString()
                }
            }
        }
    }.run()
}

private fun Dispatcher.messageCardImages() {
    message({ message.session.new }) {
        val imageResponse = dialogApi?.getAllImages()
        response {
            cardItemsList {
                header = "Images (${imageResponse?.total})"
                imageResponse?.images?.forEach { image ->
                    item {
                        imageId = image.id
                        mediaButton {
                            text = "Delete"
                            payload = mapOf("delete_image_id" to image.id)
                        }
                    }
                }
            }
        }
    }
}