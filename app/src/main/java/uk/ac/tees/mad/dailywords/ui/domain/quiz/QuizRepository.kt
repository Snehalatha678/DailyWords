package uk.ac.tees.mad.dailywords.ui.domain.quiz

import kotlinx.coroutines.flow.Flow
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult

interface QuizRepository {
    fun generateQuiz(): Flow<HttpResult<List<QuizQuestion>>>
    suspend fun saveQuizResult(score: Int, totalQuestions: Int)
    fun getQuizHistory(): Flow<List<QuizResult>>
}

data class QuizResult(
    val date: Long,
    val score: Int,
    val totalQuestions: Int
)
