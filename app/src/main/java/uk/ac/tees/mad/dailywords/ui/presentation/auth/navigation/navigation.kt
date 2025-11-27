package uk.ac.tees.mad.dailywords.ui.presentation.auth.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.dailywords.ui.presentation.auth.create_account.CreateAccountRoot
import uk.ac.tees.mad.dailywords.ui.presentation.auth.forgot.ForgotRoot
import uk.ac.tees.mad.dailywords.ui.presentation.auth.login.LoginRoot
import uk.ac.tees.mad.dailywords.ui.presentation.home.HomeRoot

@Composable
fun Navigation(navcontroller: NavHostController){
    val startDestination: Any = if (FirebaseAuth.getInstance().currentUser != null) GraphRoutes.Home else GraphRoutes.Login

    NavHost(navController = navcontroller, startDestination = startDestination){

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

        composable<GraphRoutes.Home> { HomeRoot() }


    }

}
