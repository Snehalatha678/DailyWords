package uk.ac.tees.mad.dailywords.ui.data.practice.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "practice_attempts")
data class PracticeAttemptEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val word: String,
    val timestamp: Long,
    val result: String,
    val accuracy: Float
)
