package com.github.alice.ktx.common.serializers

import com.github.alice.ktx.models.request.nlu.NluEntity
import com.github.alice.ktx.models.request.nlu.NluEntityToken
import com.github.alice.ktx.models.request.nlu.NluEntityType
import com.github.alice.ktx.models.request.nlu.NluValue
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

internal object NluEntitySerializer : KSerializer<NluEntity> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("NluEntity") {
        element<NluEntityToken>("tokens")
        element<NluEntityType>("type")
        element<JsonElement>("value")
    }

    override fun serialize(encoder: Encoder, value: NluEntity) {
        val jsonEncoder = encoder as? JsonEncoder ?: error("Only Json is supported")
        val json = buildJsonObject {
            put("tokens", jsonEncoder.json.encodeToJsonElement(value.tokens))
            put("type", value.type.serialName())
            put(
                "value",
                when (val nluValue = value.value) {
                    is NluValue.GeoValue -> jsonEncoder.json.encodeToJsonElement(nluValue)
                    is NluValue.FioValue -> jsonEncoder.json.encodeToJsonElement(nluValue)
                    is NluValue.DateTimeValue -> jsonEncoder.json.encodeToJsonElement(nluValue)
                    is NluValue.NumberValue -> JsonPrimitive(nluValue.number)
                }
            )
        }
        jsonEncoder.encodeJsonElement(json)
    }

    override fun deserialize(decoder: Decoder): NluEntity {
        val jsonDecoder = decoder as? JsonDecoder ?: error("Only Json is supported")
        val jsonObject = jsonDecoder.decodeJsonElement().jsonObject

        val tokensJson = jsonObject["tokens"] ?: error("Missing 'tokens'")
        val typeJson = jsonObject["type"]?.jsonPrimitive?.content ?: error("Missing 'type'")
        val valueJson = jsonObject["value"] ?: error("Missing 'value'")

        val nluEntityToken = jsonDecoder.json.decodeFromJsonElement<NluEntityToken>(tokensJson)
        val type = fromSerialName(typeJson)

        val nluValue: NluValue = when (type) {
            NluEntityType.GEO -> jsonDecoder.json.decodeFromJsonElement<NluValue.GeoValue>(valueJson)
            NluEntityType.FIO -> jsonDecoder.json.decodeFromJsonElement<NluValue.FioValue>(valueJson)
            NluEntityType.DATETIME -> jsonDecoder.json.decodeFromJsonElement<NluValue.DateTimeValue>(valueJson)
            NluEntityType.NUMBER -> NluValue.NumberValue(valueJson.jsonPrimitive.float)
        }

        return NluEntity(nluEntityToken, type, nluValue)
    }
}

private fun NluEntityType.serialName(): String {
    return when (this) {
        NluEntityType.FIO -> "YANDEX.FIO"
        NluEntityType.GEO -> "YANDEX.GEO"
        NluEntityType.DATETIME -> "YANDEX.DATETIME"
        NluEntityType.NUMBER -> "YANDEX.NUMBER"
    }
}

private fun fromSerialName(serialName: String): NluEntityType {
    return when (serialName) {
        "YANDEX.FIO" -> NluEntityType.FIO
        "YANDEX.GEO" -> NluEntityType.GEO
        "YANDEX.DATETIME" -> NluEntityType.DATETIME
        "YANDEX.NUMBER" -> NluEntityType.NUMBER
        else -> throw IllegalArgumentException("Unknown type: $serialName")
    }
}