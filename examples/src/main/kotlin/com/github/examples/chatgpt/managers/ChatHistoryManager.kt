package com.github.examples.chatgpt.managers

import com.github.examples.chatgpt.models.Message

/**
 * Управляет историей сообщений.
 */
class ChatHistoryManager {
    private val conversationHistory = linkedMapOf<String, ArrayList<Message>>()

    fun getHistory(userId: String): ArrayList<Message> {
        return conversationHistory.getOrPut(userId) { arrayListOf() }
    }

    fun addMessage(userId: String, message: Message) {
        val history = getHistory(userId)
        history.add(message)
        trimHistory(history)
    }

    fun clearHistory(userId: String) {
        conversationHistory.remove(userId)
    }

    private fun trimHistory(history: ArrayList<Message>, maxLength: Int = 4065) {
        var currentLength = history.sumOf { it.content.length }

        while (history.isNotEmpty() && currentLength > maxLength) {
            val removedMessage = history.removeAt(0)
            currentLength -= removedMessage.content.length
        }
    }
}
