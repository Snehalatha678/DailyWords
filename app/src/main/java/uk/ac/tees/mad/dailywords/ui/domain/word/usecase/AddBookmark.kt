package uk.ac.tees.mad.dailywords.ui.domain.word.usecase

import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.domain.word.WordRepository

class AddBookmark(private val repository: WordRepository) {

    suspend operator fun invoke(word: Word) {
        repository.addBookmark(word)
    }
}