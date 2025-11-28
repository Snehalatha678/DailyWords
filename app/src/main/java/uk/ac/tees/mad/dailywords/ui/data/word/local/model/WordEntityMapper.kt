package uk.ac.tees.mad.dailywords.ui.data.word.local.model

import kotlinx.serialization.json.Json
import uk.ac.tees.mad.dailywords.ui.domain.word.Word

fun WordEntity.toWord(): Word {
    return Json.decodeFromString(Word.serializer(), this.wordData)
}

fun Word.toWordEntity(): WordEntity {
    return WordEntity(
        word = this.word,
        wordData = Json.encodeToString(Word.serializer(), this)
    )
}