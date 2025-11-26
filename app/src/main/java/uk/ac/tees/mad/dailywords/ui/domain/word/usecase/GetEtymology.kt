package uk.ac.tees.mad.dailywords.ui.domain.word.usecase

import uk.ac.tees.mad.dailywords.ui.domain.word.Etymology
import uk.ac.tees.mad.dailywords.ui.domain.word.EtymologyRepository

class GetEtymology(private val repository: EtymologyRepository) {
    suspend operator fun invoke(word: String): Etymology? {
        return repository.getEtymologies().find { it.word.equals(word, ignoreCase = true) }
    }
}