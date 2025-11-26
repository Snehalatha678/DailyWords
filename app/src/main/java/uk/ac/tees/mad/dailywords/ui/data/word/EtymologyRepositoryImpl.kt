package uk.ac.tees.mad.dailywords.ui.data.word

import android.app.Application
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import uk.ac.tees.mad.dailywords.ui.domain.word.Etymology
import uk.ac.tees.mad.dailywords.ui.domain.word.EtymologyRepository
import java.io.InputStreamReader

class EtymologyRepositoryImpl(private val app: Application) : EtymologyRepository {
    override suspend fun getEtymologies(): List<Etymology> {
        val json = app.assets.open("etymologies.json").use { 
            InputStreamReader(it).readText()
        }
        val type = object : TypeToken<List<Etymology>>() {}.type
        return Gson().fromJson(json, type) ?: emptyList()
    }
}