package uk.ac.tees.mad.dailywords.ui.domain.word.usecase

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository

class GetBookmarkedWords(private val repository: WordRepository) {

    operator fun invoke(): Flow<List<Word>> {
        return repository.getBookmarkedWords()
    }
}