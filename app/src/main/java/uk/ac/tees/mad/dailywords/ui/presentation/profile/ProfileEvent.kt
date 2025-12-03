package uk.ac.tees.mad.dailywords.ui.presentation.profile

sealed interface ProfileEvent {
    data object LogoutSuccess : ProfileEvent
}