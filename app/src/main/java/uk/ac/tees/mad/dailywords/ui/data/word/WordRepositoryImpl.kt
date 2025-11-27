package uk.ac.tees.mad.dailywords.ui.data.word

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uk.ac.tees.mad.dailywords.ui.data.word.local.WordDao
import uk.ac.tees.mad.dailywords.ui.data.word.remote.WordApi
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult
import uk.ac.tees.mad.dailywords.ui.domain.util.httpResult
import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository

class WordRepositoryImpl(
    private val api: WordApi,
    private val dao: WordDao
) : WordRepository {

    override fun getWord(word: String): Flow<HttpResult<List<Word>>> = flow {
        emit(HttpResult.Loading)

        val localWords = dao.getWords(word).map { it.toWord() }
        if (localWords.isNotEmpty()) {
            emit(HttpResult.Success(localWords))
            return@flow
        }

        if (word.isBlank()) {
            when (val remoteResult = httpResult { api.getWord(word) }) {
                is HttpResult.Success -> {
                    remoteResult.data.let { remoteWords ->
                        dao.deleteAllWords()
                        dao.insertWords(remoteWords.map { it.toWordEntity() })
                        emit(HttpResult.Success(dao.getWords("").map { it.toWord() }))
                    }
                }
                is HttpResult.Failure -> {
                    emit(remoteResult)
                }
                else -> {}
            }
        }
    }
}