package uk.ac.tees.mad.dailywords.ui.data.word.remote.dto

import uk.ac.tees.mad.dailywords.ui.domain.word.Definition
import uk.ac.tees.mad.dailywords.ui.domain.word.Meaning
import uk.ac.tees.mad.dailywords.ui.domain.word.Word

fun WordDto.toWord(): Word {
    return Word(
        word = word,
        phonetic = phonetic,
        sourceUrls = sourceUrls,
        meanings = meanings.map { it.toMeaning() }
    )
}

fun MeaningDto.toMeaning(): Meaning {
    return Meaning(
        partOfSpeech = partOfSpeech,
        definitions = definitions.map { it.toDefinition() },
        synonyms = synonyms,
        antonyms = antonyms
    )
}

fun DefinitionDto.toDefinition(): Definition {
    return Definition(
        definition = definition,
        example = example
    )
}