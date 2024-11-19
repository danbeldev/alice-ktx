package com.github.alice.ktx.models.request.state

import com.github.alice.ktx.fsm.models.FSMStrategy
import com.github.alice.ktx.fsm.models.FSMStrategy.*
import kotlinx.serialization.Serializable

/**
 * @param user Состояние навыка в контексте авторизованного пользователя.
 * @param session Состояние навыка в контексте сессии.
 * @param application Состояние навыка в контексте экземпляра приложения пользователя.
 * */
@Serializable
data class State(
    val user: StateRequest? = null,
    val session: StateRequest? = null,
    val application: StateRequest? = null
){
    fun getCurrentStateAndData(strategy: FSMStrategy): Pair<String?, Map<String, String>?> {
        return when (strategy) {
            USER -> user?.state to user?.data
            SESSION -> session?.state to session?.data
            APPLICATION -> application?.state to application?.data
        }
    }
}

@Serializable
data class StateRequest(
    val state: String? = null,
    val data: Map<String, String>? = null
)
