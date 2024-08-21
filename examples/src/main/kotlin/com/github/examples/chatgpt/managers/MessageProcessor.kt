package com.github.examples.chatgpt.managers

import com.github.examples.chatgpt.services.ChatOpenApiService
import com.github.examples.chatgpt.models.Message
import kotlinx.coroutines.*

/**
 * Обрабатывает входящие сообщения от пользователя и генерирует соответствующие ответы.
 */
class MessageProcessor(
    private val chatHistoryManager: ChatHistoryManager,
    private val openApiService: ChatOpenApiService,
    private val delayedResponseManager: DelayedResponseManager,
    private val paginationManager: PaginationManager
) {

    suspend fun generateResponseForNewMessage(userId: String, userInput: String): String {
        val history = chatHistoryManager.getHistory(userId)
        chatHistoryManager.addMessage(userId, Message(role = "user", content = userInput))

        val deferredResponse = CoroutineScope(Dispatchers.IO).async {
            openApiService.sendMessage(history).choices.first().message
        }

        return try {
            val responseMessage = withTimeout(3800) { deferredResponse.await() }
            chatHistoryManager.addMessage(userId, responseMessage)
            paginationManager.getPaginatedResponse(userId, responseMessage)
        } catch (e: TimeoutCancellationException) {
            delayedResponseManager.prepareDelayedResponse(userId, deferredResponse)
            "Я подготовлю ответ через несколько секунд, только спросите меня: Алиса, готово?"
        }
    }
}
