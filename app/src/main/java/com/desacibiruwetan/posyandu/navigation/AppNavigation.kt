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

                    navController.navigate(Screen.Warga.route)
                },
                onNavItemSelected = { index ->

                    when (index) {
                        1 -> navController.navigate(Screen.Warga.route) // Index 1 adalah "Warga"
                        // TODO: Tambahkan navigasi Riwayat (2) dan Profil (3) nanti
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
                    // TODO: Arahkan ke screen Tambah Data Warga (Screen.TambahWarga.route) jika sudah dibuat
                    println("Tombol tambah warga ditekan!")
                }
            )
        }
    }
}