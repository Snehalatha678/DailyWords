package uk.ac.tees.mad.dailywords.ui.presentation.home

sealed interface HomeAction {
    object ToggleBookmark : HomeAction
    object PronunciationTts : HomeAction
    object OnPronunciationPractice : HomeAction
    object NextEtymology : HomeAction
    object PullToRefresh : HomeAction
}
