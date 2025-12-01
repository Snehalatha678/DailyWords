package uk.ac.tees.mad.dailywords.ui.data.word.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import uk.ac.tees.mad.dailywords.ui.data.practice.local.PracticeAttemptDao
import uk.ac.tees.mad.dailywords.ui.data.practice.local.PracticeAttemptEntity
import uk.ac.tees.mad.dailywords.ui.data.word.local.model.WordEntity

@Database(
    entities = [WordEntity::class, PracticeAttemptEntity::class],
    version = 3
)
@TypeConverters(Converters::class)
abstract class WordDatabase : RoomDatabase() {

    abstract val wordDao: WordDao
    abstract val practiceAttemptDao: PracticeAttemptDao

    companion object {
        const val DATABASE_NAME = "word_db"
    }
}