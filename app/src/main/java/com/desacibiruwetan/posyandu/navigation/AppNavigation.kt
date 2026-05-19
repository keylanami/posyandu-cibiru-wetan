package com.desacibiruwetan.posyandu.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.desacibiruwetan.posyandu.ui.screen.auth.LoginScreenWrapper
import com.desacibiruwetan.posyandu.ui.screen.auth.PersonalizationScreen
import com.desacibiruwetan.posyandu.ui.screen.auth.RegisterScreen
import com.desacibiruwetan.posyandu.ui.screen.beranda.DashboardScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.CariWargaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.CatatKejadianScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.DetailWargaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.TambahWargaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.UpdateBalitaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.UpdateKbScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.UpdateWusPusScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    val handleBottomNav: (Int) -> Unit = { index ->
        val route = when (index) {
            0 -> Screen.Dashboard.route
            1 -> Screen.Warga.route
            // 2 -> Screen.Riwayat.route // TODO: Buka komentar jika screen Riwayat sudah ada
            // 3 -> Screen.Profil.route // TODO: Buka komentar jika screen Profil sudah ada
            else -> null
        }

        if (route != null) {
            navController.navigate(route) {

                popUpTo(Screen.Dashboard.route) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

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
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }


        // main flow
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToCariWarga = {
                    handleBottomNav(1)
                },
                onNavItemSelected = handleBottomNav
            )
        }

        composable(Screen.Warga.route) {
            CariWargaScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAddWargaClick = {
                    navController.navigate(Screen.TambahWarga.route)
                },
                onNavItemSelected = handleBottomNav
            )
        }

       // sub-screen
        composable(Screen.TambahWarga.route) {
            TambahWargaScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = handleBottomNav
            )
        }

        composable(Screen.DetailWarga.route) {
            DetailWargaScreen(
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.UpdateBalita.route) {
            UpdateBalitaScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = handleBottomNav
            )
        }

        composable(Screen.UpdateKb.route) {
            UpdateKbScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = handleBottomNav
            )
        }

        composable(Screen.UpdateWusPus.route) {
            UpdateWusPusScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = handleBottomNav
            )
        }

        composable(Screen.CatatKejadian.route) {
            CatatKejadianScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = handleBottomNav
            )
        }
    }
}