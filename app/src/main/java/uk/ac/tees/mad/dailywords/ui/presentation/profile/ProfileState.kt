package uk.ac.tees.mad.dailywords.ui.presentation.profile

data class ProfileState(
    val name: String = "Jane Doe",
    val email: String = "jane.doe@example.com",
    val learningLevel: String = "Intermediate",
    val dailyNotifications: Boolean = true,
    val darkMode: Boolean = false,
)
