package uk.ac.tees.mad.dailywords.ui.domain.word.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult
import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository

class GetRandomWord(private val repository: WordRepository) {

    operator fun invoke(): Flow<HttpResult<Word>> = flow {
        emit(HttpResult.Loading)
        val allWordsResult = repository.getWord("")
        allWordsResult.collect { result ->
            when (result) {
                is HttpResult.Success -> {
                    val randomWord = result.data?.randomOrNull()
                    if (randomWord != null) {
                        emit(HttpResult.Success(randomWord))
                    } else {
                        emit(HttpResult.Failure(uk.ac.tees.mad.dailywords.ui.domain.util.DataError.Remote.NOT_FOUND))
                    }
                }
                is HttpResult.Failure -> emit(result)
                is HttpResult.Loading -> {}
            }
        }
    }
}