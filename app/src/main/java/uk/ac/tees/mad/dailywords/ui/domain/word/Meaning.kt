package uk.ac.tees.mad.dailywords.ui.domain.word

data class Meaning(
    val definitions: List<Definition>,
    val partOfSpeech: String
)