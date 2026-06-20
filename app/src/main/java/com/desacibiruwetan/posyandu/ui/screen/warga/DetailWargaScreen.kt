package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Male
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.components.items.InfoKependudukanCard
import com.desacibiruwetan.posyandu.ui.theme.ActionAmber
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.HealthBlue
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun DetailWargaScreen(
    onBackClick: () -> Unit,
    onCatatKejadianClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    nikWarga: String? = null,
    anggotaViewModel: AnggotaViewmodel,
) {
    val listWargaAsli by anggotaViewModel.listAnggotaLocal.collectAsState()
    val warga = listWargaAsli.find { it.nik == nikWarga }

    Scaffold(
        topBar = { AppTopBar(title = "Detail Warga", onBackClick = onBackClick) },
        containerColor = BgMint
    ) { paddingValues ->
        if (warga == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                EmptyState(icon = Icons.Default.Badge, message = "Data warga tidak ditemukan")
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))
            IdentityHero(warga)
            ProgramChips(warga)
            ActionPanel(
                onEdit = { onEditClick(warga.nik) },
                onEvent = { onCatatKejadianClick(warga.nik) }
            )
            InfoKependudukanCard(warga = warga)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
private fun IdentityHero(warga: AnggotaEntity) {
    val genderIcon = if (warga.jenisKelamin.equals("Laki-laki", ignoreCase = true)) Icons.Default.Male else Icons.Default.Female
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(SurfaceWhite.copy(alpha = 0.12f), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(genderIcon, contentDescription = null, tint = SurfaceWhite, modifier = Modifier.size(30.dp))
                }
                Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                    Text(
                        text = warga.nama,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SurfaceWhite
                    )
                    Text(
                        text = "NIK ${warga.nik}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SurfaceWhite.copy(alpha = 0.78f)
                    )
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                SummaryPill("Usia", "${warga.usia ?: "-"} tahun", Modifier.weight(1f))
                SummaryPill("Keluarga", "#${warga.keluargaId}", Modifier.weight(1f))
                SummaryPill("Status", warga.statusWarga ?: "Aktif", Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun SummaryPill(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = SurfaceWhite.copy(alpha = 0.68f))
        Text(value, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = SurfaceWhite)
    }
}

@Composable
private fun ProgramChips(warga: AnggotaEntity) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(22.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Konteks program", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            ProgramBadge(warga.kategoriUsia ?: "Umum", Icons.Default.ChildCare, PrimaryGreen, Modifier.weight(1f))
            ProgramBadge(warga.jenisKelamin, if (warga.jenisKelamin.equals("Laki-laki", true)) Icons.Default.Male else Icons.Default.Female, HealthBlue, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
            ProgramBadge("Rumah/KK", Icons.Default.Home, ActionAmber, Modifier.weight(1f))
            ProgramBadge("KB/Bumil", Icons.Default.FamilyRestroom, PrimaryGreen, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ProgramBadge(text: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(horizontal = 10.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(7.dp))
        Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun ActionPanel(onEdit: () -> Unit, onEvent: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        CommandRow("Edit identitas", "Perbarui data dasar dan sosial", Icons.Default.Edit, onEdit)
        CommandRow("Catat kejadian", "Kelahiran, pindah, nikah, cerai, meninggal", Icons.Default.AddCircleOutline, onEvent)
        CommandRow("Riwayat warga", "Akan tampil setelah ada catatan kunjungan", Icons.Default.History, {})
    }
}

@Composable
private fun CommandRow(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(18.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(42.dp).background(FreshTeal, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = PrimaryGreen)
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
    }
}
