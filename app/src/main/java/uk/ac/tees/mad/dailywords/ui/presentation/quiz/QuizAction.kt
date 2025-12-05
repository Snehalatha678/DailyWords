package uk.ac.tees.mad.dailywords.ui.presentation.quiz

sealed interface QuizAction {
    data class OnOptionSelected(val index: Int) : QuizAction
    data object OnSubmitAnswer : QuizAction
    data object OnNextQuestion : QuizAction
    data object OnRetryQuiz : QuizAction
    data object OnReviewMistakes : QuizAction
    data object OnFinishReview : QuizAction
    data object OnNavigateToHome : QuizAction
    data object OnNavigateToPractice : QuizAction
    data object OnNavigateToProfile : QuizAction
}
