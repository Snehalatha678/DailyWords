package uk.ac.tees.mad.dailywords.ui.presentation.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uk.ac.tees.mad.dailywords.ui.presentation.auth.create_account.CreateAccountRoot
import uk.ac.tees.mad.dailywords.ui.presentation.auth.forgot.ForgotRoot
import uk.ac.tees.mad.dailywords.ui.presentation.auth.login.LoginRoot
import uk.ac.tees.mad.dailywords.ui.presentation.home.HomeRoot
import uk.ac.tees.mad.dailywords.ui.presentation.practice.PracticeRoot
import uk.ac.tees.mad.dailywords.ui.presentation.profile.ProfileRoot
import uk.ac.tees.mad.dailywords.ui.presentation.quiz.QuizRoot
import uk.ac.tees.mad.dailywords.ui.presentation.splash.SplashRoot

@Composable
fun Navigation(navcontroller: NavHostController){
    NavHost(navController = navcontroller, startDestination = GraphRoutes.Splash){

        composable<GraphRoutes.Splash>{
            SplashRoot(
                onNavigateToHome = {
                    navcontroller.navigate(GraphRoutes.Home){
                        popUpTo(GraphRoutes.Splash) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToLogin = {
                    navcontroller.navigate(GraphRoutes.Login){
                        popUpTo(GraphRoutes.Splash) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<GraphRoutes.Login>{
         LoginRoot(
             onLoginSuccess = {
                 navcontroller.navigate(GraphRoutes.Home){
                     popUpTo(GraphRoutes.Login) {
                         inclusive = true
                     }
                 }
                              },
             onGoToCreateAccount = { navcontroller.navigate(GraphRoutes.Register) },
             onGoToForgotPassword = { navcontroller.navigate(GraphRoutes.Forgot) }
         )
        }

        composable<GraphRoutes.Register>{
            CreateAccountRoot(
                onSignInClick = {
                    navcontroller.navigate(GraphRoutes.Login) {
                        popUpTo(GraphRoutes.Register) {
                            inclusive = true
                        }
                    }
                },
                onCreateAccountSuccess = {
                    navcontroller.navigate(GraphRoutes.Login) {
                        popUpTo(GraphRoutes.Register) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<GraphRoutes.Forgot>{
            ForgotRoot(
                onBackToLogin = {
                    navcontroller.navigate(GraphRoutes.Login) {
                        popUpTo(GraphRoutes.Forgot) {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<GraphRoutes.Home> { 
            HomeRoot(
                onNavigateToPractice = { word, phonetic ->
                    navcontroller.navigate(GraphRoutes.Practice(word, phonetic))
                },
                onNavigateToProfile = { navcontroller.navigate(GraphRoutes.Profile) },
                onNavigateToQuiz = { navcontroller.navigate(GraphRoutes.Quiz) }
            )
        }

        composable<GraphRoutes.Practice> { 
            PracticeRoot(
                onBackClick = { navcontroller.navigateUp() }
            )
        }

        composable<GraphRoutes.Quiz> {
            QuizRoot(
                onNavigateToHome = { navcontroller.navigate(GraphRoutes.Home) {
                    popUpTo(GraphRoutes.Quiz) { inclusive = true }
                } },
                onNavigateToPractice = { navcontroller.navigate(GraphRoutes.Practice("","")) {
                    popUpTo(GraphRoutes.Quiz) { inclusive = true }
                } },
                onNavigateToProfile = { navcontroller.navigate(GraphRoutes.Profile) {
                    popUpTo(GraphRoutes.Quiz) { inclusive = true }
                } }
            )
        }

        composable<GraphRoutes.Profile> { 
            ProfileRoot(
                onLogout = {
                    navcontroller.navigate(GraphRoutes.Login) {
                        popUpTo(GraphRoutes.Home) {
                            inclusive = true
                        }
                    }
                },
                onNavigateToHome = { navcontroller.navigate(GraphRoutes.Home) },
                onNavigateToPractice = { navcontroller.navigate(GraphRoutes.Practice("", "")) },
                onNavigateToQuiz = { navcontroller.navigate(GraphRoutes.Quiz) }
            )
        }


    }

}
