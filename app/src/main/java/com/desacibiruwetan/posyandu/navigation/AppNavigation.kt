package com.desacibiruwetan.posyandu.navigation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

// Import Network & Repository
import com.desacibiruwetan.posyandu.data.local.database.AppDatabase
import com.desacibiruwetan.posyandu.data.network.ApiConfig
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.data.repository.AnggotaRepository
import com.desacibiruwetan.posyandu.data.repository.AuthRepository
import com.desacibiruwetan.posyandu.data.repository.KeluargaRepository
import com.desacibiruwetan.posyandu.data.repository.OfflineSyncRepository
import com.desacibiruwetan.posyandu.data.repository.RumahRepository
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.AuthViewmodel
import com.desacibiruwetan.posyandu.viewmodel.DataReadViewModel
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.RumahViewmodel
import com.desacibiruwetan.posyandu.viewmodel.UpdateViewmodel
import com.desacibiruwetan.posyandu.utils.SessionManager

// Import Screens
import com.desacibiruwetan.posyandu.ui.screen.auth.LoginScreenWrapper
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
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.desacibiruwetan.posyandu.ui.screen.warga.EditWargaScreen
import kotlinx.coroutines.launch

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val safeNavController = remember(navController) { SafeNavController(navController) }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val startDestination = remember {
        if (SessionManager.getRawToken(context).isNotBlank()) Screen.Dashboard.route else Screen.Login.route
    }
    val database = AppDatabase.getDatabase(context)
    val apiService = ApiConfig.getApiService()
    val offlineSyncRepository = remember {
        OfflineSyncRepository(apiService, database)
    }
    val authViewModel: AuthViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AuthViewmodel(AuthRepository(apiService)) as T
        },
    )

    val rumahViewModel: RumahViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                RumahViewmodel(RumahRepository(apiService, database.rumahDao(), database.syncStateDao())) as T
        },
    )

    val keluargaViewModel: KeluargaViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                KeluargaViewmodel(KeluargaRepository(apiService, database.keluargaDao(), database.rumahDao(), database.syncStateDao())) as T
        },
    )

    val anggotaViewModel: AnggotaViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                AnggotaViewmodel(
                    AnggotaRepository(
                        apiService,
                        database.anggotaDao(),
                        database.keluargaDao(),
                        database.balitaDao(),
                        database.bumilDao(),
                        database.wusPusDao(),
                        database.syncStateDao(),
                    ),
                ) as T
        },
    )

    val dataReadViewModel: DataReadViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                DataReadViewModel(apiService, database.anggotaDao(), context.applicationContext) as T
        },
    )

    val updateViewModel: UpdateViewmodel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                UpdateViewmodel(context.applicationContext as android.app.Application, apiService) as T
        },
    )

    val getMeState by authViewModel.getMeState.collectAsState()
    val currentBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry?.destination?.route

    val cachedUserName = SessionManager.getUserName(context).ifBlank { "Kader" }
    val userName = when (val state = getMeState) {
        is UiState.Success -> {
            val email = state.data.data?.email ?: ""
            if (email.contains("@")) email.substringBefore("@") else email.ifEmpty { "Kader" }
        }

        is UiState.Loading -> cachedUserName
        else -> cachedUserName
    }
    val activeRtRw = buildString {
        val rt = SessionManager.getUserRt(context)
        val rw = SessionManager.getUserRw(context)
        append("RT ")
        append(rt.ifBlank { "-" })
        append(" / RW ")
        append(rw.ifBlank { "-" })
    }

    suspend fun syncPendingThenPull() {
        val rawToken = SessionManager.getRawToken(context)
        if (rawToken.isBlank()) return
        val formattedToken = SessionManager.formatAuthorizationHeader(rawToken)

        // 1. Priority: Upload local offline changes first
        offlineSyncRepository.syncPendingChanges(formattedToken)

        // 2. Fetch latest app-wide metadata
        authViewModel.getMe(rawToken)
        dataReadViewModel.refresh(formattedToken)

        // Note: Heavy domain data (Rumah, Keluarga, Anggota) is synced within specific screens
    }

    suspend fun sendToLogin() {
        database.clearUserData()
        dataReadViewModel.clearCache()
        SessionManager.clearSession(context)
        authViewModel.resetGetMeState()
        authViewModel.resetLogoutState()
        safeNavController.navigate(Screen.Login.route) {
            popUpTo(0) { inclusive = true }
        }
    }

    suspend fun logoutThenSendToLogin() {
        val rawToken = SessionManager.getRawToken(context)
        if (rawToken.isNotBlank()) {
            runCatching {
                apiService.logout(SessionManager.formatAuthorizationHeader(rawToken))
            }
        }
        sendToLogin()
    }

    fun isPublicRoute(route: String?): Boolean {
        return (route == null) || (route == Screen.Login.route) || (route == Screen.Register.route)
    }

    LaunchedEffect(Unit) {
        syncPendingThenPull()
    }

    LaunchedEffect(currentRoute) {
        if (!isPublicRoute(currentRoute) && SessionManager.getRawToken(context).isBlank()) {
            sendToLogin()
        }
    }

    LaunchedEffect(getMeState) {
        when (val state = getMeState) {
            is UiState.Success -> {
                state.data.data?.let { user ->
                    SessionManager.saveUserProfile(context, user)
                }
            }
            is UiState.Error -> {
                val message = state.message
                val isAuthError = message.contains("Unauthorized", ignoreCase = true) ||
                        message.contains("401") ||
                        message.contains("Gagal mengambil data user")

                val isNetworkError = message.contains("Tidak ada internet", ignoreCase = true)

                // ONLY redirect if it's an authentication error and NOT a network error
                if (isAuthError && !isNetworkError) {
                    sendToLogin()
                }
            }
            else -> {}
        }
    }

    DisposableEffect(Unit) {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                coroutineScope.launch {
                    syncPendingThenPull()
                }
            }
        }

        connectivityManager.registerNetworkCallback(request, callback)
        onDispose {
            runCatching { connectivityManager.unregisterNetworkCallback(callback) }
        }
    }

    val handleBottomNav: (Int) -> Unit = { index ->
        val route = when (index) {
            0 -> Screen.Dashboard.route
            1 -> Screen.Warga.route
            2 -> Screen.Riwayat.route
            3 -> Screen.Profil.route
            else -> null
        }

        route?.let {
            navController.navigate(it) {
                launchSingleTop = true
                popUpTo(Screen.Dashboard.route) {
                    inclusive = false
                }
            }
        }
    }

    NavHost(navController = navController, startDestination = startDestination) {
        composable(Screen.Login.route) {
            BackHandler(enabled = true) {
                // Prevent going back from Login to white screen
            }
            LoginScreenWrapper(
                onNavigateToRegister = { safeNavController.navigate(Screen.Register.route) },
                onNavigateToDashboard = {
                    safeNavController.navigate(Screen.Dashboard.route) {
                        popUpTo(0) { inclusive = true }
                    }
                },
                viewmodel = authViewModel,
            )
        }

        composable(Screen.Register.route) {
            RegisterScreenWrapper(
                onNavigateToLogin = { safeNavController.popBackStack() },
                viewmodel = authViewModel,
            )
        }

        composable(Screen.Dashboard.route) {
            BackHandler(enabled = true) {
                // Prevent going back from Dashboard to white screen
            }

            DashboardScreen(
                onNavigateToCariWarga = { safeNavController.navigate(Screen.Warga.route) },
                onNavigateToCatatKejadian = { safeNavController.navigate(Screen.CatatKejadian.route) },
                onNavigateToAdministrasiRt = { safeNavController.navigate(Screen.AdministrasiRt.route) },
                onNavigateToRumahKeluarga = { safeNavController.navigate(Screen.RumahKeluarga.route) },
                onNavigateToPilot = { route -> safeNavController.navigate(route) },
                onNavItemSelected = handleBottomNav,
                userName = userName,
                activeRtRw = activeRtRw,
                dataReadViewModel = dataReadViewModel,
                updateViewModel = updateViewModel,
            )
        }

        composable(Screen.Warga.route) {
            CariWargaScreen(
                onBackClick = { safeNavController.popBackStack() },
                onAddWargaClick = { safeNavController.navigate(Screen.TambahWarga.route) },
                onNavigateToDetailWarga = { localId -> safeNavController.navigate("${Screen.DetailWarga.route}/$localId") },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                rumahViewModel = rumahViewModel,
                keluargaViewModel = keluargaViewModel,
                dataReadViewModel = dataReadViewModel,
                onNavigateToRumahKeluarga = { safeNavController.navigate(Screen.RumahKeluarga.route) },
            ) { route ->
                safeNavController.navigate(route)
            }
        }

        composable(Screen.Riwayat.route) {
            RiwayatScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                userName = userName,
                dataReadViewModel = dataReadViewModel,
            )
        }

        composable(Screen.Profil.route) {
            ProfilScreen(
                onLogoutClick = {
                    coroutineScope.launch {
                        logoutThenSendToLogin()
                    }
                },
                onNavItemSelected = handleBottomNav,
                authViewModel = authViewModel,
            )
        }

        composable(Screen.TambahWarga.route) {
            TambahWargaScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                keluargaViewModel = keluargaViewModel,
            )
        }

        composable(
            route = "${Screen.DetailWarga.route}/{localId}",
            arguments = listOf(
                navArgument("localId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getInt("localId")
            DetailWargaScreen(
                onBackClick = { safeNavController.popBackStack() },
                onCatatKejadianClick = { id -> safeNavController.navigate("${Screen.CatatKejadian.route}/$id") },
                localId = localId,
                anggotaViewModel = anggotaViewModel,
                keluargaViewModel = keluargaViewModel,
                onEditClick = { id -> safeNavController.navigate("edit_warga/$id") },
                onManageBalita = { id -> safeNavController.navigate("${Screen.UpdateBalita.route}/$id") },
                onManageBumil = { id -> safeNavController.navigate("${Screen.UpdateBumil.route}/$id") },
                onManageWusPus = { id -> safeNavController.navigate("${Screen.UpdateWusPus.route}/$id") },
                onManageKb = { id -> safeNavController.navigate("${Screen.UpdateKb.route}/$id") },
            )
        }

        composable(
            route = "edit_warga/{localId}",
            arguments = listOf(
                navArgument("localId") { type = NavType.IntType }
            )
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getInt("localId")
            EditWargaScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                localId = localId,
            )
        }

        composable(Screen.UpdateBalita.route) {
            UpdateBalitaScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
            )
        }
        composable(
            route = "${Screen.UpdateBalita.route}/{localId}",
            arguments = listOf(navArgument("localId") { type = NavType.IntType })
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getInt("localId")
            UpdateBalitaScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                initialLocalId = localId
            )
        }

        composable(Screen.UpdateKb.route) {
            UpdateKbScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
            )
        }
        composable(
            route = "${Screen.UpdateKb.route}/{localId}",
            arguments = listOf(navArgument("localId") { type = NavType.IntType })
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getInt("localId")
            UpdateKbScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                initialLocalId = localId
            )
        }

        composable(Screen.UpdateWusPus.route) {
            UpdateWusPusScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
            )
        }
        composable(
            route = "${Screen.UpdateWusPus.route}/{localId}",
            arguments = listOf(navArgument("localId") { type = NavType.IntType })
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getInt("localId")
            UpdateWusPusScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                initialLocalId = localId
            )
        }

        composable(
            route = "${Screen.CatatKejadian.route}/{localId}",
            arguments = listOf(
                navArgument("localId") {
                    type = NavType.IntType
                    defaultValue = -1
                }
            )
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getInt("localId")?.takeIf { it != -1 }
            CatatKejadianScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                initialLocalId = localId,
            )
        }

        composable(Screen.CatatKejadian.route) {
            CatatKejadianScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                initialLocalId = null,
            )
        }
        composable(Screen.AdministrasiRt.route) {
            AdministrasiRtScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.RumahKeluarga.route) {
            RumahKeluargaScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                rumahViewModel = rumahViewModel,
                keluargaViewModel = keluargaViewModel,
                anggotaViewModel = anggotaViewModel,
            )
        }
        composable(Screen.UpdateBumil.route) {
            UpdateBumilScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
            )
        }
        composable(
            route = "${Screen.UpdateBumil.route}/{localId}",
            arguments = listOf(navArgument("localId") { type = NavType.IntType })
        ) { backStackEntry ->
            val localId = backStackEntry.arguments?.getInt("localId")
            UpdateBumilScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
                anggotaViewModel = anggotaViewModel,
                initialLocalId = localId
            )
        }
        composable(Screen.PilotStunting.route) {
            PilotStuntingScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.PilotPhbs.route) {
            PilotPHBSScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.PilotKia.route) {
            PilotKesBuNakScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.PilotKebakaran.route) {
            PilotSiagaKebakaraanScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.PilotBencana.route) {
            PilotBencanaAlamScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.PilotLingkungan.route) {
            PilotPeduliLingkunganScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.PilotKeluargaSehat.route) {
            PilotKelSehatBerkualitasScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.PilotKeuangan.route) {
            PilotKeuanganSehatScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
        composable(Screen.PilotKesehatanPus.route) {
            PilotKesehatanPusScreen(
                onBackClick = { safeNavController.popBackStack() },
                onNavItemSelected = handleBottomNav,
            )
        }
    }
}
