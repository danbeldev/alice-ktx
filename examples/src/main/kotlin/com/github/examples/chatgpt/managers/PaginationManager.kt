package com.github.examples.chatgpt.managers

import com.github.examples.chatgpt.models.Message

/**
 * Управляет пагинацией длинных ответов, разбивая текст на страницы и обеспечивая навигацию между ними.
 * Алиса имеет ограничение на длину ответа в 1024 символов.
 */
class PaginationManager {
    private val paginationResponse = mutableMapOf<String, MutableList<String>>()

    fun getPaginatedResponse(userId: String, response: Message): String {
        return if (isPaginationRequired(response)) initializePagination(userId, response) else response.content
    }

    fun getNextPaginatedResponse(userId: String): String {
        val pages = paginationResponse[userId] ?: return "Страниц для отображения нет."
        val currentPage = pages.removeFirstOrNull() ?: return "Нет дальнейших страниц."

        return if (pages.isNotEmpty()) {
            paginationResponse[userId] = pages
            "$currentPage\n\nЧтобы продолжить скажите: Алиса, дальше"
        } else {
            paginationResponse.remove(userId)
            currentPage
        }
    }

    private fun isPaginationRequired(response: Message): Boolean {
        return response.content.length > 1024
    }

    private fun initializePagination(userId: String, response: Message): String {
        val pages = splitTextIntoPages(response.content)
        val currentPage = pages.removeFirst()
        paginationResponse[userId] = pages
        return "$currentPage\n\nЧтобы продолжить скажите: Алиса, дальше"
    }

    private fun splitTextIntoPages(input: String, pageSize: Int = 980): MutableList<String> {
        val words = input.split(" ")
        val pages = mutableListOf<String>()
        var currentPage = StringBuilder()

        for (word in words) {
            if (currentPage.length + word.length + 1 > pageSize) {
                pages.add(currentPage.toString())
                currentPage = StringBuilder()
            }
            if (currentPage.isNotEmpty()) {
                currentPage.append(" ")
            }
            currentPage.append(word)
        }

        if (currentPage.isNotEmpty()) {
            pages.add(currentPage.toString())
        }

        return pages
    }
}
