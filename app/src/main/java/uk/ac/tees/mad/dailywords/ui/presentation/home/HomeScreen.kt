package uk.ac.tees.mad.dailywords.ui.presentation.home

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.dailywords.ui.theme.DailyWordsTheme

@Composable
fun HomeRoot(
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        viewModel.initializeTts(context)
    }

    HomeScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
) {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                onAction(HomeAction.OnPronunciationPractice)
            }
        }
    )

    val snackbarHostState = remember { SnackbarHostState() }

    // Use official pull-to-refresh APIs:
    val pullRefreshState = rememberPullRefreshState(
        refreshing = state.isLoading,
        onRefresh = { onAction(HomeAction.PullToRefresh) }
    )

    LaunchedEffect(state.error) {
        state.error?.let {
            snackbarHostState.showSnackbar(it)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            FloatingActionButton(onClick = { onAction(HomeAction.NextEtymology) }) {
                Text("Next")
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // attach pullRefresh to the scrollable container
                .pullRefresh(pullRefreshState)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
            ) {
                if (state.isLoading && !state.isLoading /* placeholder - still show spinner when loading */) {
                    // Note: since we use pullRefresh's refreshing state, the indicator will show.
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = state.word, style = MaterialTheme.typography.headlineLarge)
                        Icon(
                            imageVector = if (state.isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = "Bookmark",
                            modifier = Modifier.clickable { onAction(HomeAction.ToggleBookmark) }
                        )
                    }

                    Text(text = state.phonetic, style = MaterialTheme.typography.bodyMedium)
                    Text(text = state.partOfSpeech, style = MaterialTheme.typography.bodySmall)

                    Spacer(modifier = Modifier.height(16.dp))

                    // Pronunciation
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                        IconButton(onClick = { onAction(HomeAction.PronunciationTts) }) {
                            Icon(Icons.AutoMirrored.Filled.VolumeUp, contentDescription = "Pronunciation")
                        }
                        IconButton(onClick = {
                            launcher.launch(Manifest.permission.RECORD_AUDIO)
                        }) {
                            Icon(Icons.Default.Mic, contentDescription = "Practice Pronunciation")
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Definitions
                    Text("Definitions", style = MaterialTheme.typography.titleMedium)
                    state.definitions.forEach { definition ->
                        Text(text = "- $definition")
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    // Examples
                    Text("Examples", style = MaterialTheme.typography.titleMedium)
                    state.examples.forEach { example ->
                        Text(text = "- $example")
                    }

                    if (state.showEtymology) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Etymology/Fun Fact", style = MaterialTheme.typography.titleMedium)
                        Text(text = state.etymology ?: "")
                    }
                }
            }

            // The official pull-to-refresh indicator (auto-shows when 'refreshing' == true)
            PullRefreshIndicator(
                refreshing = state.isLoading,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}


@Preview
@Composable
private fun Preview() {
    DailyWordsTheme {
        HomeScreen(
            state = HomeState(
                word = "Ephemeral",
                phonetic = "/əˈfem.ər.əl/",
                partOfSpeech = "adjective",
                definitions = listOf("Lasting for a very short time."),
                examples = listOf("The beauty of the cherry blossoms is ephemeral."),
                isBookmarked = true
            ),
            onAction = {}
        )
    }
}
