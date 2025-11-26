package uk.ac.tees.mad.dailywords.ui.data.word.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWords(words: List<WordEntity>)

    @Query("DELETE FROM wordentity WHERE word IN(:words)")
    suspend fun deleteWords(words: List<String>)

    @Query("SELECT * FROM wordentity WHERE word LIKE '%' || :word || '%'")
    suspend fun getWords(word: String): List<WordEntity>

    @Query("DELETE FROM wordentity")
    suspend fun deleteAllWords()
}