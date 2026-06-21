package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryFab
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.components.input.AppSearchBar
import com.desacibiruwetan.posyandu.ui.theme.ActionAmber
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.HealthBlue
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceMuted
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.DataReadViewModel
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.ReadCollection
import com.desacibiruwetan.posyandu.viewmodel.ReadRecord
import com.desacibiruwetan.posyandu.viewmodel.RumahViewmodel

private data class DetailInfo(
    val title: String,
    val subtitle: String,
    val rows: List<Pair<String, String>>
)

@Composable
fun CariWargaScreen(
    onBackClick: () -> Unit,
    onAddWargaClick: () -> Unit,
    onNavigateToDetailWarga: (String) -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    rumahViewModel: RumahViewmodel,
    keluargaViewModel: KeluargaViewmodel,
    dataReadViewModel: DataReadViewModel,
    onNavigateToRumahKeluarga: () -> Unit,
    onNavigateToUpdateBalita: () -> Unit,
    onNavigateToUpdateBumil: () -> Unit,
    onNavigateToUpdateWusPus: () -> Unit,
    onNavigateToUpdateKb: () -> Unit,
    onNavigateToProgram: (String) -> Unit,
) {
    var query by remember { mutableStateOf("") }
    var section by remember { mutableStateOf("Warga") }
    var selectedDetail by remember { mutableStateOf<DetailInfo?>(null) }
    val sections = listOf("Warga", "Rumah", "Keluarga", "Kesehatan", "Program", "Log")

    val context = LocalContext.current
    val rawToken = SessionManager.getRawToken(context)
    val token = SessionManager.formatAuthorizationHeader(rawToken)

    val warga by anggotaViewModel.listAnggotaLocal.collectAsState()
    val rumah by rumahViewModel.listRumahLocal.collectAsState()
    val keluarga by keluargaViewModel.listKeluargaLocal.collectAsState()
    val readState by dataReadViewModel.readState.collectAsState()

    LaunchedEffect(section) {
        selectedDetail = null
    }

    LaunchedEffect(rawToken) {
        if (rawToken.isNotEmpty()) {
            anggotaViewModel.syncDataAnggotaDariServer(token)
            rumahViewModel.syncDataRumah(token)
            keluargaViewModel.syncDataKeluarga(token)
            dataReadViewModel.refresh(token)
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Pusat Data", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        floatingActionButton = {
            PrimaryFab(text = "Tambah", icon = Icons.Default.Add, onClick = onAddWargaClick)
        },
        containerColor = BgMint
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }
            item {
                DataHero(
                    wargaCount = warga.size,
                    rumahCount = rumah.size,
                    keluargaCount = keluarga.size,
                    onRefresh = { dataReadViewModel.refresh(token) }
                )
            }
            item {
                AppSearchBar(
                    query = query,
                    onQueryChange = { query = it },
                    placeholder = "Cari warga, rumah, KK, atau data program"
                )
            }
            item {
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    sections.forEach { item ->
                        FilterChip(
                            selected = section == item,
                            onClick = { section = item },
                            label = { Text(item) },
                            leadingIcon = if (section == item) {
                                { Icon(Icons.Default.FilterList, contentDescription = null, modifier = Modifier.size(16.dp)) }
                            } else null,
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FreshTeal,
                                selectedLabelColor = PrimaryGreen
                            )
                        )
                    }
                }
            }
            selectedDetail?.let { detail ->
                item {
                    DetailPanel(detail = detail, onClose = { selectedDetail = null })
                }
            }

            when (section) {
                "Warga" -> {
                    val filtered = warga.filter {
                        query.isBlank() || it.nama.contains(query, true) || it.nik.contains(query)
                    }
                    item { SectionIntro("Registry warga", "${filtered.size} data cocok", Icons.Default.People, PrimaryGreen) }
                    if (filtered.isEmpty()) {
                        item { EmptyState(icon = Icons.Default.People, message = "Tidak ada warga ditemukan") }
                    } else {
                        items(filtered, key = { it.nik }) { item ->
                            ResidentReadRow(item, onClick = { onNavigateToDetailWarga(item.nik) })
                        }
                    }
                }

                "Rumah" -> {
                    val filtered = rumah.filter {
                        query.isBlank() || it.alamat.orEmpty().contains(query, true) || it.noRumah?.toString().orEmpty().contains(query)
                    }
                    item { SectionIntro("Rumah", "${filtered.size} rumah dari hasil sinkron", Icons.Default.Home, HealthBlue) }
                    item {
                        CommandBanner("Tambah / edit rumah dan keluarga", "Buka modul rumah untuk membuat rumah, KK, dan anggota keluarga.", Icons.Default.Home, onNavigateToRumahKeluarga)
                    }
                    items(filtered, key = { it.localId }) { item ->
                        val relatedFamilies = keluarga.filter { it.belongsToRumah(item) }
                        RumahReadRow(
                            item = item,
                            familyCount = relatedFamilies.size,
                            onClick = {
                                selectedDetail = DetailInfo(
                                    title = "Rumah ${item.noRumah ?: "-"}",
                                    subtitle = item.alamat ?: "Alamat belum diisi",
                                    rows = listOf(
                                        "Jumlah keluarga" to "${relatedFamilies.size}",
                                        "RT ID" to (item.rtId?.toString() ?: "-"),
                                        "Server ID" to (item.serverId?.toString() ?: "Belum sinkron"),
                                        "Status" to if (item.isSynced) "Tersinkron" else "Tersimpan lokal",
                                        "Dibuat" to (item.createdAt ?: "-"),
                                        "Diubah" to (item.updatedAt ?: "-")
                                    ) + relatedFamilies.mapIndexed { index, family ->
                                        "Keluarga ${index + 1}" to "KK ${family.noKK}"
                                    }
                                )
                            }
                        )
                    }
                }

                "Keluarga" -> {
                    val filtered = keluarga.filter { query.isBlank() || it.noKK.contains(query, true) || it.rumahId.toString().contains(query) }
                    item { SectionIntro("Keluarga", "${filtered.size} kartu keluarga", Icons.Default.Groups, ActionAmber) }
                    item {
                        CommandBanner("Kelola KK", "Tambah keluarga dari modul rumah, lalu isi anggota di dalamnya.", Icons.Default.Groups, onNavigateToRumahKeluarga)
                    }
                    items(filtered, key = { it.localId }) { item ->
                        val members = warga.filter { it.belongsToKeluarga(item) }
                        KeluargaReadRow(
                            item = item,
                            memberCount = members.size,
                            onClick = {
                                selectedDetail = DetailInfo(
                                    title = "KK ${item.noKK}",
                                    subtitle = "Rumah ${item.rumahId}",
                                    rows = listOf(
                                        "Jumlah anggota" to "${members.size}",
                                        "Tempat tinggal" to if (item.isNgontrak) "Ngontrak" else "Milik sendiri",
                                        "Kategori Gakin" to if (item.isGakin == true) "Ya" else "Tidak",
                                        "Server ID" to (item.serverId?.toString() ?: "Belum sinkron"),
                                        "Status" to if (item.isSynced) "Tersinkron" else "Tersimpan lokal",
                                        "Dibuat" to (item.createdAt ?: "-")
                                    ) + members.mapIndexed { index, member ->
                                        "Anggota ${index + 1}" to "${member.nama} - ${member.statusKeluarga}"
                                    }
                                )
                            }
                        )
                    }
                }

                "Kesehatan" -> {
                    item { SectionIntro("Data kesehatan", "Lihat dan update data sasaran warga", Icons.Default.HealthAndSafety, PrimaryGreen) }
                    item { HealthActionGrid(onNavigateToUpdateBalita, onNavigateToUpdateBumil, onNavigateToUpdateWusPus, onNavigateToUpdateKb) }
                    readCollections(readState, query, setOf("balita", "bumil", "wuspus", "kb")) { collection, record ->
                        selectedDetail = record.toDetail(collection.title)
                    }
                }

                "Program" -> {
                    item { SectionIntro("Program dan pilot", "Baca indikator, lalu input periode baru", Icons.Default.HealthAndSafety, HealthBlue) }
                    item {
                        ProgramActionGrid(onNavigateToProgram)
                    }
                    readCollections(readState, query, setOf("kia", "phbs", "stunting", "kebakaran")) { collection, record ->
                        selectedDetail = record.toDetail(collection.title)
                    }
                }

                "Log" -> {
                    item { SectionIntro("Log aktivitas", "Riwayat aktivitas sistem dan kader", Icons.Default.Refresh, TextMuted) }
                    readCollections(readState, query, setOf("logs")) { collection, record ->
                        selectedDetail = record.toDetail(collection.title)
                    }
                }
            }
            item { Spacer(modifier = Modifier.height(88.dp)) }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.readCollections(
    state: UiState<List<ReadCollection>>,
    query: String,
    keys: Set<String>,
    onRecordClick: (ReadCollection, ReadRecord) -> Unit
) {
    when (state) {
        UiState.Idle, UiState.Loading -> item {
            ReadLoadingCard()
        }

        is UiState.Error -> item {
            ReadErrorCard(state.message)
        }

        is UiState.Success -> {
            val collections = state.data.filter { it.key in keys }
                .filter { query.isBlank() || it.title.contains(query, true) || it.records.any { record -> record.title.contains(query, true) || record.subtitle.contains(query, true) } }
            if (collections.isEmpty()) {
                item { EmptyState(icon = Icons.Default.ErrorOutline, message = "Tidak ada data yang cocok") }
            } else {
                items(collections, key = { it.key }) { collection ->
                    CollectionCard(collection = collection, onRecordClick = { onRecordClick(collection, it) })
                }
            }
        }
    }
}

@Composable
private fun DataHero(wargaCount: Int, rumahCount: Int, keluargaCount: Int, onRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(28.dp))
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Pusat data posyandu", style = MaterialTheme.typography.labelLarge, color = SurfaceWhite.copy(alpha = 0.72f))
                    Text("Browse dulu, update setelah jelas", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = SurfaceWhite)
                }
                IconButton(onClick = onRefresh) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = SurfaceWhite)
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                HeroMetric("Warga", wargaCount.toString(), Modifier.weight(1f))
                HeroMetric("Rumah", rumahCount.toString(), Modifier.weight(1f))
                HeroMetric("KK", keluargaCount.toString(), Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun HeroMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SurfaceWhite.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
            .padding(12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = SurfaceWhite.copy(alpha = 0.66f))
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SurfaceWhite)
    }
}

@Composable
private fun SectionIntro(title: String, subtitle: String, icon: ImageVector, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(20.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(42.dp).background(color.copy(alpha = 0.12f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = color)
        }
        Column(modifier = Modifier.padding(start = 12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
    }
}

@Composable
private fun ResidentReadRow(item: AnggotaEntity, onClick: () -> Unit) {
    ReadRow(
        title = item.nama,
        subtitle = item.nik,
        meta = "${item.jenisKelamin} - ${item.kategoriUsia ?: "Umum"}",
        icon = Icons.Default.People,
        color = PrimaryGreen,
        onClick = onClick
    )
}

@Composable
private fun RumahReadRow(item: RumahEntity, familyCount: Int, onClick: () -> Unit) {
    ReadRow(
        title = "Rumah ${item.noRumah ?: "-"}",
        subtitle = item.alamat ?: "Alamat belum diisi",
        meta = "$familyCount keluarga - ${if (item.isSynced) "Tersinkron" else "Lokal"}",
        icon = Icons.Default.Home,
        color = HealthBlue,
        onClick = onClick
    )
}

@Composable
private fun KeluargaReadRow(item: KeluargaEntity, memberCount: Int, onClick: () -> Unit) {
    ReadRow(
        title = "KK ${item.noKK}",
        subtitle = "Rumah ${item.rumahId}",
        meta = listOf("$memberCount anggota", if (item.isNgontrak) "Ngontrak" else "Tetap", if (item.isGakin == true) "Gakin" else "Non-gakin").joinToString(" - "),
        icon = Icons.Default.Groups,
        color = ActionAmber,
        onClick = onClick
    )
}

@Composable
private fun ReadRow(title: String, subtitle: String, meta: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(18.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(42.dp).background(color.copy(alpha = 0.1f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Icon(icon, contentDescription = null, tint = color)
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Text(meta, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
    }
}

@Composable
private fun CommandBanner(title: String, subtitle: String, icon: ImageVector, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(FreshTeal, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryGreen)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = PrimaryGreen)
    }
}

@Composable
private fun HealthActionGrid(
    balita: () -> Unit,
    bumil: () -> Unit,
    wuspus: () -> Unit,
    kb: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MiniAction("Balita", Icons.Default.ChildCare, balita, Modifier.weight(1f))
            MiniAction("Bumil", Icons.Default.PregnantWoman, bumil, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MiniAction("WUS/PUS", Icons.Default.Favorite, wuspus, Modifier.weight(1f))
            MiniAction("KB", Icons.Default.FamilyRestroom, kb, Modifier.weight(1f))
        }
    }
}

@Composable
private fun ProgramActionGrid(onNavigateToProgram: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MiniAction("PHBS", Icons.Default.HealthAndSafety, { onNavigateToProgram("pilot_phbs") }, Modifier.weight(1f))
            MiniAction("Stunting", Icons.Default.ChildCare, { onNavigateToProgram("pilot_stunting") }, Modifier.weight(1f))
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            MiniAction("KIA", Icons.Default.PregnantWoman, { onNavigateToProgram("pilot_kia") }, Modifier.weight(1f))
            MiniAction("Kebakaran", Icons.Default.LocalFireDepartment, { onNavigateToProgram("pilot_kebakaran") }, Modifier.weight(1f))
        }
    }
}

@Composable
private fun MiniAction(title: String, icon: ImageVector, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(SurfaceMuted, RoundedCornerShape(16.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CollectionCard(collection: ReadCollection, onRecordClick: (ReadRecord) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(22.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(collection.title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(collection.description, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            Box(modifier = Modifier.background(FreshTeal, RoundedCornerShape(999.dp)).padding(horizontal = 12.dp, vertical = 7.dp)) {
                Text("${collection.count}", style = MaterialTheme.typography.labelLarge, color = PrimaryGreen, fontWeight = FontWeight.Bold)
            }
        }
        if (collection.records.isEmpty()) {
            Text("Belum ada data tersimpan.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        } else {
            collection.records.forEach { record ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BgMint, RoundedCornerShape(14.dp))
                        .clickable { onRecordClick(record) }
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(record.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(record.subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Text(record.meta, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
            }
        }
    }
}

@Composable
private fun ReadLoadingCard() {
    Text(
        text = "Memuat data terbaru...",
        style = MaterialTheme.typography.bodyMedium,
        color = TextMuted,
        modifier = Modifier.fillMaxWidth().background(SurfaceWhite, RoundedCornerShape(18.dp)).padding(16.dp)
    )
}

@Composable
private fun ReadErrorCard(message: String) {
    Text(
        text = "Gagal memuat data terbaru: $message",
        style = MaterialTheme.typography.bodyMedium,
        color = TextMuted,
        modifier = Modifier.fillMaxWidth().background(SurfaceWhite, RoundedCornerShape(18.dp)).padding(16.dp)
    )
}

@Composable
private fun DetailPanel(detail: DetailInfo, onClose: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text(detail.title, style = MaterialTheme.typography.titleMedium, color = SurfaceWhite, fontWeight = FontWeight.Bold)
                Text(detail.subtitle, style = MaterialTheme.typography.bodySmall, color = SurfaceWhite.copy(alpha = 0.72f))
            }
            IconButton(onClick = onClose) {
                Icon(Icons.Default.Close, contentDescription = "Tutup", tint = SurfaceWhite)
            }
        }
        detail.rows.forEach { (label, value) ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite.copy(alpha = 0.1f), RoundedCornerShape(14.dp))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(label, style = MaterialTheme.typography.labelMedium, color = SurfaceWhite.copy(alpha = 0.72f))
                Text(value, style = MaterialTheme.typography.labelMedium, color = SurfaceWhite, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun ReadRecord.toDetail(collectionTitle: String): DetailInfo =
    DetailInfo(
        title = title,
        subtitle = collectionTitle,
        rows = listOf(
            "ID" to id,
            "Ringkasan" to subtitle,
            "Keterangan" to meta
        )
    )

private fun KeluargaEntity.belongsToRumah(rumah: RumahEntity): Boolean {
    val possibleIds = setOfNotNull(rumah.serverId, rumah.localId)
    return rumahId in possibleIds
}

private fun AnggotaEntity.belongsToKeluarga(keluarga: KeluargaEntity): Boolean {
    val possibleIds = setOfNotNull(keluarga.serverId, keluarga.localId)
    return keluargaId in possibleIds
}
