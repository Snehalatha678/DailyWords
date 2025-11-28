package uk.ac.tees.mad.dailywords.ui.data.word.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.tees.mad.dailywords.ui.data.word.local.model.WordEntity

@Database(
    entities = [WordEntity::class],
    version = 2
)
@TypeConverters(Converters::class)
abstract class WordDatabase : RoomDatabase() {

    abstract val wordDao: WordDao

    companion object {
        const val DATABASE_NAME = "word_db"
    }
}