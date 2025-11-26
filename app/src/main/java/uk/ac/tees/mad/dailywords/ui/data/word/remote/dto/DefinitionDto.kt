package uk.ac.tees.mad.dailywords.ui.data.word.remote.dto

import uk.ac.tees.mad.dailywords.ui.domain.word.Definition

data class DefinitionDto(
    val antonyms: List<String>,
    val definition: String,
    val example: String?,
    val synonyms: List<String>
) {
    fun toDefinition(): Definition {
        return Definition(
            definition = definition,
            example = example
        )
    }
}