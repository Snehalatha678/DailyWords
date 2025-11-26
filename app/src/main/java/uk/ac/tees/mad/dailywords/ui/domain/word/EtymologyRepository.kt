package uk.ac.tees.mad.dailywords.ui.domain.word

interface EtymologyRepository {
    suspend fun getEtymologies(): List<Etymology>
}