package uk.ac.tees.mad.dailywords.ui.data.util

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import uk.ac.tees.mad.dailywords.ui.domain.util.JsonParser

class KotlinxJsonParser(private val json: Json) : JsonParser {
    override fun <T> fromJson(jsonString: String, serializer: KSerializer<T>): T? {
        return json.decodeFromString(serializer, jsonString)
    }

    override fun <T> toJson(obj: T, serializer: KSerializer<T>): String? {
        return json.encodeToString(serializer, obj)
    }
}