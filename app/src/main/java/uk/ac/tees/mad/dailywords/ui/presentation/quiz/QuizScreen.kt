package uk.ac.tees.mad.dailywords.ui.presentation.quiz

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuestionType
import uk.ac.tees.mad.dailywords.ui.domain.quiz.QuizQuestion
import uk.ac.tees.mad.dailywords.ui.presentation.home.BottomNavItem
import uk.ac.tees.mad.dailywords.ui.theme.DailyWordsTheme

@Composable
fun QuizRoot(
    // Use Koin to get the ViewModel. Explicitly naming the type to avoid ambiguity.
    viewModel: QuizViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    QuizScreen(
        state = state,
        onAction = { action ->
            viewModel.onAction(action) {
                when (action) {
                    QuizAction.OnNavigateToHome -> onNavigateToHome()
                    QuizAction.OnNavigateToPractice -> onNavigateToPractice()
                    QuizAction.OnNavigateToProfile -> onNavigateToProfile()
                    else -> {}
                }
            }
        }
    )
}

@Composable
fun QuizScreen(
    state: QuizState,
    onAction: (QuizAction) -> Unit,
) {
    val bottomNavItems = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home
        ),
        BottomNavItem(
            label = "Practice",
            icon = Icons.Outlined.MenuBook,
            selectedIcon = Icons.Filled.MenuBook
        ),
        BottomNavItem(
            label = "Quiz",
            icon = Icons.Outlined.Quiz,
            selectedIcon = Icons.Outlined.Quiz,
            isSelected = true
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Outlined.PersonOutline,
            selectedIcon = Icons.Filled.Person
        )
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = item.isSelected,
                        onClick = {
                            when (item.label) {
                                "Home" -> onAction(QuizAction.OnNavigateToHome)
                                "Practice" -> onAction(QuizAction.OnNavigateToPractice)
                                "Profile" -> onAction(QuizAction.OnNavigateToProfile)
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = if (item.isSelected) item.selectedIcon else item.icon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (state.isQuizComplete) {
                QuizResultContent(
                    score = state.score,
                    totalQuestions = state.questions.size,
                    onRetry = { onAction(QuizAction.OnRetryQuiz) },
                    onReview = { onAction(QuizAction.OnReviewMistakes) }
                )
            } else if (state.questions.isNotEmpty()) {
                QuizQuestionContent(
                    state = state,
                    onOptionSelected = { onAction(QuizAction.OnOptionSelected(it)) },
                    onSubmit = { onAction(QuizAction.OnSubmitAnswer) },
                    onNext = { onAction(QuizAction.OnNextQuestion) }
                )
            } else {
                // Empty state or error
                Text("No questions available.", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun QuizQuestionContent(
    state: QuizState,
    onOptionSelected: (Int) -> Unit,
    onSubmit: () -> Unit,
    onNext: () -> Unit
) {
    val question = state.questions[state.currentQuestionIndex]
    val progress = (state.currentQuestionIndex + 1).toFloat() / state.questions.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header: Progress and Streak
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .weight(1f)
                    .height(8.dp)
                    .clip(RoundedCornerShape(4.dp)),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.EmojiEvents, contentDescription = "Streak", tint = Color(0xFFFFD700))
                Text(
                    text = "${state.streak}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Question Type Badge
        Box(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer, RoundedCornerShape(8.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = question.type.name,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Question Text
        Text(
            text = question.questionText,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Options
        question.options.forEachIndexed { index, option ->
            QuizOptionItem(
                text = option,
                isSelected = state.selectedAnswerIndex == index,
                isCorrect = index == question.correctAnswerIndex,
                isSubmitted = state.isAnswerSubmitted,
                onClick = { onOptionSelected(index) }
            )
            Spacer(modifier = Modifier.height(12.dp))
        }

        // Explanation (Visible after submission)
        AnimatedContent(
            targetState = state.isAnswerSubmitted,
            transitionSpec = { fadeIn(animationSpec = tween(300)) togetherWith fadeOut(animationSpec = tween(0)) },
            label = "ExplanationAnimation"
        ) { showExplanation ->
            if (showExplanation) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Explanation:",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = question.explanation,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(24.dp))

        // Action Button
        Button(
            onClick = {
                if (state.isAnswerSubmitted) onNext() else onSubmit()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            enabled = state.selectedAnswerIndex != null || state.isAnswerSubmitted,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = if (state.isAnswerSubmitted)
                    if(state.currentQuestionIndex == state.questions.lastIndex) "Finish" else "Next Question"
                else "Check Answer",
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

@Composable
fun QuizOptionItem(
    text: String,
    isSelected: Boolean,
    isCorrect: Boolean,
    isSubmitted: Boolean,
    onClick: () -> Unit
) {
    val borderColor = when {
        isSubmitted && isCorrect -> Color(0xFF4CAF50) // Green
        isSubmitted && isSelected && !isCorrect -> Color(0xFFE53935) // Red
        isSelected -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
    }

    val backgroundColor = when {
        isSubmitted && isCorrect -> Color(0xFFE8F5E9)
        isSubmitted && isSelected && !isCorrect -> Color(0xFFFFEBEE)
        isSelected -> MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)
        else -> Color.Transparent
    }

    val icon = when {
        isSubmitted && isCorrect -> Icons.Default.Check
        isSubmitted && isSelected && !isCorrect -> Icons.Default.Close
        else -> null
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .background(backgroundColor)
            .clickable(enabled = !isSubmitted) { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.onSurface
        )
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (icon == Icons.Default.Check) Color(0xFF4CAF50) else Color(0xFFE53935)
            )
        } else {
            // Radio circle placeholder
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        2.dp,
                        if(isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        CircleShape
                    )
                    .padding(4.dp)
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                    )
                }
            }
        }
    }
}

@Composable
fun QuizResultContent(
    score: Int,
    totalQuestions: Int,
    onRetry: () -> Unit,
    onReview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.EmojiEvents,
            contentDescription = "Trophy",
            modifier = Modifier.size(120.dp),
            tint = Color(0xFFFFD700)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Quiz Complete!",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "You scored $score points!",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onRetry,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Retry Quiz")
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedButton(
            onClick = onReview,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Review Mistakes")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewQuiz() {
    DailyWordsTheme {
        QuizScreen(
            state = QuizState(
                questions = listOf(
                    QuizQuestion("1", "Test Q?", listOf("A", "B"), 0, QuestionType.MEANING, "Expl")
                )
            ),
            onAction = {}
        )
    }
}
