package com.github.alice.ktx.models.request.content

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param dangerousContext Признак реплики, которая содержит криминальный подтекст (самоубийство, разжигание ненависти, угрозы).
 * Вы можете настроить навык на определенную реакцию для таких случаев — например, отвечать «Не понимаю, о чем вы. Пожалуйста, переформулируйте вопрос.»
 * */
@Serializable
data class Markup(
    @SerialName("dangerous_context")
    val dangerousContext: Boolean
)