package com.desacibiruwetan.posyandu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.desacibiruwetan.posyandu.ui.screen.auth.LoginScreenWrapper
import com.desacibiruwetan.posyandu.ui.screen.auth.PersonalizationScreen
import com.desacibiruwetan.posyandu.ui.screen.auth.RegisterScreen
import com.desacibiruwetan.posyandu.ui.screen.beranda.DashboardScreen
import com.desacibiruwetan.posyandu.ui.theme.PosyanduCibiruTheme


sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Personalization : Screen("personalization")
    object Dashboard : Screen("dashboard")
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PosyanduCibiruTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {


        composable(Screen.Login.route) {
            LoginScreenWrapper(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                }
            )
        }


        composable(Screen.Register.route) {
            RegisterScreen(
                onNavigateToPersonalization = {
                    navController.navigate(Screen.Personalization.route) {
                        popUpTo(Screen.Register.route) { inclusive = true }
                    }
                },
                onNavigateToLogin = {
                    navController.popBackStack()
                }
            )
        }


        composable(Screen.Personalization.route) {
            PersonalizationScreen(
                onComplete = {
                     navController.navigate(Screen.Dashboard.route) {
                         popUpTo(Screen.Login.route) { inclusive = true }
                     }
                }
            )
        }

        composable(Screen.Dashboard.route) {
                DashboardScreen()
        }
    }
}