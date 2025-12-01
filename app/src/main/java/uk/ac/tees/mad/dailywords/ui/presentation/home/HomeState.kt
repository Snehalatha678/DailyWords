package uk.ac.tees.mad.dailywords.ui.presentation.home

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.Quiz
import androidx.compose.ui.graphics.vector.ImageVector
import uk.ac.tees.mad.dailywords.ui.domain.word.Word

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val selectedIcon: ImageVector,
    val isSelected: Boolean = false
)

data class HomeState(
    val word: Word? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val bottomNavItems: List<BottomNavItem> = listOf(
        BottomNavItem(
            label = "Home",
            icon = Icons.Outlined.Home,
            selectedIcon = Icons.Filled.Home,
            isSelected = true
        ),
        BottomNavItem(
            label = "Practice",
            icon = Icons.Outlined.MenuBook,
            selectedIcon = Icons.Filled.MenuBook
        ),
        BottomNavItem(
            label = "Quiz",
            icon = Icons.Outlined.Quiz,
            selectedIcon = Icons.Outlined.Quiz
        ),
        BottomNavItem(
            label = "Profile",
            icon = Icons.Outlined.PersonOutline,
            selectedIcon = Icons.Filled.Person
        )
    ),
    val bookmarkedWords: List<String> = emptyList(),
    val streakCount: Int = 0
)