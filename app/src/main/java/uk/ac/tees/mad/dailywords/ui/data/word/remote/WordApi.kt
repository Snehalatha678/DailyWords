package uk.ac.tees.mad.dailywords.ui.data.word.remote

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.serialization.JsonConvertException
import uk.ac.tees.mad.dailywords.ui.data.word.remote.dto.RandomWordDto
import uk.ac.tees.mad.dailywords.ui.data.word.remote.dto.WordDto

class WordApi(private val client: HttpClient) {

    private val apiKey = "knZiOBpy32vjvH9dnoZY3w==A5roCC29d86gCkPC"

    suspend fun getWord(word: String): List<WordDto> {
        return try {
            client.get("https://api.dictionaryapi.dev/api/v2/entries/en/$word").body()
        } catch (e: JsonConvertException) {
            emptyList()
        }
    }

    suspend fun getRandomWord(): RandomWordDto {
        return client.get("https://api.api-ninjas.com/v1/randomword") {
            header("X-Api-Key", apiKey)
        }.body()
    }
}