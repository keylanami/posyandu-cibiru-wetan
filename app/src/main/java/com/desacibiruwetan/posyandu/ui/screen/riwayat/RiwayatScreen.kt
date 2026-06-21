package com.desacibiruwetan.posyandu.ui.screen.riwayat

import android.content.Context
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.items.RiwayatItemCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.viewmodel.RiwayatViewmodel
import java.text.SimpleDateFormat
import java.util.Locale

// HELPER: Format tarikh ISO ke bentuk yang mudah dibaca (Contoh: 09 Jun 2026, 14:30)
fun parseDate(dateString: String): String {
    if (dateString.isBlank()) return "Baru sahaja"
    return try {
        // Menguruskan ISO Format Laravel: 2026-06-09T00:00:00.000000Z
        val datePart = if (dateString.contains(".")) dateString.substringBefore(".") else dateString
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale("ms", "MY"))
        val date = inputFormat.parse(datePart)
        date?.let { outputFormat.format(it) } ?: dateString.take(10)
    } catch (e: Exception) {
        dateString.take(10)
    }
}

@Composable
fun RiwayatScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String,
    riwayatViewModel: RiwayatViewmodel
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE)
    val token = "Bearer ${sharedPreferences.getString("TOKEN", "")}"

    // Tarik Data Lokal (Paparan terus tanpa memuatkan secara fizikal dari pelayan)
    val listRiwayat by riwayatViewModel.listRiwayatLocal.collectAsState()

    // Cetuskan penyegerakan data di latar belakang
    LaunchedEffect(Unit) {
        riwayatViewModel.fetchRiwayatAktivitas(token)
    }

    Scaffold(
        topBar = { AppTopBar(title = "Riwayat Aktiviti", onBackClick = onBackClick) },
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
                    // Ambil logName (contoh: 'created', 'updated') atau event sebagai tajuk
                    val rawTitle = riwayat.logName ?: riwayat.event ?: "Aktiviti Sistem"

                    // Jadikan huruf pertama huruf besar agar nampak kemas
                    val displayTitle = rawTitle.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
                    }

                    RiwayatItemCard(
                        title = displayTitle,
                        description = riwayat.description,
                        timestamp = parseDate(riwayat.createdAt ?: "")
                    )
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}