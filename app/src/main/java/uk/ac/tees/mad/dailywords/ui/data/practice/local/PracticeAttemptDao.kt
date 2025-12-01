package uk.ac.tees.mad.dailywords.ui.data.practice.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PracticeAttemptDao {

    @Insert
    suspend fun insertAttempt(attempt: PracticeAttemptEntity)

    @Query("SELECT * FROM practice_attempts WHERE word = :word ORDER BY timestamp DESC")
    fun getAttemptsForWord(word: String): Flow<List<PracticeAttemptEntity>>
}
