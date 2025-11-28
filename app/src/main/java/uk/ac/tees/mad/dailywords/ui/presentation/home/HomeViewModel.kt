package uk.ac.tees.mad.dailywords.ui.presentation.home

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult
import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.AddBookmark
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetBookmarkedWords
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetRandomWord
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.RemoveBookmark
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class HomeViewModel(
    private val getRandomWord: GetRandomWord,
    private val addBookmark: AddBookmark,
    private val removeBookmark: RemoveBookmark,
    private val getBookmarkedWords: GetBookmarkedWords,
    private val textToSpeech: TextToSpeech,
    private val context: Context
) : ViewModel() {

    private var hasLoadedInitialData = false

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()
        .onStart {
            if (!hasLoadedInitialData) {
                loadInitialData()
                observeBookmarkedWords()
                updateStreak()
                hasLoadedInitialData = true
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000L),
            initialValue = HomeState()
        )

    private fun loadInitialData() {
        viewModelScope.launch {
            getRandomWord().collect { result ->
                when (result) {
                    is HttpResult.Success -> {
                        _state.update {
                            it.copy(
                                word = result.data,
                                isLoading = false
                            )
                        }
                    }

                    is HttpResult.Failure -> {
                        _state.update {
                            it.copy(
                                error = result.error.name,
                                isLoading = false
                            )
                        }
                    }

                    is HttpResult.Loading -> {
                        _state.update {
                            it.copy(isLoading = true)
                        }
                    }
                }
            }
        }
    }

    private fun observeBookmarkedWords() {
        getBookmarkedWords().onEach { words: List<Word> ->
            val bookmarkedWords = words.map { it.word }
            _state.update { it.copy(bookmarkedWords = bookmarkedWords) }
        }.launchIn(viewModelScope)
    }

    private fun updateStreak() {
        val sharedPreferences = context.getSharedPreferences("streak_prefs", Context.MODE_PRIVATE)
        val lastVisitDate = sharedPreferences.getString("last_visit_date", null)
        val streakCount = sharedPreferences.getInt("streak_count", 0)

        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

        if (lastVisitDate == null) {
            sharedPreferences.edit().apply {
                putString("last_visit_date", currentDate)
                putInt("streak_count", 1)
                apply()
            }
            _state.update { it.copy(streakCount = 1) }
        } else {
            val lastVisit = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            ).parse(lastVisitDate)
            val today = Date()
            val diff = today.time - (lastVisit?.time ?: 0)
            val days = (diff / (1000 * 60 * 60 * 24)).toInt()

            if (days == 1) {
                val newStreak = streakCount + 1
                sharedPreferences.edit().apply {
                    putString("last_visit_date", currentDate)
                    putInt("streak_count", newStreak)
                    apply()
                }
                _state.update { it.copy(streakCount = newStreak) }
            } else if (days > 1) {
                sharedPreferences.edit().apply {
                    putString("last_visit_date", currentDate)
                    putInt("streak_count", 1)
                    apply()
                }
                _state.update { it.copy(streakCount = 1) }
            } else {
                _state.update { it.copy(streakCount = streakCount) }
            }
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnBookmarkClick -> {
                val word = state.value.word?.copy()
                word?.let {
                    viewModelScope.launch {
                        if (it.isBookmarked) {
                            removeBookmark(it)
                        } else {
                            addBookmark(it)
                        }
                        _state.update { state ->
                            state.copy(word = state.word?.copy(isBookmarked = !it.isBookmarked))
                        }
                    }
                }
            }

            is HomeAction.OnPronunciationClick -> {
                textToSpeech.speak(
                    action.word,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )
            }

            is HomeAction.OnMicClick -> {
                // Handle mic click
            }

            HomeAction.OnRefresh -> {
                loadInitialData()
            }

            HomeAction.OnNextClick -> {
                // No-op
            }
        }
    }
}
