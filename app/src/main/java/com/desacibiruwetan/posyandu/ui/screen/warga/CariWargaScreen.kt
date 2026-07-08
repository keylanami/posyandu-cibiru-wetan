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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
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
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveThreeColumn
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.theme.ActionAmber
import com.desacibiruwetan.posyandu.ui.theme.AgeElderly
import com.desacibiruwetan.posyandu.ui.theme.AgeElderlyContainer
import com.desacibiruwetan.posyandu.ui.theme.AgeProductive
import com.desacibiruwetan.posyandu.ui.theme.AgeProductiveContainer
import com.desacibiruwetan.posyandu.ui.theme.AgeToddler
import com.desacibiruwetan.posyandu.ui.theme.AgeToddlerContainer
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.HealthBlue
import com.desacibiruwetan.posyandu.ui.theme.HistorySlate
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.ProgramPurple
import com.desacibiruwetan.posyandu.ui.theme.ResidentFemale
import com.desacibiruwetan.posyandu.ui.theme.ResidentFemaleContainer
import com.desacibiruwetan.posyandu.ui.theme.ResidentMale
import com.desacibiruwetan.posyandu.ui.theme.ResidentMaleContainer
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.DataReadViewModel
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.ReadCollection
import com.desacibiruwetan.posyandu.viewmodel.ReadRecord
import com.desacibiruwetan.posyandu.viewmodel.RumahViewmodel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private data class DetailInfo(
    val key: String,
    val title: String,
    val subtitle: String,
    val rows: List<Pair<String, String>>
)

private data class DataAddAction(
    val label: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)

@OptIn(ExperimentalMaterial3Api::class)
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
    var isRefreshing by remember { mutableStateOf(false) }
    val sections = listOf("Warga", "Rumah", "Keluarga", "Kesehatan", "Program", "Log")

    val context = LocalContext.current
    val rawToken = SessionManager.getRawToken(context)
    val token = SessionManager.formatAuthorizationHeader(rawToken)

    val warga by anggotaViewModel.listAnggotaLocal.collectAsState()
    val rumah by rumahViewModel.listRumahLocal.collectAsState()
    val keluarga by keluargaViewModel.listKeluargaLocal.collectAsState()
    val readState by dataReadViewModel.readState.collectAsState()

    val isSyncingAnggota by anggotaViewModel.isSyncing.collectAsState()
    val isSyncingRumah by rumahViewModel.isSyncing.collectAsState()
    val isSyncingKeluarga by keluargaViewModel.isSyncing.collectAsState()
    val isLoading = isSyncingAnggota || isSyncingRumah || isSyncingKeluarga || readState is UiState.Loading || isRefreshing

    val addAction = when (section) {
        "Warga" -> DataAddAction("Tambah warga", Icons.Default.Add, onAddWargaClick)
        "Rumah" -> DataAddAction("Tambah rumah", Icons.Default.Home, onNavigateToRumahKeluarga)
        "Keluarga" -> DataAddAction("Tambah KK", Icons.Default.Groups, onNavigateToRumahKeluarga)
        else -> null
    }

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
    LaunchedEffect(readState) {
        if (readState !is UiState.Loading) isRefreshing = false
    }

    fun refreshAll() {
        if (rawToken.isBlank()) {
            isRefreshing = false
            return
        }
        isRefreshing = true
        anggotaViewModel.syncDataAnggotaDariServer(token)
        rumahViewModel.syncDataRumah(token)
        keluargaViewModel.syncDataKeluarga(token)
        dataReadViewModel.refresh(token) {
            isRefreshing = false
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Pusat Data", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        floatingActionButton = {
            addAction?.let { action ->
                PrimaryFab(text = action.label, icon = action.icon, onClick = action.onClick)
            }
        },
        containerColor = BgMint
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = ::refreshAll,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item {
                    DataHero(
                        wargaCount = warga.size,
                        rumahCount = rumah.size,
                        keluargaCount = keluarga.size
                    )
                }
                item {
                    AppSearchBar(
                        query = query,
                        onQueryChange = { query = it },
                        placeholder = "Cari warga, rumah, KK, atau data program",
                        modifier = Modifier.padding(horizontal = 14.dp)
                    )
                }
                item {
                    Row(
                        modifier = Modifier.horizontalScroll(rememberScrollState()).padding(horizontal = 14.dp),
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

                if (isLoading) {
                    item { DataSectionLoading() }
                } else {
                    when (section) {
                        "Warga" -> {
                            val filtered = warga.filter {
                                query.isBlank() || it.nama.contains(query, true) || it.nik.contains(query)
                            }
                            item { SectionIntro("Registry warga", "${filtered.size} data cocok", Icons.Default.People, HealthBlue) }
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
                                query.isBlank() || it.alamat.orEmpty().contains(query, true) || it.noRumah.orEmpty().contains(query, true) || it.dusun.orEmpty().contains(query, true)
                            }
                            item { SectionIntro("Rumah", "${filtered.size} rumah dari hasil sinkron", Icons.Default.Home, HealthBlue) }
                            item {
                                CommandBanner("Tambah / edit rumah dan keluarga", "Buka modul rumah untuk membuat rumah, KK, dan anggota keluarga.", Icons.Default.Home, onNavigateToRumahKeluarga)
                            }
                            items(filtered, key = { it.localId }) { item ->
                                val detailKey = "rumah-${item.localId}"
                                val relatedFamilies = keluarga.filter { it.belongsToRumah(item) }
                                RumahReadRow(
                                    item = item,
                                    familyCount = relatedFamilies.size,
                                    onClick = {
                                        selectedDetail = DetailInfo(
                                            key = detailKey,
                                            title = "Rumah ${item.noRumah ?: "-"}",
                                            subtitle = item.alamat ?: "Alamat belum diisi",
                                            rows = listOf(
                                                "Jumlah keluarga" to "${relatedFamilies.size}",
                                                "Dusun" to (item.dusun ?: "-"),
                                                "Alamat" to (item.alamat ?: "-"),
                                                "Status" to if (item.isSynced) "Tersinkron" else "Tersimpan lokal",
                                                "Dibuat" to formatIndonesianDate(item.createdAt),
                                                "Diubah" to formatIndonesianDate(item.updatedAt)
                                            ) + relatedFamilies.mapIndexed { index, family ->
                                                "Keluarga ${index + 1}" to "KK ${family.noKK}"
                                            }
                                        )
                                    }
                                )
                                if (selectedDetail?.key == detailKey) {
                                    DetailPanel(detail = selectedDetail!!, onClose = { selectedDetail = null })
                                }
                            }
                        }

                        "Keluarga" -> {
                            val filtered = keluarga.filter { query.isBlank() || it.noKK.contains(query, true) || it.rumahId.toString().contains(query) }
                            item { SectionIntro("Keluarga", "${filtered.size} kartu keluarga", Icons.Default.Groups, ActionAmber) }
                            item {
                                CommandBanner("Kelola KK", "Tambah keluarga dari modul rumah, lalu isi anggota di dalamnya.", Icons.Default.Groups, onNavigateToRumahKeluarga)
                            }
                            items(filtered, key = { it.localId }) { item ->
                                val detailKey = "keluarga-${item.localId}"
                                val members = warga.filter { it.belongsToKeluarga(item) }
                                val rumahLabel = rumah.firstOrNull { item.belongsToRumah(it) }?.noRumah?.let { "Rumah $it" }
                                    ?: "Rumah belum dikenali"
                                KeluargaReadRow(
                                    item = item,
                                    memberCount = members.size,
                                    rumahLabel = rumahLabel,
                                    onClick = {
                                        selectedDetail = DetailInfo(
                                            key = detailKey,
                                            title = "KK ${item.noKK}",
                                            subtitle = rumahLabel,
                                            rows = listOf(
                                                "Jumlah anggota" to "${members.size}",
                                                "Rumah" to rumahLabel,
                                                "Tempat tinggal" to item.statusKepemilikanRumah,
                                                "Jamban" to (item.kepemilikanJamban ?: "-"),
                                                "SPAL" to (item.kepemilikanSpal ?: "-"),
                                                "Status ekonomi" to item.statusEkonomi,
                                                "Status" to if (item.isSynced) "Tersinkron" else "Tersimpan lokal",
                                                "Dibuat" to formatIndonesianDate(item.createdAt),
                                                "Diubah" to formatIndonesianDate(item.updatedAt)
                                            ) + members.mapIndexed { index, member ->
                                                "Anggota ${index + 1}" to "${member.nama} - ${member.statusKeluarga}"
                                            }
                                        )
                                    }
                                )
                                if (selectedDetail?.key == detailKey) {
                                    DetailPanel(detail = selectedDetail!!, onClose = { selectedDetail = null })
                                }
                            }
                        }

                        "Kesehatan" -> {
                            item { SectionIntro("Data kesehatan", "Lihat dan update data sasaran warga", Icons.Default.HealthAndSafety, PrimaryGreen) }
                            item { HealthActionGrid(onNavigateToUpdateBalita, onNavigateToUpdateBumil, onNavigateToUpdateWusPus, onNavigateToUpdateKb) }
                            readCollections(readState, query, setOf("balita", "bumil", "wuspus", "kb"), selectedDetail, { selectedDetail = null }) { collection, record, detailKey ->
                                selectedDetail = record.toDetail(collection.title, detailKey)
                            }
                        }

                        "Program" -> {
                            item { SectionIntro("Program dan pilot", "Baca indikator, lalu input periode baru", Icons.Default.HealthAndSafety, ProgramPurple) }
                            item {
                                ProgramActionGrid(onNavigateToProgram)
                            }
                            readCollections(readState, query, setOf("kia", "phbs", "stunting", "kebakaran"), selectedDetail, { selectedDetail = null }) { collection, record, detailKey ->
                                selectedDetail = record.toDetail(collection.title, detailKey)
                            }
                        }

                        "Log" -> {
                            item { SectionIntro("Log aktivitas", "Riwayat aktivitas sistem dan kader", Icons.Default.Refresh, HistorySlate) }
                            readCollections(readState, query, setOf("logs"), selectedDetail, { selectedDetail = null }) { collection, record, detailKey ->
                                selectedDetail = record.toDetail(collection.title, detailKey)
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(88.dp)) }
            }
        }
    }
}

private fun androidx.compose.foundation.lazy.LazyListScope.readCollections(
    state: UiState<List<ReadCollection>>,
    query: String,
    keys: Set<String>,
    selectedDetail: DetailInfo?,
    onCloseDetail: () -> Unit,
    onRecordClick: (ReadCollection, ReadRecord, String) -> Unit
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
                    CollectionCard(
                        collection = collection,
                        selectedDetail = selectedDetail,
                        onCloseDetail = onCloseDetail,
                        onRecordClick = { record, detailKey -> onRecordClick(collection, record, detailKey) }
                    )
                }
            }
        }
    }
}

@Composable
private fun DataSectionLoading() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 48.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = PrimaryGreen)
    }
}

@Composable
private fun DataHero(wargaCount: Int, rumahCount: Int, keluargaCount: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(28.dp))
            .padding(18.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("Pusat data Warga Cibiru Wetan", style = MaterialTheme.typography.labelLarge, color = SurfaceWhite.copy(alpha = 0.72f))
                        Text("Manajemen Data Warga", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = SurfaceWhite)
                }
            }
            ResponsiveThreeColumn(
                first = { HeroMetric("Warga", wargaCount.toString(), it) },
                second = { HeroMetric("Rumah", rumahCount.toString(), it) },
                third = { HeroMetric("KK", keluargaCount.toString(), it) }
            )
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
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
    }
}

@Composable
private fun ResidentReadRow(item: AnggotaEntity, onClick: () -> Unit) {
    val accent = residentAccent(item.jenisKelamin)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(residentContainer(item.jenisKelamin), RoundedCornerShape(18.dp))
            .border(1.dp, accent.copy(alpha = 0.24f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(42.dp).background(SurfaceWhite.copy(alpha = 0.72f), RoundedCornerShape(14.dp)), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.People, contentDescription = null, tint = accent)
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(item.nama, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(item.nik, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 6.dp)) {
                LabelBadge(text = item.jenisKelamin, color = accent, container = SurfaceWhite.copy(alpha = 0.72f))
                item.kategoriUsia?.takeIf { it.isNotBlank() }?.let { label ->
                    LabelBadge(text = label, color = ageAccent(label), container = ageContainer(label))
                }
            }
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = accent)
    }
}

@Composable
private fun RumahReadRow(item: RumahEntity, familyCount: Int, onClick: () -> Unit) {
    ReadRow(
        title = "Rumah ${item.noRumah ?: "-"}",
        subtitle = item.alamat ?: "Alamat belum diisi",
        meta = listOfNotNull(item.dusun?.let { "Dusun $it" }, "$familyCount keluarga", if (item.isSynced) "Tersinkron" else "Lokal").joinToString(" - "),
        icon = Icons.Default.Home,
        color = HealthBlue,
        onClick = onClick
    )
}

@Composable
private fun KeluargaReadRow(item: KeluargaEntity, memberCount: Int, rumahLabel: String, onClick: () -> Unit) {
    ReadRow(
        title = "KK ${item.noKK}",
        subtitle = rumahLabel,
        meta = listOf("$memberCount anggota", item.statusKepemilikanRumah, item.statusEkonomi).joinToString(" - "),
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
            .background(ActionAmber.copy(alpha = 0.14f), RoundedCornerShape(18.dp))
            .border(1.dp, ActionAmber.copy(alpha = 0.24f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = ActionAmber)
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = ActionAmber)
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
        ResponsiveTwoColumn(
            first = { itemModifier -> MiniAction("Balita", Icons.Default.ChildCare, ProgramPurple, balita, itemModifier) },
            second = { itemModifier -> MiniAction("Bumil", Icons.Default.PregnantWoman, HealthBlue, bumil, itemModifier) }
        )
        ResponsiveTwoColumn(
            first = { itemModifier -> MiniAction("WUS/PUS", Icons.Default.Favorite, ActionAmber, wuspus, itemModifier) },
            second = { itemModifier -> MiniAction("KB", Icons.Default.FamilyRestroom, PrimaryGreen, kb, itemModifier) }
        )
    }
}

@Composable
private fun ProgramActionGrid(onNavigateToProgram: (String) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        ResponsiveTwoColumn(
            first = { itemModifier -> MiniAction("PHBS", Icons.Default.HealthAndSafety, PrimaryGreen, { onNavigateToProgram("pilot_phbs") }, itemModifier) },
            second = { itemModifier -> MiniAction("Stunting", Icons.Default.ChildCare, ProgramPurple, { onNavigateToProgram("pilot_stunting") }, itemModifier) }
        )
        ResponsiveTwoColumn(
            first = { itemModifier -> MiniAction("KIA", Icons.Default.PregnantWoman, HealthBlue, { onNavigateToProgram("pilot_kia") }, itemModifier) },
            second = { itemModifier -> MiniAction("Kebakaran", Icons.Default.LocalFireDepartment, ActionAmber, { onNavigateToProgram("pilot_kebakaran") }, itemModifier) }
        )
    }
}

@Composable
private fun MiniAction(title: String, icon: ImageVector, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.10f), RoundedCornerShape(16.dp))
            .border(1.dp, color.copy(alpha = 0.20f), RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun CollectionCard(
    collection: ReadCollection,
    selectedDetail: DetailInfo?,
    onCloseDetail: () -> Unit,
    onRecordClick: (ReadRecord, String) -> Unit
) {
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
            Box(modifier = Modifier.background(HealthBlue.copy(alpha = 0.10f), RoundedCornerShape(999.dp)).padding(horizontal = 12.dp, vertical = 7.dp)) {
                Text("${collection.count}", style = MaterialTheme.typography.labelLarge, color = HealthBlue, fontWeight = FontWeight.Bold)
            }
        }
        if (collection.records.isEmpty()) {
            Text("Belum ada data tersimpan.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        } else {
            collection.records.forEach { record ->
                val detailKey = "${collection.key}-${record.id}"
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(HistorySlate.copy(alpha = 0.08f), RoundedCornerShape(14.dp))
                        .border(1.dp, HistorySlate.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                        .clickable { onRecordClick(record, detailKey) }
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(record.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                    Text(record.subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
                    Text(record.meta, style = MaterialTheme.typography.labelSmall, color = TextMuted)
                }
                if (selectedDetail?.key == detailKey) {
                    DetailPanel(detail = selectedDetail, onClose = onCloseDetail)
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

private fun ReadRecord.toDetail(collectionTitle: String, detailKey: String): DetailInfo =
    DetailInfo(
        key = detailKey,
        title = title,
        subtitle = collectionTitle,
        rows = listOf(
            "Ringkasan" to subtitle,
            "Keterangan" to (formatIndonesianDate(meta).takeIf { it != meta } ?: meta)
        )
    )

private fun formatIndonesianDate(value: String?): String {
    val source = value?.trim().orEmpty()
    if (source.isBlank() || source == "null") return "-"

    val locale = Locale("id", "ID")
    val outputPattern = if (source.length <= 10) "d MMMM yyyy" else "d MMMM yyyy, HH.mm"
    val output = SimpleDateFormat(outputPattern, locale)
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd"
    )

    patterns.forEach { pattern ->
        val parser = SimpleDateFormat(pattern, Locale.US).apply {
            isLenient = false
            if (pattern.endsWith("'Z'")) {
                timeZone = TimeZone.getTimeZone("UTC")
            }
        }
        runCatching { parser.parse(source) }.getOrNull()?.let { return output.format(it) }
    }

    return source
}

@Composable
private fun LabelBadge(text: String, color: Color, container: Color) {
    Box(
        modifier = Modifier
            .background(container, RoundedCornerShape(999.dp))
            .border(1.dp, color.copy(alpha = 0.24f), RoundedCornerShape(999.dp))
            .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, style = MaterialTheme.typography.labelSmall, color = color, fontWeight = FontWeight.Bold)
    }
}

private fun residentAccent(jenisKelamin: String?): Color =
    if (jenisKelamin.equals("Perempuan", ignoreCase = true)) ResidentFemale else ResidentMale

private fun residentContainer(jenisKelamin: String?): Color =
    if (jenisKelamin.equals("Perempuan", ignoreCase = true)) ResidentFemaleContainer else ResidentMaleContainer

private fun ageAccent(label: String): Color =
    when (label.lowercase()) {
        "balita" -> AgeToddler
        "lansia" -> AgeElderly
        "produktif" -> AgeProductive
        else -> HistorySlate
    }

private fun ageContainer(label: String): Color =
    when (label.lowercase()) {
        "balita" -> AgeToddlerContainer
        "lansia" -> AgeElderlyContainer
        "produktif" -> AgeProductiveContainer
        else -> SurfaceWhite.copy(alpha = 0.72f)
    }

private fun KeluargaEntity.belongsToRumah(rumah: RumahEntity): Boolean {
    val possibleIds = setOfNotNull(rumah.serverId, rumah.localId)
    return rumahId in possibleIds
}

private fun AnggotaEntity.belongsToKeluarga(keluarga: KeluargaEntity): Boolean {
    val possibleIds = setOfNotNull(keluarga.serverId, keluarga.localId)
    return keluargaId in possibleIds
}
