package com.github.alice.ktx.models.request.nlu

import kotlinx.serialization.SerialName

enum class NluEntityType {
    @SerialName("YANDEX.FIO")
    FIO,

    @SerialName("YANDEX.GEO")
    GEO,

    @SerialName("YANDEX.DATETIME")
    DATETIME,

    @SerialName("NUMBER")
    NUMBER
}