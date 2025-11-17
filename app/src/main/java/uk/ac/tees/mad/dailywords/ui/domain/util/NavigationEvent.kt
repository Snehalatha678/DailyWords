package uk.ac.tees.mad.dailywords.ui.domain.util

sealed interface NavigationEvent {
    data object NavigateBack : NavigationEvent
}
