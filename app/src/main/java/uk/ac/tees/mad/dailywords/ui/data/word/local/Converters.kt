package uk.ac.tees.mad.dailywords.ui.data.word.local

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import uk.ac.tees.mad.dailywords.ui.domain.util.JsonParser
import uk.ac.tees.mad.dailywords.ui.domain.word.Meaning

@ProvidedTypeConverter
class Converters(
    private val jsonParser: JsonParser
) {
    @TypeConverter
    fun fromMeaningsJson(json: String): List<Meaning> {
        return jsonParser.fromJson<ArrayList<Meaning>>(
            json,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toMeaningsJson(meanings: List<Meaning>): String {
        return jsonParser.toJson(
            meanings,
            object : TypeToken<ArrayList<Meaning>>() {}.type
        ) ?: "[]"
    }

    @TypeConverter
    fun fromSourceUrlsJson(json: String): List<String> {
        return jsonParser.fromJson<List<String>>(
            json,
            object : TypeToken<List<String>>() {}.type
        ) ?: emptyList()
    }

    @TypeConverter
    fun toSourceUrlsJson(sourceUrls: List<String>): String {
        return jsonParser.toJson(
            sourceUrls,
            object : TypeToken<List<String>>() {}.type
        ) ?: "[]"
    }
}