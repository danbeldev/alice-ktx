package com.github.examples.chatgpt.managers

import com.github.examples.chatgpt.models.Message
import kotlinx.coroutines.*

/**
 * Управляет задержанными ответами, когда ответ от API занимает больше времени, чем ожидалось.
 * Алиса имеет ограничение на время запроса в 4.5 секунды.
 */
class DelayedResponseManager(
    private val chatHistoryManager: ChatHistoryManager,
    private val paginationManager: PaginationManager
) {
    private val delayedResponses = mutableMapOf<String, Pair<Boolean, Message?>>()

    suspend fun prepareDelayedResponse(userId: String, deferredResponse: Deferred<Message>) {
        CoroutineScope(Dispatchers.IO).launch {
            delayedResponses[userId] = false to null
            val delayedResponseMessage = deferredResponse.await()
            chatHistoryManager.addMessage(userId, delayedResponseMessage)
            delayedResponses[userId] = true to delayedResponseMessage
        }
    }

    fun processDelayedResponse(userId: String): String {
        val delayedResponse = delayedResponses[userId] ?: return "Ответ ещё не готов."

        return if (delayedResponse.first) {
            delayedResponses.remove(userId)
            val content = delayedResponse.second
            if (content != null)
                paginationManager.getPaginatedResponse(userId, content)
            else
                "Ошибка при обработке ответа."
        } else {
            "Ответ ещё не готов."
        }
    }

    fun hasPendingResponse(userId: String): Boolean {
        return delayedResponses[userId] != null
    }

    fun clear(userId: String) {
        delayedResponses.remove(userId)
    }
}
