package uk.ac.tees.mad.dailywords.ui.data.word.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import uk.ac.tees.mad.dailywords.ui.data.word.remote.dto.WordDto

class WordApi(private val client: HttpClient) {

    suspend fun getWord(word: String): List<WordDto> {
        return client.get("api/v2/entries/en/$word").body()
    }

    companion object {
        const val BASE_URL = "https://api.dictionaryapi.dev/"
    }
}