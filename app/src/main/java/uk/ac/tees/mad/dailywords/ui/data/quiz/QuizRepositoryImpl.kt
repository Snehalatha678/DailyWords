package uk.ac.tees.mad.dailywords.ui.data.quiz

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import uk.ac.tees.mad.dailywords.ui.data.word.local.WordDao
import uk.ac.tees.mad.dailywords.ui.data.word.local.model.toWord
import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuestionType
import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuizQuestion
import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuizRepository
import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuizResult
import uk.ac.tees.mad.dailywords.ui.domain.util.DataError
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult
import uk.ac.tees.mad.dailywords.ui.domain.word.Word

class QuizRepositoryImpl(
    private val wordDao: WordDao
) : QuizRepository {

    override fun generateQuiz(): Flow<HttpResult<List<QuizQuestion>>> = flow {
        emit(HttpResult.Loading)
        try {
            val learnedWords = wordDao.getAllWordsSync().map { it.toWord() }
            if (learnedWords.size < 4) {
                emit(HttpResult.Failure(DataError.Local.DISK_FULL)) // Using a generic error for now
                return@flow
            }

            val quizQuestions = mutableListOf<QuizQuestion>()
            val shuffledWords = learnedWords.shuffled().take(5)

            shuffledWords.forEachIndexed { index, word ->
                val questionType = QuestionType.values().random()
                val question = when (questionType) {
                    QuestionType.MEANING -> generateMeaningQuestion(index, word, learnedWords)
                    QuestionType.SPELLING -> generateSpellingQuestion(index, word)
                    QuestionType.USAGE -> generateUsageQuestion(index, word, learnedWords)
                }
                quizQuestions.add(question)
            }
            emit(HttpResult.Success(quizQuestions))
        } catch (e: Exception) {
             emit(HttpResult.Failure(DataError.Local.UNKNOWN))
        }
    }

    override suspend fun saveQuizResult(score: Int, totalQuestions: Int) {
        // Save to local DB or Firestore
        // For now, we can just log it or save to shared preferences if needed
        // In a real app, we'd have a QuizResultDao
    }

    override fun getQuizHistory(): Flow<List<QuizResult>> = flow {
        // Return empty list for now until we implement persistence
        emit(emptyList())
    }

    private fun generateMeaningQuestion(index: Int, word: Word, allWords: List<Word>): QuizQuestion {
        val correctMeaning = word.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition ?: "Meaning not available"
        val distractors = allWords.filter { it.word != word.word }
            .shuffled()
            .take(3)
            .map { it.meanings.firstOrNull()?.definitions?.firstOrNull()?.definition ?: "Distractor" }
        
        val options = (distractors + correctMeaning).shuffled()
        val correctIndex = options.indexOf(correctMeaning)

        return QuizQuestion(
            id = index.toString(),
            questionText = "What is the meaning of '${word.word}'?",
            options = options,
            correctAnswerIndex = correctIndex,
            type = QuestionType.MEANING,
            explanation = "The correct meaning is: $correctMeaning"
        )
    }

    private fun generateSpellingQuestion(index: Int, word: Word): QuizQuestion {
        val correctSpelling = word.word
        val misspelled1 = modifySpelling(correctSpelling)
        val misspelled2 = modifySpelling(correctSpelling)
        val misspelled3 = modifySpelling(correctSpelling)

        val options = listOf(correctSpelling, misspelled1, misspelled2, misspelled3).shuffled()
        val correctIndex = options.indexOf(correctSpelling)

        return QuizQuestion(
            id = index.toString(),
            questionText = "Select the correct spelling:",
            options = options,
            correctAnswerIndex = correctIndex,
            type = QuestionType.SPELLING,
            explanation = "The correct spelling is '$correctSpelling'."
        )
    }
    
    private fun modifySpelling(word: String): String {
        // Simple logic to misspell words
        if (word.length > 3) {
            val charArray = word.toCharArray()
            val pos = (1 until word.length - 1).random()
            val temp = charArray[pos]
            charArray[pos] = charArray[pos + 1]
            charArray[pos + 1] = temp
            return String(charArray)
        }
        return word + "s"
    }

    private fun generateUsageQuestion(index: Int, word: Word, allWords: List<Word>): QuizQuestion {
         val example = word.meanings.firstOrNull()?.definitions?.firstOrNull { it.example != null }?.example
         
         if (example != null) {
             val questionText = example.replace(word.word, "______", ignoreCase = true)
             val distractors = allWords.filter { it.word != word.word }.shuffled().take(3).map { it.word }
             val options = (distractors + word.word).shuffled()
             val correctIndex = options.indexOf(word.word)
             
             return QuizQuestion(
                id = index.toString(),
                questionText = "Complete the sentence:\n$questionText",
                options = options,
                correctAnswerIndex = correctIndex,
                type = QuestionType.USAGE,
                explanation = "The missing word is '${word.word}'."
            )
         } else {
             // Fallback to Meaning question if no example exists
             return generateMeaningQuestion(index, word, allWords)
         }
    }
}
