package uk.ac.tees.mad.dailywords.ui.data.word.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.dailywords.ui.data.word.local.model.WordEntity

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWord(word: WordEntity)

    @Query("SELECT * FROM wordentity WHERE word = :word")
    suspend fun getWord(word: String): WordEntity?

    @Query("SELECT * FROM wordentity WHERE isBookmarked = 1")
    fun getBookmarkedWords(): Flow<List<WordEntity>>

    @Query("SELECT * FROM wordentity")
    suspend fun getAllWordsSync(): List<WordEntity>
}