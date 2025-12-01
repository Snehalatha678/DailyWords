package uk.ac.tees.mad.dailywords.ui.presentation.practice

import android.app.Application
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.dailywords.ui.domain.util.AudioPlayer
import uk.ac.tees.mad.dailywords.ui.domain.util.AudioRecorder
import uk.ac.tees.mad.dailywords.ui.domain.util.TextToSpeechProvider
import uk.ac.tees.mad.dailywords.ui.presentation.practice.voicetotext.VoiceToTextParser
import java.io.File

class PracticeViewModel(
    private val textToSpeechProvider: TextToSpeechProvider,
    private val savedStateHandle: SavedStateHandle,
    private val application: Application
) : ViewModel() {

    private val recorder by lazy { AudioRecorder(application) }
    private val player by lazy { AudioPlayer(application) }
    private var audioFile: File? = null

    private val voiceToTextParser by lazy {
        VoiceToTextParser(application) {
            val isCorrect = it.equals(state.value.word, ignoreCase = true)
            _state.update {
                it.copy(
                    pronunciationFeedback = if (isCorrect) "Good job!" else "Try again!",
                    accuracyScore = if (isCorrect) 1f else 0f
                )
            }
        }
    }

    private val _state = MutableStateFlow(PracticeState())
    val state = _state.asStateFlow()

    init {
        val word = savedStateHandle.get<String>("word") ?: ""
        val phonetic = savedStateHandle.get<String>("phonetic") ?: ""
        _state.update { it.copy(word = word, phonetic = phonetic) }

        viewModelScope.launch {
            voiceToTextParser.state.collect { voiceState ->
                _state.update { it.copy(voiceState = voiceState) }
            }
        }
    }

    fun onAction(action: PracticeAction, onNavigate: () -> Unit = {}) {
        when (action) {
            PracticeAction.OnBackClick -> onNavigate()
            PracticeAction.OnStartRecording -> {
                voiceToTextParser.startListening()
                File(application.cacheDir, "audio.mp3").also {
                    recorder.start(it)
                    audioFile = it
                }
                _state.update {
                    it.copy(
                        pronunciationFeedback = "",
                        userPronunciation = ""
                    )
                }
            }
            PracticeAction.OnStopRecording -> {
                voiceToTextParser.stopListening()
                recorder.stop()
                _state.update { it.copy(isRecordingAvailable = true) }
            }
            PracticeAction.OnPlayOriginal -> {
                textToSpeechProvider.speak(state.value.word)
            }
            PracticeAction.OnPlayRecording -> {
                audioFile?.let { player.play(it) }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        player.stop()
        recorder.stop()
    }
}