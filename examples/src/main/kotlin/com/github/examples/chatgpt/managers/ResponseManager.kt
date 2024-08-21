package com.github.examples.chatgpt.managers

import com.github.examples.chatgpt.services.ChatOpenApiService

/**
 * Координирует процесс генерации ответов, интегрируя логику пагинации, задержанных ответов и обработки сообщений.
 */
class ResponseManager {

    private val chatHistoryManager = ChatHistoryManager()
    private val openApiService = ChatOpenApiService()
    private val paginationManager = PaginationManager()
    private val delayedResponseManager = DelayedResponseManager(chatHistoryManager, paginationManager)
    private val messageProcessor = MessageProcessor(chatHistoryManager, openApiService, delayedResponseManager, paginationManager)

    suspend fun processUserInput(userId: String, userInput: String): String {
        return when {
            delayedResponseManager.hasPendingResponse(userId) && userInput.contains("готово", ignoreCase = true) -> {
                delayedResponseManager.processDelayedResponse(userId)
            }
            paginationManager.getNextPaginatedResponse(userId).isNotBlank() && userInput.contains("дальше", ignoreCase = true) -> {
                paginationManager.getNextPaginatedResponse(userId)
            }
            else -> {
                messageProcessor.generateResponseForNewMessage(userId, userInput)
            }
        }
    }

    fun clearHistory(userId: String) {
        chatHistoryManager.clearHistory(userId)
        delayedResponseManager.clear(userId)
    }
}
