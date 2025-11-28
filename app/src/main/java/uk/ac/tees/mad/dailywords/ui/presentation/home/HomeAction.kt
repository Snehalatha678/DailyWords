package uk.ac.tees.mad.dailywords.ui.presentation.home

sealed interface HomeAction {
    object OnRefresh : HomeAction
    data class OnBookmarkClick(val word: String) : HomeAction
    data class OnPronunciationClick(val word: String) : HomeAction
    data class OnMicClick(val word: String) : HomeAction
    object OnNextClick : HomeAction
}
