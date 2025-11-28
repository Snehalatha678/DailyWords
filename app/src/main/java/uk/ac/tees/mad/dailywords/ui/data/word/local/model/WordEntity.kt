package uk.ac.tees.mad.dailywords.ui.data.word.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WordEntity(
    @PrimaryKey val word: String,
    val wordData: String,
    val isBookmarked: Boolean = false
)