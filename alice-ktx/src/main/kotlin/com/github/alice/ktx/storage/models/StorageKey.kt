package com.github.alice.ktx.storage.models

import com.github.alice.ktx.fsm.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest

const val DEFAULT_DESTINY = "default"

data class StorageKey(
    val skillId: String?,
    val userId: String?,
    val sessionId: String?,
    val applicationId: String?,
    val strategy: FSMStrategy,
    val destiny: String = DEFAULT_DESTINY
) {
    class Builder {
        fun build(skillId: String?, message: MessageRequest, strategy: FSMStrategy): StorageKey {
            return StorageKey(
                skillId = skillId,
                userId = message.session.user?.userId,
                sessionId = message.session.sessionId,
                applicationId = message.session.application.applicationId,
                strategy = strategy
            )
        }
    }
}
