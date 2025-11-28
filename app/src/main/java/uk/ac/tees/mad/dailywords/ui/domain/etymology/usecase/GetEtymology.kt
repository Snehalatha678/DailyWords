package uk.ac.tees.mad.dailywords.ui.domain.etymology.usecase

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.dailywords.ui.domain.etymology.Etymology
import uk.ac.tees.mad.dailywords.ui.domain.etymology.EtymologyRepository
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult

class GetEtymology(private val repository: EtymologyRepository) {

    operator fun invoke(word: String): Flow<HttpResult<Etymology>> {
        return repository.getEtymology(word)
    }
}