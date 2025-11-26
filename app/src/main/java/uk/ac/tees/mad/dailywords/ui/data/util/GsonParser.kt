package uk.ac.tees.mad.dailywords.ui.data.util

import com.google.gson.Gson
import uk.ac.tees.mad.dailywords.ui.domain.util.JsonParser
import java.lang.reflect.Type

class GsonParser(private val gson: Gson) : JsonParser {
    override fun <T> fromJson(json: String, type: Type): T? {
        return gson.fromJson(json, type)
    }

    override fun <T> toJson(obj: T, type: Type): String? {
        return gson.toJson(obj, type)
    }
}