package uk.ac.tees.mad.dailywords.ui.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class ProfileViewModel : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(ProfileState())
    val state = _state
        .onStart {
            if (!hasLoadedInitialData) {
                /** Load initial data here **/
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = ProfileState()
        )

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnNameChanged -> _state.update { it.copy(name = action.name) }
            is ProfileAction.OnEmailChanged -> _state.update { it.copy(email = action.email) }
            is ProfileAction.OnLearningLevelChanged -> _state.update { it.copy(learningLevel = action.level) }
            is ProfileAction.OnDailyNotificationsToggled -> _state.update { it.copy(dailyNotifications = action.isEnabled) }
            is ProfileAction.OnDarkModeToggled -> _state.update { it.copy(darkMode = action.isEnabled) }
            ProfileAction.OnBookmarksClicked -> {
                // TODO: Handle Bookmarks Click
            }
            ProfileAction.OnResetStreakClicked -> {
                // TODO: Handle Reset Streak Click
            }
            ProfileAction.OnLogoutClicked -> {
                // TODO: Handle Logout Click
            }
        }
    }
}
