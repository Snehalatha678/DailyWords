package uk.ac.tees.mad.dailywords.ui.data.word.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import uk.ac.tees.mad.dailywords.ui.domain.word.Meaning
import uk.ac.tees.mad.dailywords.ui.domain.word.Word

@Entity
data class WordEntity(
    val meanings: List<Meaning>,
    val phonetic: String,
    val sourceUrls: List<String>,
    @PrimaryKey val word: String
) {
    fun toWord(): Word {
        return Word(
            meanings = meanings,
            phonetic = phonetic,
            sourceUrls = sourceUrls,
            word = word
        )
    }
}