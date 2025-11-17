package uk.ac.tees.mad.dailywords.ui.presentation.auth.forgot

data class ForgotState(
    val email: String = "",
    val isLoading: Boolean = false
)