package com.desacibiruwetan.posyandu.ui.screen.beranda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.navigation.Screen
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.dialog.PilotSelectionDialog
import com.desacibiruwetan.posyandu.ui.components.items.SyncStatusBadge
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.viewmodel.AuthViewmodel

@Composable
fun DashboardScreen(
    onNavigateToCariWarga: () -> Unit,
    onNavigateToCatatKejadian: () -> Unit,
    onNavigateToUpdateKb: () -> Unit,
    onNavigateToUpdateWusPus: () -> Unit,
    onNavigateToUpdateBalita: () -> Unit,
    onNavigateToAdministrasiRt: () -> Unit,
    onNavigateToBumil: () -> Unit,
    onNavigateToRumahKeluarga: () -> Unit,
    onNavigateToPilot: (String) -> Unit,
    onNavItemSelected: (Int) -> Unit,
    authViewModel: AuthViewmodel,
    userName: String
) {
    var showPilotDialog by remember { mutableStateOf(false) }

    if (showPilotDialog) {
        PilotSelectionDialog(
            onDismiss = { showPilotDialog = false },
            onOptionSelected = { option ->
                showPilotDialog = false
                val route = when (option) {
                    "Peduli Stunting" -> Screen.PilotStunting.route
                    "PHBS" -> Screen.PilotPhbs.route
                    "Kesehatan Ibu & Anak" -> Screen.PilotKia.route
                    "Siaga Kebakaran" -> Screen.PilotKebakaran.route
                    "Bencana Alam" -> Screen.PilotBencana.route
                    "Peduli Lingkungan" -> Screen.PilotLingkungan.route
                    "Keluarga Sehat Berkualitas" -> Screen.PilotKeluargaSehat.route
                    "Keuangan Sehat" -> Screen.PilotKeuangan.route
                    "Kesehatan PUS" -> Screen.PilotKesehatanPus.route
                    else -> ""
                }
                if (route.isNotEmpty()) onNavigateToPilot(route)
            }
        )
    }

    Scaffold(
        containerColor = BgMint,
        bottomBar = { AppNavBar(selectedIndex = 0, onItemSelected = onNavItemSelected) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            DashboardHeader(userName = userName)

            SectionTitle("Aksi utama")
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                DashboardAction(
                    title = "Cari Warga",
                    subtitle = "Lihat dan buka data",
                    icon = Icons.Default.People,
                    onClick = onNavigateToCariWarga,
                    modifier = Modifier.weight(1f)
                )
                DashboardAction(
                    title = "Catat Kejadian",
                    subtitle = "Kelahiran, pindah, wafat",
                    icon = Icons.Default.Edit,
                    onClick = onNavigateToCatatKejadian,
                    modifier = Modifier.weight(1f)
                )
            }

            SectionTitle("Kelola data")
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                DashboardRowAction("Rumah & Keluarga", "Kelola rumah, KK, dan anggota", Icons.Default.Home, onNavigateToRumahKeluarga)
                DashboardRowAction("Balita", "Perbarui berat, tinggi, dan orang tua", Icons.Default.ChildCare, onNavigateToUpdateBalita)
                DashboardRowAction("Bumil", "Perbarui data ibu hamil dan ASI", Icons.Default.PregnantWoman, onNavigateToBumil)
                DashboardRowAction("WUS/PUS", "Status pasangan usia subur", Icons.Default.Favorite, onNavigateToUpdateWusPus)
                DashboardRowAction("KB", "Data penggunaan kontrasepsi", Icons.Default.FamilyRestroom, onNavigateToUpdateKb)
                DashboardRowAction("Administrasi RT", "Ringkasan administrasi wilayah", Icons.Default.Groups, onNavigateToAdministrasiRt)
            }

            SectionTitle("Program")
            DashboardRowAction(
                title = "Pilot / indikator program",
                subtitle = "PHBS, stunting, KIA, kebakaran, dan lainnya",
                icon = Icons.Default.HealthAndSafety,
                onClick = { showPilotDialog = true }
            )

            SectionTitle("Riwayat terbaru")
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                HistoryLine("Catat kejadian", "Kelahiran - Sumarsih", "10 Mei 2026")
                HistoryLine("Catat kejadian", "Wafat - Mulyodawg", "11 Mei 2026")
            }

            Spacer(modifier = Modifier.height(56.dp))
        }
    }
}

@Composable
private fun DashboardHeader(userName: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(PrimaryGreen)
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(46.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(SurfaceWhite),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryGreen)
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = userName,
                    style = MaterialTheme.typography.titleMedium,
                    color = SurfaceWhite,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Kader Posyandu Cibiru Wetan",
                    style = MaterialTheme.typography.bodySmall,
                    color = SurfaceWhite.copy(alpha = 0.82f)
                )
            }
            SyncStatusBadge(text = "Tersinkron", isOnline = true)
        }
    }
}

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleSmall,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF272727)
    )
}

@Composable
private fun DashboardAction(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(SurfaceWhite)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(26.dp))
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6D6D6D))
    }
}

@Composable
private fun DashboardRowAction(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceWhite)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(PrimaryGreen.copy(alpha = 0.12f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(22.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = Color(0xFF6D6D6D))
        }
    }
}

@Composable
private fun HistoryLine(type: String, description: String, date: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(SurfaceWhite)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = PrimaryGreen)
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(type, style = MaterialTheme.typography.labelMedium, color = Color(0xFF6D6D6D))
            Text(description, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        }
        Text(date, style = MaterialTheme.typography.labelSmall, color = Color(0xFF6D6D6D))
    }
}
