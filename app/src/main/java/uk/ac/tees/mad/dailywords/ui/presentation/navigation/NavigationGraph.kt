package uk.ac.tees.mad.dailywords.ui.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.dailywords.ui.presentation.home.HomeRoot
import uk.ac.tees.mad.dailywords.ui.presentation.profile.ProfileScreen

@Composable
fun NavigationGraph(navController: NavHostController) {
    NavHost(navController, startDestination = BottomNavItem.Home.route) {
        composable(BottomNavItem.Home.route) {
            HomeRoot()
        }
        composable(BottomNavItem.Profile.route) {
            ProfileScreen()
        }
    }
}
