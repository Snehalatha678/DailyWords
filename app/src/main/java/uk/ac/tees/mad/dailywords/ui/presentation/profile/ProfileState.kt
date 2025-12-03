package uk.ac.tees.mad.dailywords.ui.presentation.profile

data class ProfileState(
    val name: String = "",
    val email: String = "",
    val learningLevel: String = "",
    val dailyNotifications: Boolean = true,
    val darkMode: Boolean = false,
    val profileImage: Any? = null
)
