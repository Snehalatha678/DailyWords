package uk.ac.tees.mad.dailywords.ui.presentation.home

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.dailywords.ui.domain.word.Word
import uk.ac.tees.mad.dailywords.ui.theme.DailyWordsTheme

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel(),
    onNavigateToPractice: (String, String) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    HomeScreen(
        state = state,
        onAction = { action ->
            viewModel.onAction(action) {
                when (action) {
                    is HomeAction.OnNavigateToPractice -> onNavigateToPractice(state.word?.word ?: "", state.word?.phonetic ?: "")
                    is HomeAction.OnNavigateToProfile -> onNavigateToProfile()
                    else -> {}
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "DailyWords",
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(
                            imageVector = Icons.Default.MenuBook,
                            contentDescription = "Dictionary"
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { onAction(HomeAction.OnRefresh) }) {
                        Icon(imageVector = Icons.Default.Refresh, contentDescription = "Refresh")
                    }
                    IconButton(onClick = { onAction(HomeAction.OnBookmarkClick(state.word?.word ?: "")) }) {
                        Icon(
                            imageVector = if (state.word?.isBookmarked == true) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark"
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        },
        bottomBar = {
            NavigationBar {
                state.bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = item.isSelected,
                        onClick = {
                            when (item.label) {
                                "Practice" -> onAction(HomeAction.OnNavigateToPractice)
                                "Profile" -> onAction(HomeAction.OnNavigateToProfile)
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
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (state.isLoading) {
                CircularProgressIndicator()
            } else if (state.error != null) {
                Text(text = state.error)
            } else if (state.word != null) {
                WordContent(
                    word = state.word,
                    onAction = onAction
                )
            }
        }
    }
}

@Composable
fun WordContent(
    word: Word,
    onAction: (HomeAction) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = word.word,
            fontSize = 48.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Text(
            text = word.phonetic ?: "",
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Audio Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            IconButton(
                onClick = { onAction(HomeAction.OnPronunciationClick(word.word)) },
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Pronounce",
                    modifier = Modifier.size(32.dp),
                    tint = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Meaning Card
        word.meanings.forEach {
            InfoCard(title = it.partOfSpeech, subtitle = "Meaning", content = it.definitions.joinToString { it.definition })
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Example Usage Card
        word.meanings.forEach { meaning ->
            meaning.definitions.forEach {
                if (it.example != null) {
                    InfoCard(
                        title = "Example Usage",
                        subtitle = "Contextual sentence",
                        content = it.example
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun InfoCard(title: String, subtitle: String, content: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge
            )
            Text(
                text = subtitle,
                color = Color.Gray,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = content, style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    val previewState = HomeState(
        word = Word(
            word = "Ephemeral",
            phonetic = "/ɪˈfɛmərəl/",
            meanings = listOf(),
            sourceUrls = listOf()
        )
    )
    DailyWordsTheme {
        HomeScreen(
            state = previewState,
            onAction = {}
        )
    }
}
