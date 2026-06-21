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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.model.BumilData
import com.desacibiruwetan.posyandu.data.model.KbData
import com.desacibiruwetan.posyandu.data.network.UiState
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
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.utils.formatDateForDisplay
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.WargaProgramSummary

@Composable
fun DetailWargaScreen(
    onBackClick: () -> Unit,
    onCatatKejadianClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    nikWarga: String? = null,
    anggotaViewModel: AnggotaViewmodel,
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val token = SessionManager.getAuthorizationHeader(context)
    val listWargaAsli by anggotaViewModel.listAnggotaLocal.collectAsState()
    val programSummaryState by anggotaViewModel.programSummaryState.collectAsState()
    val warga = listWargaAsli.find { it.nik == nikWarga }

    LaunchedEffect(warga?.localId, warga?.serverId, token) {
        if (warga != null) {
            anggotaViewModel.loadProgramSummary(token, warga.localId, warga.serverId)
        }
    }

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
            ProgramChips(warga, (programSummaryState as? UiState.Success)?.data)
            ActionPanel(
                onEdit = { onEditClick(warga.nik) },
                onEvent = { onCatatKejadianClick(warga.nik) }
            )
            ProgramDetailSection(programSummaryState)
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
                SummaryPill("Usia", warga.usia ?: "-", Modifier.weight(1f))
                SummaryPill("Relasi", warga.statusKeluarga, Modifier.weight(1f))
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
private fun ProgramChips(warga: AnggotaEntity, summary: WargaProgramSummary?) {
    val programItems = buildList {
        add(warga.kategoriUsia ?: "Umum")
        add(warga.jenisKelamin)
        if (summary?.balita != null) add("Balita")
        if (summary?.bumilLocal != null || summary?.bumilRemote?.isNotEmpty() == true) add("Bumil")
        if (summary?.wusPusLocal != null || summary?.wusPusRemote != null) add("WUS/PUS")
        if (summary?.kbs?.isNotEmpty() == true) add("KB")
    }.distinct()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(22.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Konteks program", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        programItems.chunked(2).forEach { rowItems ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                rowItems.forEach { item ->
                    val icon = when (item) {
                        "Balita" -> Icons.Default.ChildCare
                        "Bumil" -> Icons.Default.PregnantWoman
                        "WUS/PUS", "KB" -> Icons.Default.FamilyRestroom
                        "Laki-laki" -> Icons.Default.Male
                        "Perempuan" -> Icons.Default.Female
                        else -> Icons.Default.Home
                    }
                    val color = when (item) {
                        "KB", "WUS/PUS" -> HealthBlue
                        "Bumil" -> ActionAmber
                        else -> PrimaryGreen
                    }
                    ProgramBadge(item, icon, color, Modifier.weight(1f))
                }
                if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
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
        Text(text, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    }
}

@Composable
private fun ProgramDetailSection(state: UiState<WargaProgramSummary>) {
    when (state) {
        UiState.Idle, UiState.Loading -> Unit
        is UiState.Error -> Unit
        is UiState.Success -> {
            val summary = state.data
            if (!summary.hasData) return

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(22.dp))
                    .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text("Data kesehatan warga", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)

                summary.balita?.let {
                    ProgramInfoCard(
                        title = "Balita",
                        icon = Icons.Default.ChildCare,
                        color = PrimaryGreen,
                        rows = listOf(
                            "Nama ayah" to it.namaAyah,
                            "Nama ibu" to it.namaIbu,
                            "Tinggi badan" to "${it.tinggiBadan} cm",
                            "Berat badan" to "${it.beratBadan} kg"
                        )
                    )
                }

                val bumilRows = summary.bumilRemote.ifEmpty {
                    summary.bumilLocal?.let {
                        listOf(
                            BumilData(
                                id = it.bumilServerId ?: it.idLocalBumil,
                                anggotaId = it.anggotaServerId ?: it.anggotaLocalId,
                                asiEksklusif = it.asiEksklusif,
                                hamilKe = it.hamilKe,
                                tanggalMulaiAsi = it.tanggalMulaiAsi,
                                tanggalSelesaiAsi = it.tanggalSelesaiAsi,
                                createdAt = it.createdAt,
                                updatedAt = it.updatedAt
                            )
                        )
                    }.orEmpty()
                }
                bumilRows.forEach { bumil ->
                    ProgramInfoCard(
                        title = "Bumil",
                        icon = Icons.Default.PregnantWoman,
                        color = ActionAmber,
                        rows = listOf(
                            "Hamil ke" to bumil.hamilKe.toString(),
                            "ASI eksklusif" to if (bumil.asiEksklusif) "Ya" else "Tidak",
                            "Mulai ASI" to formatDateForDisplay(bumil.tanggalMulaiAsi).ifBlank { "-" },
                            "Selesai ASI" to formatDateForDisplay(bumil.tanggalSelesaiAsi).ifBlank { "-" }
                        )
                    )
                }

                (summary.wusPusRemote ?: summary.wusPusLocal)?.let { data ->
                    val title = if (data is com.desacibiruwetan.posyandu.data.model.WusPusData) data.statusKategori else (data as com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity).statusKategori
                    val namaSuami = if (data is com.desacibiruwetan.posyandu.data.model.WusPusData) data.namaSuami else (data as com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity).namaSuami
                    val tanggal = if (data is com.desacibiruwetan.posyandu.data.model.WusPusData) data.tanggalMulaiStatus else (data as com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity).tanggalMulaiStatus
                    val keterangan = if (data is com.desacibiruwetan.posyandu.data.model.WusPusData) data.keterangan else (data as com.desacibiruwetan.posyandu.data.local.entity.WusPusEntity).keterangan
                    ProgramInfoCard(
                        title = title,
                        icon = Icons.Default.FamilyRestroom,
                        color = HealthBlue,
                        rows = listOf(
                            "Nama pasangan" to (namaSuami ?: "-"),
                            "Mulai status" to formatDateForDisplay(tanggal).ifBlank { "-" },
                            "Keterangan" to (keterangan ?: "-")
                        )
                    )
                }

                summary.kbs.forEach { kb ->
                    KbInfoCard(kb)
                }
            }
        }
    }
}

@Composable
private fun ProgramInfoCard(title: String, icon: ImageVector, color: Color, rows: List<Pair<String, String>>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(color.copy(alpha = 0.08f), RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Text(
                title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(start = 8.dp)
            )
        }
        rows.forEach { (label, value) ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(label, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                Text(value, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
    }
}

@Composable
private fun KbInfoCard(kb: KbData) {
    ProgramInfoCard(
        title = "KB ${kb.jenisKb}",
        icon = Icons.Default.FamilyRestroom,
        color = HealthBlue,
        rows = listOf(
            "Status" to if (kb.statusAktif) "Aktif" else "Tidak aktif",
            "Mulai KB" to formatDateForDisplay(kb.tanggalMulaiKb).ifBlank { "-" },
            "Keterangan" to (kb.keterangan ?: "-")
        )
    )
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
