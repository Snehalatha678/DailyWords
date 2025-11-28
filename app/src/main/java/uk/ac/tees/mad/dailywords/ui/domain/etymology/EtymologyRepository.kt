package uk.ac.tees.mad.dailywords.ui.domain.etymology

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult

interface EtymologyRepository {
    fun getEtymology(word: String): Flow<HttpResult<Etymology>>
}