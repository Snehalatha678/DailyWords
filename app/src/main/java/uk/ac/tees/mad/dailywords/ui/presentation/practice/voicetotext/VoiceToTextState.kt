package uk.ac.tees.mad.dailywords.ui.presentation.practice.voicetotext

data class VoiceToTextState(
    val spokenText: String = "",
    val isSpeaking: Boolean = false,
    val error: String? = null
)