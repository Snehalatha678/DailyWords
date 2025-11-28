package uk.ac.tees.mad.dailywords.ui.domain.util

import kotlinx.serialization.KSerializer

interface JsonParser {
    fun <T> fromJson(json: String, serializer: KSerializer<T>): T?
    fun <T> toJson(obj: T, serializer: KSerializer<T>): String?
}