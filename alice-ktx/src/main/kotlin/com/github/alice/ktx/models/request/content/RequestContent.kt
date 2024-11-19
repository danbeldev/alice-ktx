package com.github.alice.ktx.models.request.content

import com.github.alice.ktx.models.request.content.error.RequestError
import com.github.alice.ktx.models.request.content.show.ShowType
import com.github.alice.ktx.models.request.nlu.Nlu
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * @param command Нормализованный текст запроса.
 * @param originalUtterance Полный текст пользовательского запроса, максимум 1024 символа.
 * @param type Тип ввода.
 * @param payload
 * @param markup Формальные характеристики реплики, которые удалось выделить Яндекс Диалогам. Свойство отсутствует, если ни одно из вложенных свойств не применимо.
 * @param nlu Слова и именованные сущности, которые Диалоги извлекли из запроса пользователя.
 * @param tokens Обозначение начала и конца именованной сущности в массиве слов. Нумерация слов в массиве начинается с 0.
 * @param showType Тип шоу.
 * @param purchaseRequestId UUID-идентификатор заказа, переданный при запуске сценария оплаты.
 * @param purchaseToken UUIDv4-идентификатор транзакции.
 * @param orderId Идентификатор заказа. Остается неизменным для всех платежей в рамках подписки.
 * @param purchaseTimestamp Время совершения оплаты. Равно количеству миллисекунд, прошедших с 01.01.1970 00:00:00 UTC.
 * @param purchasePayload JSON-объект, полученный при запуске сценария оплаты.
 * @param signedData Строка, использованная для подписи. Формат: purchase_request_id=[value]&purchase_token=[value]&order_id=[value]&purchase_timestamp=[value].
 * @param signature Подпись, полученная путем хеширования значения поля signed_data с помощью алгоритма SHA256 с RSA и приватного ключа. Закодирована в base64.
 */
@Serializable
data class RequestContent(

    val command: String? = null,
    @SerialName("original_utterance")
    val originalUtterance: String? = null,
    val type: RequestContentType,
    val markup: Markup? = null,
    val nlu: Nlu = Nlu(),
    val tokens: Map<String, String> = emptyMap(),

    val payload: Map<String, String> = emptyMap(),

    @SerialName("show_type")
    val showType: ShowType? = null,

    @SerialName("purchase_request_id")
    val purchaseRequestId: String? = null,
    @SerialName("purchase_token")
    val purchaseToken: String? = null,
    @SerialName("order_id")
    val orderId: String? = null,
    @SerialName("purchase_timestamp")
    val purchaseTimestamp: Long? = null,
    @SerialName("purchase_payload")
    val purchasePayload: Map<String, String> = emptyMap(),
    @SerialName("signed_data")
    val signedData: String? = null,
    @SerialName("signature")
    val signature: String? = null,

    val error: RequestError? = null
)
