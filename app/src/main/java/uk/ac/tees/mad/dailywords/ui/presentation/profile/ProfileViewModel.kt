package uk.ac.tees.mad.dailywords.ui.presentation.profile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.dailywords.ui.data.repository.SupabaseStorageRepository

class ProfileViewModel(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val storageRepository: SupabaseStorageRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    private val eventChannel = Channel<ProfileEvent>()
    val events = eventChannel.receiveAsFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch(ioDispatcher) {
            val user = auth.currentUser
            user?.let {
                firestore.collection("users").document(it.uid).get()
                    .addOnSuccessListener { document ->
                        if (document != null) {
                            _state.update {
                                it.copy(
                                    name = document.getString("name") ?: "",
                                    email = document.getString("email") ?: "",
                                    learningLevel = document.getString("learningLevel") ?: "",
                                    dailyNotifications = document.getBoolean("dailyNotifications") ?: true,
                                    darkMode = document.getBoolean("darkMode") ?: false,
                                    profileImage = document.getString("profile_image_url")
                                )
                            }
                        }
                    }
            }
        }
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnNameChanged -> _state.update { it.copy(name = action.name) }
            is ProfileAction.OnEmailChanged -> _state.update { it.copy(email = action.email) }
            is ProfileAction.OnLearningLevelChanged -> _state.update { it.copy(learningLevel = action.level) }
            is ProfileAction.OnDailyNotificationsToggled -> _state.update { it.copy(dailyNotifications = action.isEnabled) }
            is ProfileAction.OnDarkModeToggled -> _state.update { it.copy(darkMode = action.isEnabled) }
            is ProfileAction.OnProfileImageChange -> {
                viewModelScope.launch(ioDispatcher) {
                    try {
                        val result = storageRepository.uploadProfilePicture(action.imageUri)
                        _state.update { it.copy(profileImage = result.remoteUrl) }
                    } catch (e: Exception) {
                        Log.e("ProfileViewModel", "Failed to upload profile picture", e)
                    }
                }
            }
            ProfileAction.OnSaveChangesClick -> {
                viewModelScope.launch() {
                    val user = auth.currentUser
                    user?.let {
                        val userData = hashMapOf(
                            "name" to state.value.name,
                            "email" to state.value.email,
                            "learningLevel" to state.value.learningLevel,
                            "dailyNotifications" to state.value.dailyNotifications,
                            "darkMode" to state.value.darkMode
                        )
                        firestore.collection("users").document(it.uid).set(userData)
                    }
                }
            }
            ProfileAction.OnLogoutClicked -> {
                viewModelScope.launch(ioDispatcher) {
                    auth.signOut()
                    eventChannel.send(ProfileEvent.LogoutSuccess)
                }
            }
            else -> {}
        }
    }
}