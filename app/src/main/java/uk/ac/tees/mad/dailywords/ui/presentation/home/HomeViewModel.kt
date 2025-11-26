package uk.ac.tees.mad.dailywords.ui.presentation.home

import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.dailywords.ui.domain.util.HttpResult
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetEtymology
import uk.ac.tees.mad.dailywords.ui.domain.word.usecase.GetRandomWord
import java.util.Locale

class HomeViewModel(
    private val getRandomWord: GetRandomWord,
    private val getEtymology: GetEtymology
) : ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000L),
        initialValue = HomeState()
    )

    private var tts: TextToSpeech? = null

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        viewModelScope.launch {
            getRandomWord().onEach { result ->
                when (result) {
                    is HttpResult.Loading -> {
                        _state.update { it.copy(isLoading = true, error = null) }
                    }

                    is HttpResult.Success -> {
                        val word = result.data
                        if (word != null) {
                            _state.update {
                                it.copy(
                                    isLoading = false,
                                    word = word.word,
                                    phonetic = word.phonetic,
                                    partOfSpeech = word.meanings.firstOrNull()?.partOfSpeech ?: "",
                                    definitions = word.meanings.flatMap { it.definitions }.map { it.definition },
                                    examples = word.meanings.flatMap { it.definitions }.mapNotNull { it.example },
                                    etymology = ""
                                )
                            }
                        } else {
                            _state.update { it.copy(isLoading = false, error = "Word not found") }
                        }
                    }

                    is HttpResult.Failure -> {
                        _state.update { it.copy(isLoading = false, error = result.error.name) }
                    }
                }
            }.launchIn(this)
        }
    }

    fun onAction(action: HomeAction) {
        when (action) {
            HomeAction.ToggleBookmark -> {
                _state.update { it.copy(isBookmarked = !it.isBookmarked) }
            }

            HomeAction.PronunciationTts -> {
                tts?.speak(_state.value.word, TextToSpeech.QUEUE_FLUSH, null, null)
            }

            HomeAction.OnPronunciationPractice -> {
                // Navigate to Pronunciation screen
            }

            HomeAction.NextEtymology -> {
                viewModelScope.launch {
                    val etymology = getEtymology(_state.value.word)
                    _state.update { it.copy(showEtymology = true, etymology = etymology?.etymology) }
                }
            }

            HomeAction.PullToRefresh -> {
                loadInitialData()
            }
        }
    }

    fun initializeTts(context: Context) {
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
            }
        }
    }

    override fun onCleared() {
        tts?.stop()
        tts?.shutdown()
        super.onCleared()
    }
}