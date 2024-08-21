package com.github.examples.chatgpt.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.message
import com.github.alice.ktx.models.response.response
import com.github.examples.chatgpt.managers.ResponseManager

fun Dispatcher.chatHandlers(manager: ResponseManager) {
    message {
        val userId = message.session.user?.userId!!
        val userInput = message.request.originalUtterance!!
        val responseText = manager.processUserInput(userId, userInput)

        response {
            text = responseText
            clearHistoryButton()
        }
    }
}