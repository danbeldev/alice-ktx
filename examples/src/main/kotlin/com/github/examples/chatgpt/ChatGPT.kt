package com.github.examples.chatgpt

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.webhook.impl.ktorWebhookServer
import com.github.alice.ktx.skill
import com.github.examples.chatgpt.handlers.baseHandlers
import com.github.examples.chatgpt.handlers.chatHandlers
import com.github.examples.chatgpt.handlers.historyHandlers
import com.github.examples.chatgpt.managers.ResponseManager


fun main() {
    val responseManager = ResponseManager()

    skill {
        webhookServer = ktorWebhookServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
            baseHandlers()
            historyHandlers(manager = responseManager)
            chatHandlers(manager = responseManager)
        }
    }.run()
}
