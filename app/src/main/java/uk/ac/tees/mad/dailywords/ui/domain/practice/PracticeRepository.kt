package uk.ac.tees.mad.dailywords.ui.domain.practice

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.dailywords.ui.data.practice.local.PracticeAttemptEntity

interface PracticeRepository {

    suspend fun insertAttempt(attempt: PracticeAttemptEntity)

    fun getAttemptsForWord(word: String): Flow<List<PracticeAttemptEntity>>
}