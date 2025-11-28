package uk.ac.tees.mad.dailywords.ui.data.word.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class WordDto(
    val word: String,
    val phonetic: String? = null,
    val phonetics: List<PhoneticDto>,
    val meanings: List<MeaningDto>,
    val license: LicenseDto,
    val sourceUrls: List<String>
)

@Serializable
data class PhoneticDto(
    val text: String? = null,
    val audio: String? = null,
    val sourceUrl: String? = null,
    val license: LicenseDto? = null
)

@Serializable
data class MeaningDto(
    val partOfSpeech: String,
    val definitions: List<DefinitionDto>,
    val synonyms: List<String>,
    val antonyms: List<String>
)

@Serializable
data class DefinitionDto(
    val definition: String,
    val example: String? = null
)

@Serializable
data class LicenseDto(
    val name: String,
    val url: String
)

@Serializable
data class RandomWordDto(
    val word: List<String>
)
