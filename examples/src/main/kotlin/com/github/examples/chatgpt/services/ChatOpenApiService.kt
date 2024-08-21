package com.github.examples.chatgpt.services

import com.github.examples.chatgpt.models.Choices
import com.github.examples.chatgpt.models.Message
import com.github.examples.chatgpt.models.OpenApiRequest
import com.github.examples.chatgpt.models.OpenApiResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ChatOpenApiService {

    private companion object {
        private const val BASE_URL = "https://api.vsegpt.ru/v1/"
        private const val TIMEOUT_MS = 30000L
        private const val OPEN_API_TOKEN = "OPEN_API_TOKEN"
        private const val OPEN_API_MODEL = "OPEN_API_MODEL"
    }

    private val client = HttpClient(CIO) {
        install(HttpTimeout) {
            requestTimeoutMillis = TIMEOUT_MS
        }
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        defaultRequest {
            url(BASE_URL)
            headers.append(HttpHeaders.Authorization, "Bearer $OPEN_API_TOKEN")
            headers.append(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        }
    }

    suspend fun sendMessage(messages: List<Message>): OpenApiResponse {
        return try {
            val requestBody = OpenApiRequest(
                model = OPEN_API_MODEL,
                messages = messages
            )
            val response = client.post("chat/completions") {
                setBody(requestBody)
                contentType(ContentType.Application.Json)
            }

            response.body<OpenApiResponse>()
        } catch (e: Exception) {
            e.printStackTrace()
            OpenApiResponse(
                choices = listOf(
                    Choices(
                        message = Message(
                            role = "assistant",
                            content = "Извините, произошла ошибка."
                        )
                    )
                )
            )
        }
    }
}