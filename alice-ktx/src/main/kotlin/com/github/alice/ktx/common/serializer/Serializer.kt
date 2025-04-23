package com.github.alice.ktx.common.serializer

import kotlin.reflect.KClass

/**
 * Интерфейс абстрактного сериализатора, позволяющий преобразовывать объекты в строку и обратно
 */
interface Serializer {

    /**
     * Сериализует объект [value] в строковое представление.
     *
     * @param T Тип сериализуемого объекта.
     * @param value Объект, который необходимо сериализовать.
     * @param serializerClass Класс, описывающий тип объекта для сериализации.
     * @return Строковое представление объекта.
     */
    fun <T : Any> serialize(value: T, serializerClass: KClass<T>): String

    /**
     * Десериализует строку [input] в объект указанного типа [serializerClass].
     *
     * @param T Тип объекта, который должен быть восстановлен.
     * @param input Строка, содержащая сериализованные данные.
     * @param serializerClass Класс, описывающий тип объекта для десериализации.
     * @return Объект, восстановленный из строки.
     */
    fun <T : Any> deserialize(input: String, serializerClass: KClass<T>): T
}
