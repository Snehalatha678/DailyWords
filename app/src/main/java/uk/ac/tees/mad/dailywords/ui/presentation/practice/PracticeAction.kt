package uk.ac.tees.mad.dailywords.ui.presentation.practice

sealed interface PracticeAction {
    data object OnBackClick : PracticeAction
    data object OnStartRecording : PracticeAction
    data object OnStopRecording : PracticeAction
}