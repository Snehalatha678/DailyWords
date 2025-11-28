package uk.ac.tees.mad.dailywords.ui.data.word.local

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import uk.ac.tees.mad.dailywords.ui.domain.word.Meaning

class Converters {

    private val json = Json { ignoreUnknownKeys = true }

    @TypeConverter
    fun fromMeaningsJson(jsonString: String): List<Meaning> {
        return json.decodeFromString(ListSerializer(Meaning.serializer()), jsonString)
    }

    @TypeConverter
    fun toMeaningsJson(meanings: List<Meaning>): String {
        return json.encodeToString(ListSerializer(Meaning.serializer()), meanings)
    }
}