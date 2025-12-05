package uk.ac.tees.mad.dailywords.ui.presentation.quiz

import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuizQuestion

data class QuizState(
    val isLoading: Boolean = false,
    val currentQuestionIndex: Int = 0,
    val score: Int = 0,
    val streak: Int = 0,
    val isQuizComplete: Boolean = false,
    val isReviewMode: Boolean = false,
    val questions: List<QuizQuestion> = emptyList(),
    val selectedAnswerIndex: Int? = null,
    val isAnswerSubmitted: Boolean = false
)
