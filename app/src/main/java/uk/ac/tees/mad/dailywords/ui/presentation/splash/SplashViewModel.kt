package uk.ac.tees.mad.dailywords.ui.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SplashViewModel(
    private val auth: FirebaseAuth
) : ViewModel() {

    private val _state = MutableStateFlow(SplashState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isUserLoggedIn = auth.currentUser != null,
                    quote = getRandomQuote()
                )
            }
        }
    }

    private fun getRandomQuote(): String {
        val quotes = listOf(
            "The more that you read, the more things you will know. The more that you learn, the more places you’ll go.",
            "Live as if you were to die tomorrow. Learn as if you were to live forever.",
            "An investment in knowledge pays the best interest."
        )
        return quotes.random()
    }
}