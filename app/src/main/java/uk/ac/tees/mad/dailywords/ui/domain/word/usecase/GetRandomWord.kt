package uk.ac.tees.mad.dailywords.ui.domain.word.usecase

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.header
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uk.ac.tees.mad.dailywords.ui.data.word.remote.WordApi
import uk.ac.tees.mad.dailywords.ui.data.word.remote.dto.RandomWordDto
import uk.ac.tees.mad.dailywords.ui.domain.util.DataError
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult
import uk.ac.tees.mad.dailywords.ui.domain.util.httpResult
import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository

class GetRandomWord(
    private val repository: WordRepository,
    private val wordApi: WordApi
) {

    operator fun invoke(): Flow<HttpResult<Word>> = flow {
        emit(HttpResult.Loading)

        when (val randomWordResult = httpResult { wordApi.getRandomWord() }) {
            is HttpResult.Success -> {
                val randomWord = randomWordResult.data.word.firstOrNull()
                if (randomWord == null) {
                    emit(HttpResult.Failure(DataError.Remote.NOT_FOUND))
                    return@flow
                }

                repository.getWord(randomWord).collect { result ->
                    when (result) {
                        is HttpResult.Success -> {
                            val wordData = result.data.firstOrNull()
                            if (wordData != null) {
                                emit(HttpResult.Success(wordData))
                            } else {
                                emit(HttpResult.Failure(DataError.Remote.NOT_FOUND))
                            }
                        }
                        is HttpResult.Failure -> {
                            emit(result)
                        }
                        is HttpResult.Loading -> {}
                    }
                }
            }
            is HttpResult.Failure -> {
                emit(randomWordResult)
            }
            else -> {}
        }
    }
}