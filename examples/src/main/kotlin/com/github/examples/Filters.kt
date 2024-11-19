package com.github.examples

import com.github.alice.ktx.dispatch
import com.github.alice.ktx.handlers.filters.Filter
import com.github.alice.ktx.handlers.impl.message
import com.github.alice.ktx.handlers.impl.request
import com.github.alice.ktx.models.response.response
import com.github.alice.ktx.server.impl.ktorWebServer
import com.github.alice.ktx.skill

fun main() {
    skill {
        webServer = ktorWebServer {
            port = 8080
            path = "/alice"
        }
        dispatch {
           message({ filter(Filter.All) }) {
                response {
                    text = messageText
                }
            }
        }
    }.run()
}