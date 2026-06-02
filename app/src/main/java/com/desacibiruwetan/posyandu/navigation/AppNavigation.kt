package com.desacibiruwetan.posyandu.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

// Import Network & Repository
import com.desacibiruwetan.posyandu.data.local.database.AppDatabase
import com.desacibiruwetan.posyandu.data.network.ApiConfig
import com.desacibiruwetan.posyandu.data.repository.AnggotaRepository
import com.desacibiruwetan.posyandu.data.repository.AuthRepository
import com.desacibiruwetan.posyandu.data.repository.KeluargaRepository
import com.desacibiruwetan.posyandu.data.repository.RumahRepository
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.AuthViewmodel
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.RumahViewmodel

// Import Screens
import com.desacibiruwetan.posyandu.ui.screen.auth.LoginScreenWrapper
import com.desacibiruwetan.posyandu.ui.screen.auth.PersonalizationScreen
import com.desacibiruwetan.posyandu.ui.screen.auth.RegisterScreenWrapper
import com.desacibiruwetan.posyandu.ui.screen.beranda.DashboardScreen
import com.desacibiruwetan.posyandu.ui.screen.profile.ProfilScreen
import com.desacibiruwetan.posyandu.ui.screen.riwayat.RiwayatScreen

// Import Warga Screens
import com.desacibiruwetan.posyandu.ui.screen.warga.AdministrasiRtScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.CariWargaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.CatatKejadianScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.DetailWargaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotBencanaAlamScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotKelSehatBerkualitasScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotKesBuNakScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotKesehatanPusScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotKeuanganSehatScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotPHBSScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotPeduliLingkunganScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotSiagaKebakaraanScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.PilotStuntingScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.RumahKeluargaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.TambahWargaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.UpdateBalitaScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.UpdateBumilScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.UpdateKbScreen
import com.desacibiruwetan.posyandu.ui.screen.warga.UpdateWusPusScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current
    val database = AppDatabase.getDatabase(context)
    val apiService = ApiConfig.getApiService()

    val authViewModel: AuthViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = AuthRepository(apiService)
                return AuthViewmodel(repository) as T
            }
        }
    )

    val rumahViewModel: RumahViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = RumahRepository(apiService, database.rumahDao())
                return RumahViewmodel(repository) as T
            }
        }
    )

    val keluargaViewModel: KeluargaViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = KeluargaRepository(apiService, database.keluargaDao())
                return KeluargaViewmodel(repository) as T
            }
        }
    )

    val anggotaViewModel: AnggotaViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val repository = AnggotaRepository(apiService, database.anggotaDao())
                return AnggotaViewmodel(repository) as T
            }
        }
    )

    val handleBottomNav: (Int) -> Unit = { index ->
        val route = when (index) {
            0 -> Screen.Dashboard.route
            1 -> Screen.Warga.route
            2 -> Screen.Riwayat.route
            3 -> Screen.Profil.route
            else -> null
        }

        if (route != null) {
            navController.navigate(route) {
                popUpTo(navController.graph.findStartDestination().id) {
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
                onNavigateToRegister = { navController.navigate(Screen.Register.route) },
                onNavigateToDashboard = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewmodel = authViewModel
            )
        }

        composable(Screen.Register.route) {
            RegisterScreenWrapper(
                onNavigateToLogin = { navController.popBackStack() },
                viewmodel = authViewModel
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
                onNavigateToCariWarga = { navController.navigate(Screen.Warga.route) },
                onNavigateToCatatKejadian = { navController.navigate(Screen.CatatKejadian.route) },
                onNavigateToUpdateKb = { navController.navigate(Screen.UpdateKb.route) },
                onNavigateToUpdateBalita = { navController.navigate(Screen.UpdateBalita.route) },
                onNavigateToUpdateWusPus = { navController.navigate(Screen.UpdateWusPus.route) },
                onNavigateToAdministrasiRt = { navController.navigate(Screen.AdministrasiRt.route) },
                onNavigateToBumil = { navController.navigate(Screen.UpdateBumil.route) },
                onNavigateToRumahKeluarga = { navController.navigate(Screen.RumahKeluarga.route) },
                onNavigateToPilot = { route -> navController.navigate(route) },
                onNavItemSelected = handleBottomNav
            )
        }

        composable(Screen.Warga.route) {
            CariWargaScreen(
                onBackClick = { navController.popBackStack() },
                onAddWargaClick = { navController.navigate(Screen.TambahWarga.route) },
                onNavigateToDetailWarga = { nikWarga ->
                    navController.navigate("${Screen.DetailWarga.route}/$nikWarga")
                },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel
            )
        }

        composable(Screen.Riwayat.route) {
            RiwayatScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = handleBottomNav
            )
        }

        composable(Screen.Profil.route) {
            ProfilScreen(
                onBackClick = { navController.popBackStack() },
                onLogoutClick = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                onNavItemSelected = handleBottomNav
            )
        }

        composable(Screen.TambahWarga.route) {
            TambahWargaScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel
            )
        }

        composable("${Screen.DetailWarga.route}/{nik}") { backStackEntry ->
            val nik = backStackEntry.arguments?.getString("nik")
            DetailWargaScreen(
                onBackClick = { navController.popBackStack() },
                nikWarga = nik,
                anggotaViewModel = anggotaViewModel
            )
        }

        composable(Screen.UpdateBalita.route) {
            UpdateBalitaScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }

        composable(Screen.UpdateKb.route) {
            UpdateKbScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }

        composable(Screen.UpdateWusPus.route) {
            UpdateWusPusScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }

        composable(Screen.CatatKejadian.route) {
            CatatKejadianScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }

        composable(Screen.AdministrasiRt.route) {
            AdministrasiRtScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }

        composable(Screen.RumahKeluarga.route) {
            RumahKeluargaScreen(
                onBackClick = { navController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                rumahViewModel = rumahViewModel,
                keluargaViewModel = keluargaViewModel
            )
        }

        composable(Screen.UpdateBumil.route) {
            UpdateBumilScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }

        composable(Screen.PilotStunting.route) {
            PilotStuntingScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
        composable(Screen.PilotPhbs.route) {
            PilotPHBSScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
        composable(Screen.PilotKia.route) {
            PilotKesBuNakScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
        composable(Screen.PilotKebakaran.route) {
            PilotSiagaKebakaraanScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
        composable(Screen.PilotBencana.route) {
            PilotBencanaAlamScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
        composable(Screen.PilotLingkungan.route) {
            PilotPeduliLingkunganScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
        composable(Screen.PilotKeluargaSehat.route) {
            PilotKelSehatBerkualitasScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
        composable(Screen.PilotKeuangan.route) {
            PilotKeuanganSehatScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
        composable(Screen.PilotKesehatanPus.route) {
            PilotKesehatanPusScreen(onBackClick = { navController.popBackStack() }, onNavItemSelected = handleBottomNav)
        }
    }
}
