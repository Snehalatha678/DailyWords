package uk.ac.tees.mad.dailywords.ui.presentation.profile

import android.Manifest
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.Flow
import org.koin.androidx.compose.koinViewModel
import uk.ac.tees.mad.dailywords.ui.presentation.home.BottomNavItem
import uk.ac.tees.mad.dailywords.ui.theme.DailyWordsTheme


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ProfileRoot(
    viewModel: ProfileViewModel = koinViewModel(),
    onLogout: () -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPractice: () -> Unit,
    onNavigateToQuiz: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            ProfileEvent.LogoutSuccess -> onLogout()
        }
    }

    val notificationPermissionState = rememberPermissionState(Manifest.permission.POST_NOTIFICATIONS)
    LaunchedEffect(Unit) {
        if (!notificationPermissionState.status.isGranted) {
            notificationPermissionState.launchPermissionRequest()
        }
    }

    ProfileScreen(
        state = state,
        onAction = {
            viewModel.onAction(it) {
                when(it) {
                    ProfileAction.OnNavigateToQuiz -> onNavigateToQuiz()
                    else -> {}
                }
            }
        },
        onNavigateToHome = onNavigateToHome,
        onNavigateToPractice = onNavigateToPractice,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    state: ProfileState,
    onAction: (ProfileAction) -> Unit,
    onNavigateToHome: () -> Unit,
    onNavigateToPractice: () -> Unit,
) {
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            uri?.let { onAction(ProfileAction.OnProfileImageChange(it)) }
        }
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Settings & Profile") },
                colors = androidx.compose.material3.TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFF0F4F8)
                )
            )
        },
        containerColor = Color(0xFFF0F4F8),
        bottomBar = {
            NavigationBar {
                val bottomNavItems = listOf(
                    BottomNavItem("Home", Icons.Outlined.Home, Icons.Filled.Home),
                    BottomNavItem("Practice", Icons.Outlined.MenuBook, Icons.AutoMirrored.Filled.MenuBook),
                    BottomNavItem("Quiz", Icons.Outlined.Quiz, Icons.Outlined.Quiz),
                    BottomNavItem("Profile", Icons.Outlined.Person, Icons.Filled.Person, true)
                )
                bottomNavItems.forEach { item ->
                    NavigationBarItem(
                        selected = item.isSelected,
                        onClick = {
                            when (item.label) {
                                "Home" -> onNavigateToHome()
                                "Practice" -> onNavigateToPractice()
                                "Quiz" -> onAction(ProfileAction.OnNavigateToQuiz)
                            }
                        },
                        icon = { Icon(if (item.isSelected) item.selectedIcon else item.icon, contentDescription = item.label) },
                        label = { Text(item.label) }
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Section
            Section(title = "Profile") {
                ProfileCard(
                    image = state.profileImage,
                    name = state.name,
                    email = state.email,
                    onImageClick = { imagePickerLauncher.launch("image/*") },
                    onLogoutClick = { onAction(ProfileAction.OnLogoutClicked) }
                )
            }

            // Hydration Settings
            Section(title = "Hydration Settings") {
                HydrationSettingsCard(
                    learningLevel = state.learningLevel,
                    onLearningLevelChange = { onAction(ProfileAction.OnLearningLevelChanged(it)) },
                    dailyNotifications = state.dailyNotifications,
                    onDailyNotificationsToggled = { onAction(ProfileAction.OnDailyNotificationsToggled(it)) },
                    onSaveChanges = { onAction(ProfileAction.OnSaveChangesClick) }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}


@Composable
private fun Section(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        content()
    }
}

@Composable
private fun ProfileCard(
    image: Any?,
    name: String,
    email: String,
    onImageClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = image),
                contentDescription = "Profile Picture",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(50.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onImageClick) // Pass the onImageClick lambda here
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = email, color = Color.Gray, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Row(modifier = Modifier.clickable(onClick = onLogoutClick), verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                    contentDescription = "Logout",
                    tint = Color.Red
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text("Logout", color = Color.Red, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun HydrationSettingsCard(
    learningLevel: String,
    onLearningLevelChange: (String) -> Unit,
    dailyNotifications: Boolean,
    onDailyNotificationsToggled: (Boolean) -> Unit,
    onSaveChanges: () -> Unit
) {
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val reminderOptions = listOf("Beginner", "Intermediate", "Advanced")

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "App Settings", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(16.dp))

            // Learning Level
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Learning Level")
                Box {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF0F4F8))
                            .clickable { isDropdownExpanded = true }
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(learningLevel, textAlign = TextAlign.Center)
                        Icon(Icons.Default.ArrowDropDown, "Dropdown")
                    }
                    DropdownMenu(
                        expanded = isDropdownExpanded,
                        onDismissRequest = { isDropdownExpanded = false }
                    ) {
                        reminderOptions.forEach { level ->
                            DropdownMenuItem(
                                text = { Text(level) },
                                onClick = {
                                    onLearningLevelChange(level)
                                    isDropdownExpanded = false
                                }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))

            // Daily Notifications
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Daily Notifications")
                Switch(checked = dailyNotifications, onCheckedChange = onDailyNotificationsToggled)
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Save Button
            Button(
                onClick = onSaveChanges,
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF15A4B8)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Icon(imageVector = Icons.Default.Save, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Save Changes", color = Color.White)
            }
        }
    }
}

@Composable
fun ObserveAsEvents(flow: Flow<ProfileEvent>, onEvent: (ProfileEvent) -> Unit) {
    LaunchedEffect(flow) {
        flow.collect {
            onEvent(it)
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview() {
    DailyWordsTheme() {
        ProfileScreen(
            state = ProfileState(),
            onAction = {},
            onNavigateToHome = {},
            onNavigateToPractice = {}
        )
    }
}
