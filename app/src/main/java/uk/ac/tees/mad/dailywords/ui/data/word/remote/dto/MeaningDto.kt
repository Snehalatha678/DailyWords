package uk.ac.tees.mad.dailywords.ui.data.word.remote.dto

import uk.ac.tees.mad.dailywords.ui.domain.word.Meaning

data class MeaningDto(
    val antonyms: List<String>,
    val definitions: List<DefinitionDto>,
    val partOfSpeech: String,
    val synonyms: List<String>
) {
    fun toMeaning(): Meaning {
        return Meaning(
            definitions = definitions.map { it.toDefinition() },
            partOfSpeech = partOfSpeech,
        )
    }
}