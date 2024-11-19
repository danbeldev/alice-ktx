package com.github.alice.ktx.models.request.nlu

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed class NluValue {

    @Serializable
    @SerialName("YANDEX.FIO")
    data class FioValue(
        @SerialName("first_name")
        val firstName: String? = null,
        @SerialName("patronymic_name")
        val patronymicName: String? = null,
        @SerialName("last_name")
        val lastName: String? = null
    ) : NluValue()

    @Serializable
    @SerialName("YANDEX.GEO")
    data class GeoValue(
        val country: String? = null,
        val city: String? = null,
        val street: String? = null,
        @SerialName("house_number")
        val houseNumber: String? = null,
        val airport: String? = null
    ) : NluValue()

    @Serializable
    @SerialName("YANDEX.DATETIME")
    data class DateTimeValue(
        val year: Int? = null,
        val month: Int? = null,
        val day: Int? = null,
        val hour: Int? = null,
        val minute: Int? = null,
        @SerialName("year_is_relative")
        val yearIsRelative: Boolean? = null,
        @SerialName("month_is_relative")
        val monthIsRelative: Boolean? = null,
        @SerialName("day_is_relative")
        val dayIsRelative: Boolean? = null,
        @SerialName("hour_is_relative")
        val hourIsRelative: Boolean? = null,
        @SerialName("minute_is_relative")
        val minuteIsRelative: Boolean? = null
    ) : NluValue()

    @Serializable
    @SerialName("YANDEX.NUMBER")
    data class NumberValue(val number: Float) : NluValue()
}