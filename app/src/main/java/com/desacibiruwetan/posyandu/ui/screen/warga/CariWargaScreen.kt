package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryFab
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.components.input.AppSearchBar
import com.desacibiruwetan.posyandu.ui.components.items.WargaItemCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun CariWargaScreen(
    onBackClick: () -> Unit,
    onAddWargaClick: () -> Unit,
    onNavigateToDetailWarga: (String) -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    userName: String?
) {
    var searchQuery by remember { mutableStateOf("") }

    val listWargaAsli by anggotaViewModel.listAnggotaLocal.collectAsState()

    val filteredWarga = remember(searchQuery, listWargaAsli) {
        listWargaAsli.filter { anggota ->
            anggota.nama.contains(searchQuery, ignoreCase = true) ||
                    anggota.nik.contains(searchQuery)
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

            Spacer(modifier = Modifier.height(24.dp))

            if (filteredWarga.isEmpty()) {
                EmptyState(icon = Icons.Default.Face, message = "Data warga tidak ditemukan")
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredWarga) { warga ->
                        WargaItemCard(
                            name = warga.nama,
                            nik = warga.nik,
                            rtRw = warga.statusKeluarga,
                            onClick = { onNavigateToDetailWarga(warga.nik) }
                        )
                    }
                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}