package uk.ac.tees.mad.dailywords.ui.domain.word

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Word(
    val meanings: List<Meaning>,
    val phonetic: String?,
    val sourceUrls: List<String>,
    val word: String,
    @Transient val isBookmarked: Boolean = false
)

@Serializable
data class Meaning(
    val partOfSpeech: String,
    val definitions: List<Definition>,
    val synonyms: List<String>,
    val antonyms: List<String>
)

@Serializable
data class Definition(
    val definition: String,
    val example: String?
)
