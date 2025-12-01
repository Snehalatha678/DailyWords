package uk.ac.tees.mad.dailywords.ui.presentation.practice

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.practice.voicetotext.VoiceToTextState
import uk.ac.tees.mad.dailywords.ui.theme.DailyWordsTheme
import kotlin.random.Random

@Composable
fun PracticeRoot(
    viewModel: PracticeViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    PracticeScreen(
        state = state,
        onAction = {
            viewModel.onAction(it) {
                onBackClick()
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PracticeScreen(
    state: PracticeState,
    onAction: (PracticeAction) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Pronunciation Practice", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { onAction(PracticeAction.OnBackClick) }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            BottomAppBar {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    state.bottomNavItems.forEach { item ->
                        NavigationBarItem(
                            selected = item.isSelected,
                            onClick = { /*TODO*/ },
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) }
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            WordPronunciationCard(word = state.word, phonetic = state.phonetic)
            Spacer(modifier = Modifier.height(32.dp))
            RecordingSection(state, onAction)
            Spacer(modifier = Modifier.height(32.dp))
            PreviousAttemptsSection(attempts = state.previousAttempts)
        }
    }
}

@Composable
fun WordPronunciationCard(word: String, phonetic: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 24.dp, horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = word,
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(text = phonetic, fontSize = 18.sp, color = Color.Gray)
        }
    }
}

@Composable
fun RecordingSection(state: PracticeState, onAction: (PracticeAction) -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier.size(120.dp),
            contentAlignment = Alignment.Center
        ) {
            val animatedProgress by animateFloatAsState(
                targetValue = if (state.voiceState.isSpeaking) 1f else 0f,
                animationSpec = tween(500),
                label = "progress"
            )
            val primaryColor = MaterialTheme.colorScheme.primary
            Canvas(modifier = Modifier.size(120.dp)) {
                drawArc(
                    color = Color.LightGray,
                    startAngle = -90f,
                    sweepAngle = 360f,
                    useCenter = false,
                    style = Stroke(width = 4.dp.toPx())
                )
                drawArc(
                    color = primaryColor,
                    startAngle = -90f,
                    sweepAngle = 360 * animatedProgress,
                    useCenter = false,
                    style = Stroke(width = 4.dp.toPx())
                )
            }
            IconButton(
                onClick = {
                    if (state.voiceState.isSpeaking) {
                        onAction(PracticeAction.OnStopRecording)
                    } else {
                        onAction(PracticeAction.OnStartRecording)
                    }
                },
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = if (state.voiceState.isSpeaking) Icons.Default.Stop else Icons.Default.Mic,
                    contentDescription = if (state.voiceState.isSpeaking) "Stop recording" else "Start recording",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Text(
            text = state.voiceState.spokenText.ifEmpty { "Press the button to start speaking" },
            textAlign = TextAlign.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        if (state.pronunciationFeedback.isNotEmpty()) {
            Text(
                text = state.pronunciationFeedback,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = if (state.accuracyScore > 0.8) Color(0xFF1E8E3E) else Color(0xFFF9AB00)
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Play original")
            }
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Play your recording")
            }
        }
    }
}

@Composable
fun PreviousAttemptsSection(attempts: List<PracticeAttempt>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Previous Attempts",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                if (attempts.isEmpty()) {
                    Text(
                        "No attempts yet.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                } else {
                    attempts.forEach { attempt ->
                        AttemptRow(attempt)
                    }
                }
            }
        }
    }
}

@Composable
fun AttemptRow(attempt: PracticeAttempt) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Schedule, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(16.dp))
        Text(attempt.time, color = Color.Gray, modifier = Modifier.weight(1f))
        Text(
            text = attempt.result,
            color = Color.White,
            modifier = Modifier
                .background(attempt.resultColor, RoundedCornerShape(50))
                .padding(horizontal = 12.dp, vertical = 6.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, device = "id:pixel_6")
@Composable
private fun Preview() {
    val previewState = PracticeState(
        word = "Serendipity",
        phonetic = "/ˌsɛrənˈdɪpɪti/",
        previousAttempts = listOf(
            PracticeAttempt("10:30 AM", "Perfect!", Color(0xFF1E8E3E)),
            PracticeAttempt("10:28 AM", "Try again!", Color(0xFFD93025)),
            PracticeAttempt("Yesterday", "Good attempt.", Color(0xFFF9AB00))
        ),
        bottomNavItems = listOf(), // Simplified for preview
        voiceState = VoiceToTextState(isSpeaking = true)
    )
    DailyWordsTheme {
        PracticeScreen(
            state = previewState,
            onAction = {}
        )
    }
}
