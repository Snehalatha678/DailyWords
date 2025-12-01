package uk.ac.tees.mad.dailywords.ui.presentation.practice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.ac.tees.mad.dailywords.ui.presentation.practice.voicetotext.VoiceToTextParser

class PracticeViewModel(private val voiceToTextParser: VoiceToTextParser) : ViewModel() {

    private val _state = MutableStateFlow(PracticeState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            voiceToTextParser.state.collect { voiceState ->
                _state.update { it.copy(voiceState = voiceState) }
            }
        }
    }

    fun onAction(action: PracticeAction, onNavigate: () -> Unit = {}) {
        when (action) {
            PracticeAction.OnBackClick -> onNavigate()
            PracticeAction.OnStartRecording -> voiceToTextParser.startListening()
            PracticeAction.OnStopRecording -> voiceToTextParser.stopListening()
        }
    }
}