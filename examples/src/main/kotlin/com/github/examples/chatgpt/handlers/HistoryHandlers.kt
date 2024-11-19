package com.github.examples.chatgpt.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.models.response.button.button
import com.github.alice.ktx.models.response.MessageResponse
import com.github.alice.ktx.models.response.response
import com.github.examples.chatgpt.managers.ResponseManager

fun Dispatcher.historyHandlers(manager: ResponseManager) {
    message({ message.request.command == "очистить историю" }) {
        val userId = message.session.user?.userId!!
        manager.clearHistory(userId)
        response {
            text = "История диалога очищена."
        }
    }
}

fun MessageResponse.Builder.clearHistoryButton() {
    button { title = "Очистить историю" }
}