package uk.ac.tees.mad.dailywords.ui.presentation.auth.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class GraphRoutes {
    @Serializable
    data object Login : GraphRoutes()
    @Serializable
    data object Register : GraphRoutes()
    @Serializable
    data object Forgot : GraphRoutes()
    @Serializable
    data object Home: GraphRoutes()
    @Serializable
    data object Practice: GraphRoutes()
}