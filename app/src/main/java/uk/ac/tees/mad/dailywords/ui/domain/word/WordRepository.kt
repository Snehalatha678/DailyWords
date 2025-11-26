package uk.ac.tees.mad.dailywords.ui.domain.word

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult

interface WordRepository {
    fun getWord(word: String): Flow<HttpResult<List<Word>>>
}