package uk.ac.tees.mad.dailywords.ui.data.word.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [WordEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class WordDatabase : RoomDatabase() {

    abstract val wordDao: WordDao

    companion object {
        const val DATABASE_NAME = "word_db"
    }
}