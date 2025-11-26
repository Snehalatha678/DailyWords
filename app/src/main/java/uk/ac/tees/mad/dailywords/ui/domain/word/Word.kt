package uk.ac.tees.mad.dailywords.ui.domain.word

data class Word(
    val meanings: List<Meaning>,
    val phonetic: String,
    val sourceUrls: List<String>,
    val word: String
)