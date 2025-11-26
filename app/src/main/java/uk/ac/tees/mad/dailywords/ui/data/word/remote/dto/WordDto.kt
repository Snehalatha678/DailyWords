package uk.ac.tees.mad.dailywords.ui.data.word.remote.dto

import uk.ac.tees.mad.dailywords.ui.data.word.local.WordEntity

data class WordDto(
    val meanings: List<MeaningDto>,
    val phonetic: String?,
    val phonetics: List<PhoneticDto>,
    val sourceUrls: List<String>,
    val word: String,
    val license: LicenseDto
) {
    fun toWordEntity(): WordEntity {
        return WordEntity(
            meanings = meanings.map { it.toMeaning() },
            phonetic = phonetic ?: phonetics.find { it.text != null }?.text ?: "",
            sourceUrls = sourceUrls,
            word = word
        )
    }
}