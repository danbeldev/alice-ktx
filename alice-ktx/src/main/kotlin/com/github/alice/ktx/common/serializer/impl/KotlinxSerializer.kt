package com.github.alice.ktx.common.serializer.impl

import com.github.alice.ktx.common.AliceDsl
import com.github.alice.ktx.common.serializer.Serializer
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import kotlin.reflect.KClass

@AliceDsl
fun kotlinxSerializer(
    body: KotlinxSerializer.Builder.() -> Unit = {}
): KotlinxSerializer {
    return KotlinxSerializer.Builder().apply(body).build()
}

internal val defaultJson = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

@OptIn(InternalSerializationApi::class)
class KotlinxSerializer internal constructor(
    private val json: Json
): Serializer {

    class Builder {
        var json = defaultJson

        fun build(): KotlinxSerializer {
            return KotlinxSerializer(
                json = json
            )
        }
    }

    override fun <T : Any> serialize(value: T, serializerClass: KClass<T>): String {
        return json.encodeToString(serializerClass.serializer(), value)
    }

    override fun <T : Any> deserialize(input: String, serializerClass: KClass<T>): T {
        return json.decodeFromString(serializerClass.serializer(), input)
    }
}