package uk.ac.tees.mad.dailywords.ui.domain.word.usecase

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult
import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository

class GetRandomWord(
    private val repository: WordRepository
) {

    operator fun invoke(): Flow<HttpResult<Word>> {
        return repository.getRandomWord()
    }
}