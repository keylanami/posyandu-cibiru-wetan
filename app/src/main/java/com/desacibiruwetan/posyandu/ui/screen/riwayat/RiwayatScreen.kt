package com.desacibiruwetan.posyandu.ui.screen.riwayat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.items.RiwayatItemCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint

data class DummyRiwayat(
    val title: String,
    val description: String,
    val location: String,
    val timestamp: String
)

@Composable
fun RiwayatScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    val listRiwayat = remember {
        listOf(
            DummyRiwayat(
                title = "Data KB atta halilintar diperbarui",
                description = "Pembaruan status metode kontrasepsi IUD pada kunjungan rutin",
                location = "RT04 / RW02",
                timestamp = "08:45"
            ),
            DummyRiwayat(
                title = "Pendaftaran Warga Baru",
                description = "Bapak Rafi Ahmad berhasil ditambahkan ke database kader",
                location = "RT04 / RW02",
                timestamp = "Kemarin"
            ),
            DummyRiwayat(
                title = "Perceraian",
                description = "Ibu Rafi Ahmad bercerai",
                location = "RT04 / RW02",
                timestamp = "28 Februari"
            )
        )
    }

    Scaffold(
        topBar = { AppTopBar(title = "Riwayat Aktivitas", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 2, onItemSelected = onNavItemSelected) },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(listRiwayat) { riwayat ->
                    RiwayatItemCard(
                        title = riwayat.title,
                        description = riwayat.description,
                        location = riwayat.location,
                        timestamp = riwayat.timestamp
                    )
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}