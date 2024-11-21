package com.github.examples.chatgpt.handlers

import com.github.alice.ktx.Dispatcher
import com.github.alice.ktx.handlers.error.responseFailure
import com.github.alice.ktx.handlers.filters.Filter
import com.github.alice.ktx.handlers.impl.help
import com.github.alice.ktx.handlers.impl.newSession
import com.github.alice.ktx.middleware.outerMiddleware
import com.github.alice.ktx.models.response.response

fun Dispatcher.baseHandlers() {
    outerMiddleware {
        if (!isValidFor(Filter.Authorized)) return@outerMiddleware response {
            text = "Для использование необходимо войти в аккаунт Яндекс."
        }

        null
    }

    responseFailure {
        response {
            text = "Прошу прощения, возникла ошибка."
        }
    }

    newSession {
        response {
            text = "Добро пожаловать в ChatGPT-бот!"
        }
    }

    help {
        response {
            text = "Я ChatGPT-бот!"
        }
    }
}