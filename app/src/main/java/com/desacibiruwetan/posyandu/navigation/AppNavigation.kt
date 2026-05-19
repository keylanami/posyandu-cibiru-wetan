package com.desacibiruwetan.posyandu.navigation

import androidx.compose.runtime.Composable
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

        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToCariWarga = {
                    navController.navigate(Screen.Warga.route) {
                        launchSingleTop = true
                    }
                },
                onNavItemSelected = { index ->
                    when (index) {
                        1 -> navController.navigate(Screen.Warga.route) { launchSingleTop = true }
                        // TODO: Handle index 2 (Riwayat), 3 (Profil)
                    }
                }
            )
        }

        composable(Screen.Warga.route) {
            CariWargaScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onAddWargaClick = {
                    // TODO: Arahkan ke screen Tambah Data Warga
                },
                onNavItemSelected = { index ->
                    when (index) {
                        0 -> {
                            navController.popBackStack(Screen.Dashboard.route, inclusive = false)
                        }
                        // TODO: Handle index 2 (Riwayat), 3 (Profil)
                    }
                }
            )
        }

        composable(Screen.TambahWarga.route) {
            TambahWargaScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = { /* Handle bottom nav routing */ }
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
                onNavItemSelected = { /* Handle bottom nav routing */ }
            )
        }

        composable(Screen.UpdateKb.route) {
            UpdateKbScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = { /* Handle bottom nav routing */ }
            )
        }

        composable(Screen.UpdateWusPus.route) {
            UpdateWusPusScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = { /* Handle bottom nav routing */ }
            )
        }

        composable(Screen.CatatKejadian.route) {
            CatatKejadianScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = { /* Handle bottom nav routing */ }
            )
        }
    }
}