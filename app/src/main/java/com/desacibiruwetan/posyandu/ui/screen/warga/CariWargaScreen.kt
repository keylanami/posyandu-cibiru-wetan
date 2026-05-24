package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.model.MockData
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryFab
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.components.input.AppSearchBar
import com.desacibiruwetan.posyandu.ui.components.items.WargaItemCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint

data class DummyWarga(val name: String, val nik: String, val rtRw: String)

@Composable
fun CariWargaScreen(
    onBackClick: () -> Unit,
    onAddWargaClick: () -> Unit,
    onNavigateToDetailWarga: (String) -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredWarga = remember(searchQuery) {
        MockData.listWarga.filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.nik.contains(searchQuery)
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Cari Warga", onBackClick = onBackClick) },
        bottomBar = {
            AppNavBar(
                selectedIndex = 1,
                onItemSelected = onNavItemSelected
            )
        },
        floatingActionButton = {
            PrimaryFab(
                text = "Tambah Warga",
                icon = Icons.Default.Add,
                onClick = onAddWargaClick
            )
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
                EmptyState(
                    icon = Icons.Default.Face,
                    message = "Data warga tidak ditemukan"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(filteredWarga) { warga ->
                        WargaItemCard(
                            name = warga.name,
                            nik = warga.nik,
                            rtRw = warga.rtRw,
                            onClick = { onNavigateToDetailWarga(warga.nik) }
                        )
                    }

                    item { Spacer(modifier = Modifier.height(80.dp)) }
                }
            }
        }
    }
}