package uk.ac.tees.mad.dailywords.ui.presentation.practice

sealed interface PracticeAction {
    data object OnBackClick : PracticeAction
    data object OnStartRecording : PracticeAction
    data object OnStopRecording : PracticeAction
    data object OnPlayOriginal : PracticeAction
    data object OnPlayRecording : PracticeAction
    data object OnNavigateToHome : PracticeAction
    data object OnNavigateToQuiz : PracticeAction
    data object OnNavigateToProfile : PracticeAction
}