package uk.ac.tees.mad.dailywords.ui.presentation.profile

import android.net.Uri

sealed interface ProfileAction {
    data class OnNameChanged(val name: String) : ProfileAction
    data class OnEmailChanged(val email: String) : ProfileAction
    data class OnLearningLevelChanged(val level: String) : ProfileAction
    data class OnDailyNotificationsToggled(val isEnabled: Boolean) : ProfileAction
    data class OnDarkModeToggled(val isEnabled: Boolean) : ProfileAction
    data class OnProfileImageChange(val imageUri: Uri) : ProfileAction
    data object OnBookmarksClicked : ProfileAction
    data object OnResetStreakClicked : ProfileAction
    data object OnLogoutClicked : ProfileAction
    data object OnSaveChangesClick : ProfileAction
    data object OnNavigateToQuiz : ProfileAction
}