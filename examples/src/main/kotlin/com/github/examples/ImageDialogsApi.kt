package com.github.examples

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.api.common.Response
import com.github.alice.ktx.api.dialog.yandex.impl.ktorYandexDialogApi
import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.models.response.button.mediaButton
import com.github.alice.ktx.models.response.card.cardItemsList
import com.github.alice.ktx.models.response.card.item
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill
import java.io.File

fun main() {
    skill {
        skillId = "..."
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
                val result = dialogApi?.deleteImage(imageId)
                response {
                    text = if(result is Response.Success) "Success" else "Failure"
                }
            }

            message({ message.request.originalUtterance == "upload_image_file" }) {
                val file = File("examples/src/main/resources/ktor_icon.png")
                val response = dialogApi?.uploadImage(file)
                response {
                    text = when(response) {
                        is Response.Failed -> response.message
                        is Response.Success -> response.data.image.toString()
                        null -> throw NullPointerException("Response was null")
                    }
                }
            }
        }
    }.run()
}

private fun Dispatcher.messageCardImages() {
    message({ message.session.new }) {
        when(val imageResponse = dialogApi?.getAllImages()) {
            is Response.Failed, null -> response { text = "Failed" }
            is Response.Success -> response {
                cardItemsList {
                    header = "Images (${imageResponse.data.total})"
                    imageResponse.data.images.forEach { image ->
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
}