package uk.ac.tees.mad.dailywords.ui.presentation.practice

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import uk.ac.tees.mad.dailywords.ui.presentation.practice.voicetotext.VoiceToTextState

data class PracticeAttempt(
    val time: String,
    val result: String,
    val resultColor: Color
)

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val isSelected: Boolean = false
)

data class PracticeState(
    val word: String = "",
    val phonetic: String = "",
    val previousAttempts: List<PracticeAttempt> = emptyList(),
    val bottomNavItems: List<BottomNavItem> = emptyList(),
    val voiceState: VoiceToTextState = VoiceToTextState(),
    val userPronunciation: String = "",
    val pronunciationFeedback: String = "",
    val attemptCount: Int = 0,
    val accuracyScore: Float = 0.0f,
    val isRecordingAvailable: Boolean = false
)
