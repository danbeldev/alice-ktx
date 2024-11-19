package com.github.alice.ktx.models.response.button

import com.github.alice.ktx.common.AliceResponseDsl
import com.github.alice.ktx.models.response.MessageResponse
import kotlinx.serialization.Serializable

@AliceResponseDsl
fun MessageResponse.Builder.button(body: Button.Builder.() -> Unit) {
    val button = Button.Builder().build(body)
    addButton(button)
}

/***
 * @param title Текст кнопки.
 * @param payload Произвольный JSON-объект, который Яндекс Диалоги должны отправить обработчику, если данная кнопка будет нажата.
 * @param url URL, который должна открывать кнопка.
 * @param hide Признак того, что кнопку нужно убрать после следующей реплики пользователя.
 * false — кнопка должна оставаться активной (значение по умолчанию);
 * true — кнопку нужно скрывать после нажатия.
 */
@Serializable
data class Button internal constructor(
    val title: String,
    val payload: Map<String, String>?,
    val url: String?,
    val hide: Boolean
) {
    @AliceResponseDsl
    class Builder {
        lateinit var title: String
        var url: String? = null
        var hide: Boolean = true
        var payload: Map<String, String>? = null

        internal fun build(body: Builder.() -> Unit): Button {
            body()
            return Button(
                title = title,
                url = url,
                hide = hide,
                payload = payload
            )
        }
    }
}
