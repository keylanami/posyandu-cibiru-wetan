package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryFab
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.components.input.AppSearchBar
import com.desacibiruwetan.posyandu.ui.components.items.WargaItemCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

private const val PAGE_SIZE = 10

@Composable
fun CariWargaScreen(
    onBackClick: () -> Unit,
    onAddWargaClick: () -> Unit,
    onNavigateToDetailWarga: (String) -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Semua") }
    var loadedCount by remember { mutableIntStateOf(PAGE_SIZE) }

    val context = LocalContext.current
    val sharedPreferences = remember { SessionManager.getPreferences(context) }
    val rawToken = SessionManager.getRawToken(context)
    val userRt = sharedPreferences.getString("USER_RT", "00") ?: "00"
    val userRw = sharedPreferences.getString("USER_RW", "00") ?: "00"
    val displayRtRw = "RT $userRt / RW $userRw"

    LaunchedEffect(Unit) {
        if (rawToken.isNotEmpty()) {
            anggotaViewModel.syncDataAnggotaDariServer(SessionManager.formatAuthorizationHeader(rawToken))
        }
    }

    val listWargaAsli by anggotaViewModel.listAnggotaLocal.collectAsState()

    val filteredWarga = remember(searchQuery, selectedFilter, listWargaAsli) {
        listWargaAsli.filter { anggota ->
            val matchesQuery = searchQuery.isBlank() ||
                anggota.nama.contains(searchQuery, ignoreCase = true) ||
                anggota.nik.contains(searchQuery)
            val matchesFilter = when (selectedFilter) {
                "Laki-laki" -> anggota.jenisKelamin.equals("Laki-laki", ignoreCase = true)
                "Perempuan" -> anggota.jenisKelamin.equals("Perempuan", ignoreCase = true)
                "Balita" -> anggota.kategoriUsia?.contains("balita", ignoreCase = true) == true
                else -> true
            }
            matchesQuery && matchesFilter
        }
    }

    // reset pagination kalau query berubah
    LaunchedEffect(searchQuery, selectedFilter) { loadedCount = PAGE_SIZE }

    val displayedWarga = remember(filteredWarga, loadedCount) {
        filteredWarga.take(loadedCount)
    }

    val listState = rememberLazyListState()

    // load more ketika item terakhir terlihat
    val lastVisibleIndex by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 } }
    LaunchedEffect(lastVisibleIndex) {
        if (lastVisibleIndex >= displayedWarga.size - 1 && displayedWarga.size < filteredWarga.size) {
            loadedCount += PAGE_SIZE
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Cari Warga",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            AppNavBar(
                selectedIndex = 1,
                onItemSelected = onNavItemSelected
            )
        },
        floatingActionButton = {
            PrimaryFab(text = "Tambah Warga", icon = Icons.Default.Add, onClick = onAddWargaClick)
        },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            AppSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Cari nama atau NIK"
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                listOf("Semua", "Laki-laki", "Perempuan", "Balita").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredWarga.isEmpty()) {
                EmptyState(icon = Icons.Default.Face, message = "Data warga tidak ditemukan")
            } else {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayedWarga) { warga ->
                        WargaItemCard(
                            name = warga.nama,
                            nik = warga.nik,
                            rtRw = displayRtRw,
                            onClick = { onNavigateToDetailWarga(warga.nik) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}
