package com.github.alice.ktx.storage.models

import com.github.alice.ktx.models.FSMStrategy
import com.github.alice.ktx.models.request.MessageRequest

data class StorageKey(
    val skillId: String?,
    val userId: String?,
    val sessionId: String?,
    val applicationId: String?,
    val strategy: FSMStrategy
) {
    class Builder {
        fun build(skillId: String?, message: MessageRequest, strategy: FSMStrategy): StorageKey {
            return StorageKey(
                skillId = skillId,
                userId = if (strategy == FSMStrategy.USER) message.session.user?.userId else null,
                sessionId = if (strategy == FSMStrategy.SESSION) message.session.sessionId else null,
                applicationId = if (strategy == FSMStrategy.APPLICATION) message.session.application.applicationId else null,
                strategy = strategy
            )
        }
    }
}
