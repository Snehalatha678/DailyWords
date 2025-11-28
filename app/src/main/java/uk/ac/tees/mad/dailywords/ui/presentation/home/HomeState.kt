package uk.ac.tees.mad.dailywords.ui.presentation.home

import androidx.compose.ui.graphics.vector.ImageVector
import uk.ac.tees.mad.dailywords.ui.domain.word.Word

data class BottomNavItem(
    val label: String,
    val icon: ImageVector,
    val isSelected: Boolean = false
)

data class HomeState(
    val word: Word? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val bottomNavItems: List<BottomNavItem> = emptyList(),
    val bookmarkedWords: List<String> = emptyList(),
    val streakCount: Int = 0
)