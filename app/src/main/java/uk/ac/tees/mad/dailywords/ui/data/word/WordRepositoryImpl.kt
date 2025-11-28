package uk.ac.tees.mad.dailywords.ui.data.word

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.dailywords.ui.data.word.local.WordDao
import uk.ac.tees.mad.dailywords.ui.data.word.local.model.toWord
import uk.ac.tees.mad.dailywords.ui.data.word.local.model.toWordEntity
import uk.ac.tees.mad.dailywords.ui.data.word.remote.WordApi
import uk.ac.tees.mad.dailywords.ui.data.word.remote.dto.toWord
import uk.ac.tees.mad.dailywords.ui.domain.util.DataError
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult
import uk.ac.tees.mad.dailywords.ui.domain.util.NetworkManager
import uk.ac.tees.mad.dailywords.ui.domain.util.httpResult
import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository

class WordRepositoryImpl(
    private val api: WordApi,
    private val dao: WordDao,
    private val networkManager: NetworkManager
) : WordRepository {

    override fun getWord(word: String): Flow<HttpResult<List<Word>>> = flow {
        emit(HttpResult.Loading)

        val localWord = dao.getWord(word)?.toWord()
        if (localWord != null) {
            emit(HttpResult.Success(listOf(localWord)))
        }

        if (networkManager.isConnected()) {
            when (val remoteResult = httpResult { api.getWord(word).map { it.toWord() } }) {
                is HttpResult.Success -> {
                    val remoteWord = remoteResult.data
                    if (remoteWord.isNotEmpty()) {
                        dao.insertWord(remoteWord.first().toWordEntity())
                        emit(HttpResult.Success(remoteWord))
                    }
                }
                is HttpResult.Failure -> {
                    if (localWord == null) {
                        emit(remoteResult)
                    }
                }
                else -> {}
            }
        } else {
            if (localWord == null) {
                emit(HttpResult.Failure(DataError.Remote.NO_INTERNET))
            }
        }
    }

    override suspend fun addBookmark(word: Word) {
        dao.insertWord(word.toWordEntity().copy(isBookmarked = true))
    }

    override suspend fun removeBookmark(word: Word) {
        dao.insertWord(word.toWordEntity().copy(isBookmarked = false))
    }

    override fun getBookmarkedWords(): Flow<List<Word>> {
        return dao.getBookmarkedWords().map { list ->
            list.map { it.toWord() }
        }
    }
}