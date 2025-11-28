package uk.ac.tees.mad.dailywords.ui.data.etymology.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import uk.ac.tees.mad.dailywords.ui.domain.etymology.Etymology

class EtymologyApi(private val client: HttpClient) {

    suspend fun getEtymology(word: String): Etymology? {
        return try {
            client.get("https://api.etymonline.com/v1/terms/$word").body()
        } catch (e: Exception) {
            null
        }
    }
}