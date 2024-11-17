package com.github.alice.ktx.storage.key.impl

import com.github.alice.ktx.models.FSMStrategy.*
import com.github.alice.ktx.storage.key.KeyBuilder
import com.github.alice.ktx.storage.key.impl.BaseKeyBuilder.Builder
import com.github.alice.ktx.storage.models.DEFAULT_DESTINY
import com.github.alice.ktx.storage.models.StorageKey

fun baseKeyBuilder(body: Builder.() -> Unit = {}): BaseKeyBuilder {
    return Builder().build(body)
}

class BaseKeyBuilder(
    private val prefix: String = "fsm",
    private val separator: String = ":",
    private val withDestiny: Boolean = false
): KeyBuilder {

    class Builder {

        var prefix: String = "fsm"
        var separator: String = ":"
        var withDestiny: Boolean = false

        fun build(body: Builder.() -> Unit): BaseKeyBuilder {
            body()

            return BaseKeyBuilder(
                prefix = prefix,
                separator = separator,
                withDestiny = withDestiny
            )
        }
    }

    override suspend fun build(key: StorageKey, part: String): String {
        val parts = mutableListOf(prefix)

        if (key.skillId != null) parts.add(key.skillId)

        when(key.strategy) {
            USER -> parts.add(key.userId.orEmpty())
            SESSION -> parts.add(key.sessionId.orEmpty())
            APPLICATION -> parts.add(key.applicationId.orEmpty())
        }

        if (withDestiny) {
            parts.add(key.destiny)
        } else if (key.destiny != DEFAULT_DESTINY) {
            throw IllegalArgumentException(
                "Redis key builder is not configured to use key destiny other than default."
            )
        }

        parts.add(part)
        return parts.joinToString(separator);
    }
}