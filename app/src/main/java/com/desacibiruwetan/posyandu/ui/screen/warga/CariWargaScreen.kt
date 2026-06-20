package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.People
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryFab
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.components.input.AppSearchBar
import com.desacibiruwetan.posyandu.ui.components.items.WargaItemCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
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

    val context = androidx.compose.ui.platform.LocalContext.current
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
    val filters = listOf("Semua", "Laki-laki", "Perempuan", "Balita")

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

    LaunchedEffect(searchQuery, selectedFilter) { loadedCount = PAGE_SIZE }

    val displayedWarga = remember(filteredWarga, loadedCount) {
        filteredWarga.take(loadedCount)
    }

    val listState = rememberLazyListState()
    val lastVisibleIndex by remember {
        derivedStateOf { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0 }
    }
    LaunchedEffect(lastVisibleIndex) {
        if (lastVisibleIndex >= displayedWarga.size - 1 && displayedWarga.size < filteredWarga.size) {
            loadedCount += PAGE_SIZE
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Warga", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        floatingActionButton = {
            PrimaryFab(text = "Tambah", icon = Icons.Default.Add, onClick = onAddWargaClick)
        },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))

            RegistryHeader(
                total = listWargaAsli.size,
                filtered = filteredWarga.size,
                wilayah = displayRtRw
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(24.dp))
                    .border(1.dp, BorderLight, RoundedCornerShape(24.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AppSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    placeholder = "Cari nama atau NIK"
                )

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FreshTeal,
                                selectedLabelColor = PrimaryGreen
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = selectedFilter == filter,
                                borderColor = BorderLight,
                                selectedBorderColor = PrimaryGreen
                            )
                        )
                    }
                }
            }

            if (filteredWarga.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    EmptyState(icon = Icons.Default.Face, message = "Tidak ada warga sesuai filter")
                }
            } else {
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    item {
                        Text(
                            text = "Menampilkan ${displayedWarga.size} dari ${filteredWarga.size} warga",
                            style = MaterialTheme.typography.labelMedium,
                            color = TextMuted,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                    items(displayedWarga, key = { it.nik }) { warga ->
                        WargaItemCard(
                            name = warga.nama,
                            nik = warga.nik,
                            rtRw = displayRtRw,
                            onClick = { onNavigateToDetailWarga(warga.nik) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(88.dp)) }
                }
            }
        }
    }
}

@Composable
private fun RegistryHeader(total: Int, filtered: Int, wilayah: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .background(SurfaceWhite.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Groups, contentDescription = null, tint = SurfaceWhite)
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(
                text = "Direktori warga",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = SurfaceWhite
            )
            Text(
                text = "$filtered cocok dari $total data",
                style = MaterialTheme.typography.bodySmall,
                color = SurfaceWhite.copy(alpha = 0.76f)
            )
        }
        Column(horizontalAlignment = Alignment.End) {
            Icon(Icons.Default.People, contentDescription = null, tint = SurfaceWhite.copy(alpha = 0.76f))
            Text(wilayah, style = MaterialTheme.typography.labelSmall, color = SurfaceWhite.copy(alpha = 0.76f))
        }
    }
}
