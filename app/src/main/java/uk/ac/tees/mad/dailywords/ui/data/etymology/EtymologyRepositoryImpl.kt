package uk.ac.tees.mad.dailywords.ui.data.etymology

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import uk.ac.tees.mad.dailywords.ui.data.etymology.remote.EtymologyApi
import uk.ac.tees.mad.dailywords.ui.domain.etymology.Etymology
import uk.ac.tees.mad.dailywords.ui.domain.etymology.EtymologyRepository
import uk.ac.tees.mad.dailywords.ui.domain.util.DataError
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult

class EtymologyRepositoryImpl(private val api: EtymologyApi) : EtymologyRepository {

    override fun getEtymology(word: String): Flow<HttpResult<Etymology>> = flow {
        emit(HttpResult.Loading)
        try {
            val result = api.getEtymology(word)
            if (result != null) {
                emit(HttpResult.Success(result))
            } else {
                emit(HttpResult.Failure(DataError.Remote.NOT_FOUND))
            }
        } catch (e: Exception) {
            emit(HttpResult.Failure(DataError.Remote.UNKNOWN))
        }
    }
}