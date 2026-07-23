package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryFab
import com.desacibiruwetan.posyandu.ui.components.input.AppRadioButton
import com.desacibiruwetan.posyandu.ui.components.input.AppDropdownField
import com.desacibiruwetan.posyandu.ui.components.input.AppSearchBar
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.layout.DraggableScrollbar
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
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
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.RumahViewmodel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun RumahKeluargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    rumahViewModel: RumahViewmodel,
    keluargaViewModel: KeluargaViewmodel,
    anggotaViewModel: AnggotaViewmodel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val preferences = remember { SessionManager.getPreferences(context) }
    val token = SessionManager.getAuthorizationHeader(context)
    val rt = preferences.getString("USER_RT", "00") ?: "00"
    val rw = preferences.getString("USER_RW", "00") ?: "00"

    val rumahList by rumahViewModel.listRumahLocal.collectAsState()
    val keluargaList by keluargaViewModel.listKeluargaLocal.collectAsState()
    val anggotaList by anggotaViewModel.listAnggotaLocal.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showAddRumahForm by remember { mutableStateOf(false) }
    var editingRumahId by remember { mutableStateOf<Int?>(null) }
    var addingKeluargaRumahId by remember { mutableStateOf<Int?>(null) }
    var editingKeluargaId by remember { mutableStateOf<Int?>(null) }
    var selectedKeluargaDetailId by remember { mutableStateOf<Int?>(null) }

    val addRumahRequester = remember { BringIntoViewRequester() }
    val editRumahRequester = remember { BringIntoViewRequester() }
    val addKeluargaRequester = remember { BringIntoViewRequester() }
    val editKeluargaRequester = remember { BringIntoViewRequester() }

    fun closeForms() {
        showAddRumahForm = false
        editingRumahId = null
        addingKeluargaRumahId = null
        editingKeluargaId = null
    }

    LaunchedEffect(showAddRumahForm) {
        if (showAddRumahForm) {
            addRumahRequester.bringIntoView()
        }
    }

    LaunchedEffect(editingRumahId) {
        if (editingRumahId != null) {
            editRumahRequester.bringIntoView()
        }
    }

    LaunchedEffect(addingKeluargaRumahId) {
        if (addingKeluargaRumahId != null) {
            addKeluargaRequester.bringIntoView()
        }
    }

    LaunchedEffect(editingKeluargaId) {
        if (editingKeluargaId != null) {
            editKeluargaRequester.bringIntoView()
        }
    }

    suspend fun refreshData() {
        if (token.isNotBlank()) {
            rumahViewModel.syncDataRumah(token)
            keluargaViewModel.syncDataKeluarga(token)
            anggotaViewModel.syncDataAnggotaDariServer(token)
        }
    }

    LaunchedEffect(token) {
        refreshData()
    }

    val filteredRumahList = remember(searchQuery, rumahList, keluargaList, anggotaList) {
        rumahList.filter { rumah ->
            rumah.matchesRumahKeluargaSearch(
                query = searchQuery,
                keluargaList = keluargaList,
                anggotaList = anggotaList
            )
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Rumah & Keluarga", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        floatingActionButton = {
            PrimaryFab(
                text = "Tambah Rumah",
                icon = Icons.Default.Add,
                onClick = {
                    closeForms()
                    showAddRumahForm = true
                }
            )
        },
        containerColor = BgMint
    ) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .responsiveScreenPadding(),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item {
                    RumahKeluargaHero(
                        rumahCount = rumahList.size,
                        keluargaCount = keluargaList.size
                    )
                }
                item {
                    AppSearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        placeholder = "Cari no rumah, alamat, no KK, atau nama anggota"
                    )
                }
                if (searchQuery.isNotBlank() && rumahList.isNotEmpty()) {
                    item {
                        Text(
                            text = "${filteredRumahList.size} rumah cocok untuk \"$searchQuery\"",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextMuted
                        )
                    }
                }

                if (showAddRumahForm) {
                    item {
                        Box(modifier = Modifier.bringIntoViewRequester(addRumahRequester)) {
                            RumahFormCard(
                                title = "Tambah rumah",
                                subtitle = "Buat data rumah dulu, lalu tambahkan satu atau beberapa KK di dalamnya.",
                                rumah = null,
                                rt = rt,
                                rw = rw,
                                onCancel = { showAddRumahForm = false },
                                onSubmit = { alamat, dusun ->
                                    if (token.isBlank()) {
                                        Toast.makeText(context, "Sesi habis, silakan login ulang.", Toast.LENGTH_SHORT).show()
                                        return@RumahFormCard
                                    }
                                    rumahViewModel.tambahRumah(token, alamat, dusun, rt.toIntOrNull() ?: 0) {
                                        Toast.makeText(context, "Rumah berhasil disimpan.", Toast.LENGTH_SHORT).show()
                                        showAddRumahForm = false
                                    }
                                }
                            )
                        }
                    }
                }

                if (rumahList.isEmpty()) {
                    item {
                        EmptyRumahCard(onAddRumah = {
                            closeForms()
                            showAddRumahForm = true
                        })
                    }
                } else if (filteredRumahList.isEmpty()) {
                    item {
                        EmptySearchCard(query = searchQuery)
                    }
                } else {
                    items(filteredRumahList, key = { it.localId }) { rumah ->
                        val keluargaRumah = keluargaList.filter { it.belongsToRumah(rumah) }
                        RumahManagementCard(
                            rumah = rumah,
                            keluargaList = keluargaRumah,
                            anggotaList = anggotaList,
                            selectedKeluargaDetailId = selectedKeluargaDetailId,
                            isEditingRumah = editingRumahId == rumah.localId,
                            isAddingKeluarga = addingKeluargaRumahId == rumah.localId,
                            editingKeluargaId = editingKeluargaId,
                            rt = rt,
                            rw = rw,
                            editRumahRequester = editRumahRequester,
                            addKeluargaRequester = addKeluargaRequester,
                            editKeluargaRequester = editKeluargaRequester,
                            onEditRumah = {
                                closeForms()
                                editingRumahId = rumah.localId
                            },
                            onAddKeluarga = {
                                closeForms()
                                addingKeluargaRumahId = rumah.localId
                            },
                            onToggleKeluargaDetail = { keluarga ->
                                closeForms()
                                selectedKeluargaDetailId =
                                    if (selectedKeluargaDetailId == keluarga.localId) null else keluarga.localId
                            },
                            onEditKeluarga = { keluarga ->
                                closeForms()
                                selectedKeluargaDetailId = keluarga.localId
                                editingKeluargaId = keluarga.localId
                            },
                            onCancel = { closeForms() },
                            onSubmitRumah = { alamat, dusun ->
                                if (token.isBlank()) {
                                    Toast.makeText(context, "Sesi habis, silakan login ulang.", Toast.LENGTH_SHORT).show()
                                    return@RumahManagementCard
                                }
                                rumahViewModel.updateRumah(token, rumah, alamat, dusun)
                                Toast.makeText(context, "Perubahan rumah disimpan.", Toast.LENGTH_SHORT).show()
                                closeForms()
                            },
                            onSubmitKeluarga = { keluarga, noKk, statusKepemilikanRumah, kepemilikanJamban, kepemilikanSpal, statusEkonomi ->
                                if (token.isBlank()) {
                                    Toast.makeText(context, "Sesi habis, silakan login ulang.", Toast.LENGTH_SHORT).show()
                                    return@RumahManagementCard
                                }
                                if (keluarga == null) {
                                    keluargaViewModel.tambahKeluarga(
                                        token = token,
                                        rumahId = rumah.localId,
                                        rumahServerId = rumah.serverId,
                                        noKk = noKk,
                                        statusKepemilikanRumah = statusKepemilikanRumah,
                                        kepemilikanJamban = kepemilikanJamban,
                                        kepemilikanSpal = kepemilikanSpal,
                                        statusEkonomi = statusEkonomi
                                    ) {
                                        Toast.makeText(context, "KK berhasil ditambahkan ke rumah ini.", Toast.LENGTH_SHORT).show()
                                        closeForms()
                                    }
                                } else {
                                    keluargaViewModel.updateKeluarga(
                                        token,
                                        keluarga,
                                        noKk,
                                        statusKepemilikanRumah,
                                        kepemilikanJamban,
                                        kepemilikanSpal,
                                        statusEkonomi
                                    )
                                    Toast.makeText(context, "Perubahan KK disimpan.", Toast.LENGTH_SHORT).show()
                                    closeForms()
                                }
                            }
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(88.dp)) }
            }

            DraggableScrollbar(
                listState = listState,
                modifier = Modifier.align(Alignment.CenterEnd)
            )
        }
    }
}

@Composable
private fun RumahKeluargaHero(
    rumahCount: Int,
    keluargaCount: Int
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(26.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Rumah & keluarga", style = MaterialTheme.typography.labelLarge, color = SurfaceWhite.copy(alpha = 0.72f))
            Text("Kelola rumah, lalu isi KK di dalamnya", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SurfaceWhite)
        }
        ResponsiveTwoColumn(
            first = { itemModifier -> HeroMetric("Rumah", rumahCount.toString(), itemModifier) },
            second = { itemModifier -> HeroMetric("KK", keluargaCount.toString(), itemModifier) }
        )
    }
}

@Composable
private fun HeroMetric(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SurfaceWhite.copy(alpha = 0.1f), RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = SurfaceWhite.copy(alpha = 0.68f))
        Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SurfaceWhite)
    }
}

@Composable
private fun EmptyRumahCard(onAddRumah: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(22.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Belum ada rumah", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text("Mulai dari data rumah. Setelah rumah tersimpan, kamu bisa menambahkan lebih dari satu KK di bawah rumah tersebut.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        CompactAction("Tambah rumah pertama", Icons.Default.Add, PrimaryGreen, onAddRumah)
    }
}

@Composable
private fun EmptySearchCard(query: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(22.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .background(HealthBlue.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.SearchOff, contentDescription = null, tint = HealthBlue)
        }
        Text("Tidak ada hasil", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        Text(
            "Tidak ditemukan rumah, alamat, KK, atau anggota yang cocok dengan \"$query\".",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun RumahManagementCard(
    rumah: RumahEntity,
    keluargaList: List<KeluargaEntity>,
    anggotaList: List<AnggotaEntity>,
    selectedKeluargaDetailId: Int?,
    isEditingRumah: Boolean,
    isAddingKeluarga: Boolean,
    editingKeluargaId: Int?,
    rt: String,
    rw: String,
    editRumahRequester: BringIntoViewRequester,
    addKeluargaRequester: BringIntoViewRequester,
    editKeluargaRequester: BringIntoViewRequester,
    onEditRumah: () -> Unit,
    onAddKeluarga: () -> Unit,
    onToggleKeluargaDetail: (KeluargaEntity) -> Unit,
    onEditKeluarga: (KeluargaEntity) -> Unit,
    onCancel: () -> Unit,
    onSubmitRumah: (String?, String?) -> Unit,
    onSubmitKeluarga: (KeluargaEntity?, String, String, String?, String?, String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(24.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(46.dp).background(HealthBlue.copy(alpha = 0.12f), RoundedCornerShape(15.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Home, contentDescription = null, tint = HealthBlue)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text("Rumah ${rumah.noRumah ?: "-"}", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                Text(rumah.alamat ?: "Alamat belum diisi", style = MaterialTheme.typography.bodySmall, color = TextMuted)
                Text(
                    listOfNotNull(
                        rumah.dusun?.let { "Dusun $it" },
                        "${keluargaList.size} KK",
                        if (rumah.isSynced) "Tersinkron" else "Tersimpan lokal"
                    ).joinToString(" - "),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
            }
        }

        ResponsiveTwoColumn(
            first = { itemModifier -> CompactAction("Edit rumah", Icons.Default.Edit, HealthBlue, onEditRumah, itemModifier) },
            second = { itemModifier -> CompactAction("Tambah KK", Icons.Default.Groups, PrimaryGreen, onAddKeluarga, itemModifier) }
        )

        if (isEditingRumah) {
            Box(modifier = Modifier.bringIntoViewRequester(editRumahRequester)) {
                RumahFormCard(
                    title = "Edit rumah ${rumah.noRumah ?: ""}",
                    subtitle = "Perubahan disimpan lokal dulu dan akan ikut sinkron saat online.",
                    rumah = rumah,
                    rt = rt,
                    rw = rw,
                    onCancel = onCancel,
                    onSubmit = onSubmitRumah
                )
            }
        }

        if (isAddingKeluarga) {
            Box(modifier = Modifier.bringIntoViewRequester(addKeluargaRequester)) {
                KeluargaFormCard(
                    title = "Tambah KK ke Rumah ${rumah.noRumah ?: "-"}",
                    subtitle = "Satu rumah bisa punya lebih dari satu KK.",
                    keluarga = null,
                    onCancel = onCancel,
                    onSubmit = { noKk, statusKepemilikanRumah, kepemilikanJamban, kepemilikanSpal, statusEkonomi ->
                        onSubmitKeluarga(null, noKk, statusKepemilikanRumah, kepemilikanJamban, kepemilikanSpal, statusEkonomi)
                    }
                )
            }
        }

        if (keluargaList.isEmpty()) {
            Text("Belum ada KK di rumah ini.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        } else {
            keluargaList.forEach { keluarga ->
                val anggotaKeluarga = anggotaList.filter { it.belongsToKeluarga(keluarga) }
                KeluargaRow(
                    keluarga = keluarga,
                    anggotaList = anggotaKeluarga,
                    isExpanded = selectedKeluargaDetailId == keluarga.localId,
                    isEditing = editingKeluargaId == keluarga.localId,
                    editKeluargaRequester = editKeluargaRequester,
                    onToggleDetail = { onToggleKeluargaDetail(keluarga) },
                    onEdit = { onEditKeluarga(keluarga) },
                    onCancel = onCancel,
                    onSubmit = { noKk, statusKepemilikanRumah, kepemilikanJamban, kepemilikanSpal, statusEkonomi ->
                        onSubmitKeluarga(keluarga, noKk, statusKepemilikanRumah, kepemilikanJamban, kepemilikanSpal, statusEkonomi)
                    }
                )
            }
        }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
private fun KeluargaRow(
    keluarga: KeluargaEntity,
    anggotaList: List<AnggotaEntity>,
    isExpanded: Boolean,
    isEditing: Boolean,
    editKeluargaRequester: BringIntoViewRequester,
    onToggleDetail: () -> Unit,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
    onSubmit: (String, String, String?, String?, String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgMint, RoundedCornerShape(16.dp))
                .clickable(onClick = onToggleDetail)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(FreshTeal, RoundedCornerShape(14.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Groups, contentDescription = null, tint = PrimaryGreen)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text("KK ${keluarga.noKK}", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
                Text(
                    listOf(
                        keluarga.statusKepemilikanRumah,
                        keluarga.statusEkonomi,
                        if (keluarga.isSynced) "Tersinkron" else "Lokal"
                    ).joinToString(" - "),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text("Diubah ${formatIndonesianDate(keluarga.updatedAt)}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
        }

        if (isExpanded) {
            KeluargaDetailPanel(
                keluarga = keluarga,
                anggotaList = anggotaList,
                onEdit = onEdit
            )
        }

        if (isEditing) {
            Box(modifier = Modifier.bringIntoViewRequester(editKeluargaRequester)) {
                KeluargaFormCard(
                    title = "Edit KK ${keluarga.noKK}",
                    subtitle = "Edit nomor KK dan status keluarga tanpa mengubah rumahnya.",
                    keluarga = keluarga,
                    onCancel = onCancel,
                    onSubmit = onSubmit
                )
            }
        }
    }
}

@Composable
private fun KeluargaDetailPanel(
    keluarga: KeluargaEntity,
    anggotaList: List<AnggotaEntity>,
    onEdit: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceMuted, RoundedCornerShape(18.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Detail KK ${keluarga.noKK}", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(
                    "${anggotaList.size} anggota keluarga",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextMuted
                )
            }
            CompactAction("Edit KK", Icons.Default.Edit, PrimaryGreen, onEdit)
        }

        ResponsiveTwoColumn(
            first = { itemModifier ->
                DetailPill(
                    label = "Tempat tinggal",
                    value = keluarga.statusKepemilikanRumah,
                    modifier = itemModifier
                )
            },
            second = { itemModifier ->
                DetailPill(
                    label = "Status ekonomi",
                    value = keluarga.statusEkonomi,
                    modifier = itemModifier
                )
            }
        )
        ResponsiveTwoColumn(
            first = { itemModifier ->
                DetailPill(
                    label = "Jamban",
                    value = keluarga.kepemilikanJamban ?: "-",
                    modifier = itemModifier
                )
            },
            second = { itemModifier ->
                DetailPill(
                    label = "SPAL",
                    value = keluarga.kepemilikanSpal ?: "-",
                    modifier = itemModifier
                )
            }
        )

        if (anggotaList.isEmpty()) {
            Text("Belum ada anggota pada KK ini.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        } else {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                anggotaList.forEach { anggota ->
                    AnggotaKeluargaRow(anggota)
                }
            }
        }
    }
}

@Composable
private fun DetailPill(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SurfaceWhite, RoundedCornerShape(14.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
            .padding(12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = TextMuted)
        Text(value, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun AnggotaKeluargaRow(anggota: AnggotaEntity) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(14.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(14.dp))
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .background(HealthBlue.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Groups, contentDescription = null, tint = HealthBlue, modifier = Modifier.size(20.dp))
        }
        Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
            Text(anggota.nama, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Bold)
            Text(
                listOfNotNull(
                    anggota.statusKeluarga,
                    anggota.kategoriUsia,
                    anggota.statusWarga
                ).joinToString(" - "),
                style = MaterialTheme.typography.labelSmall,
                color = TextMuted
            )
        }
        Text(anggota.nik, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}

@Composable
private fun RumahFormCard(
    title: String,
    subtitle: String,
    rumah: RumahEntity?,
    rt: String,
    rw: String,
    onCancel: () -> Unit,
    onSubmit: (String?, String?) -> Unit
) {
    var alamat by remember(rumah?.localId) { mutableStateOf(rumah?.alamat.orEmpty()) }
    var dusun by remember(rumah?.localId) { mutableStateOf(rumah?.dusun.orEmpty()) }

    FormPanel(title = title, subtitle = subtitle, icon = Icons.Default.Home, color = HealthBlue, onCancel = onCancel) {
        AppTextField(
            label = "Alamat Lengkap",
            value = alamat,
            singleLine = false,
            onValueChange = {
                alamat = it
            }
        )
        AppDropdownField(
            label = "Dusun",
            value = dusun,
            options = listOf("1", "2", "3", "4", "5"),
            placeholder = "Pilih dusun",
            onValueChange = { dusun = it }
        )
        ResponsiveTwoColumn(
            first = { fieldModifier ->
            AppTextField(
                label = "RT",
                value = rt,
                onValueChange = {},
                readOnly = true,
                modifier = fieldModifier,
            )
            },
            second = { fieldModifier ->
            AppTextField(
                label = "RW",
                value = rw,
                onValueChange = {},
                readOnly = true,
                modifier = fieldModifier,
            )
            }
        )
        Text(
            text = if (rumah == null) "No rumah akan dibuat otomatis oleh server." else "No rumah ${rumah.noRumah ?: "-"} dibuat otomatis dan tidak dapat diubah dari mobile.",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
        PrimaryButton(
            text = if (rumah == null) "Simpan rumah" else "Simpan perubahan rumah",
            icon = Icons.Default.Save,
            onClick = {
                onSubmit(alamat.trim().ifBlank { null }, dusun.ifBlank { null })
            }
        )
    }
}

@Composable
private fun KeluargaFormCard(
    title: String,
    subtitle: String,
    keluarga: KeluargaEntity?,
    onCancel: () -> Unit,
    onSubmit: (String, String, String?, String?, String) -> Unit
) {
    var noKk by remember(keluarga?.localId) { mutableStateOf(keluarga?.noKK.orEmpty()) }
    var statusKepemilikanRumah by remember(keluarga?.localId) { mutableStateOf(keluarga?.statusKepemilikanRumah ?: "Milik Sendiri") }
    var kepemilikanJamban by remember(keluarga?.localId) { mutableStateOf(keluarga?.kepemilikanJamban ?: "Milik Sendiri") }
    var kepemilikanSpal by remember(keluarga?.localId) { mutableStateOf(keluarga?.kepemilikanSpal ?: "Septitang") }
    var statusEkonomi by remember(keluarga?.localId) { mutableStateOf(keluarga?.statusEkonomi ?: "Sejahtera") }
    var noKkError by remember(keluarga?.localId) { mutableStateOf<String?>(null) }

    FormPanel(title = title, subtitle = subtitle, icon = Icons.Default.Groups, color = PrimaryGreen, onCancel = onCancel) {
        AppTextField(
            label = "No KK",
            value = noKk,
            placeholder = "16 digit nomor KK",
            keyboardType = KeyboardType.Number,
            maxLength = 16,
            counterLabel = "NO KK",
            error = noKkError,
            onValueChange = {
                noKk = it.filter(Char::isDigit).take(16)
                noKkError = null
            }
        )
        AppDropdownField(
            label = "Status tempat tinggal",
            value = statusKepemilikanRumah,
            options = listOf("Milik Sendiri", "Ngontrak", "Numpang Orang Tua"),
            onValueChange = { statusKepemilikanRumah = it }
        )
        ResponsiveTwoColumn(
            first = { fieldModifier ->
                AppDropdownField(
                    modifier = fieldModifier,
                    label = "Jamban",
                    value = kepemilikanJamban,
                    options = listOf("Milik Sendiri", "Umum"),
                    onValueChange = { kepemilikanJamban = it }
                )
            },
            second = { fieldModifier ->
                AppDropdownField(
                    modifier = fieldModifier,
                    label = "SPAL",
                    value = kepemilikanSpal,
                    options = listOf("Septitang", "Selokan"),
                    onValueChange = { kepemilikanSpal = it }
                )
            }
        )
        AppDropdownField(
            label = "Status ekonomi",
            value = statusEkonomi,
            options = listOf("Sejahtera", "Pra Sejahtera", "Gakin"),
            onValueChange = { statusEkonomi = it }
        )
        PrimaryButton(
            text = if (keluarga == null) "Simpan KK" else "Simpan perubahan KK",
            icon = Icons.Default.Save,
            onClick = {
                noKkError = when {
                    noKk.isBlank() -> "No kk wajib diisi."
                    noKk.length != 16 -> "No kk harus 16 digit."
                    else -> null
                }
                if (noKkError != null) return@PrimaryButton
                onSubmit(noKk, statusKepemilikanRumah, kepemilikanJamban, kepemilikanSpal, statusEkonomi)
            }
        )
    }
}

@Composable
private fun FormPanel(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onCancel: () -> Unit,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceMuted, RoundedCornerShape(20.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(38.dp).background(color.copy(alpha = 0.12f), RoundedCornerShape(13.dp)), contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = color)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
            SmallIconButton(icon = Icons.Default.Close, label = "Tutup", color = TextMuted, onClick = onCancel)
        }
        content()
    }
}

@Composable
private fun ChoiceGroup(
    title: String,
    first: String,
    second: String,
    selectedSecond: Boolean,
    onFirst: () -> Unit,
    onSecond: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
        Text(title, style = MaterialTheme.typography.bodyMedium, color = TextMuted)
        Spacer(modifier = Modifier.height(8.dp))
        ResponsiveTwoColumn(
            first = { itemModifier -> AppRadioButton(first, !selectedSecond, onFirst, itemModifier) },
            second = { itemModifier -> AppRadioButton(second, selectedSecond, onSecond, itemModifier) }
        )
    }
}

@Composable
private fun CompactAction(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .background(color.copy(alpha = 0.1f), RoundedCornerShape(15.dp))
            .border(1.dp, color.copy(alpha = 0.18f), RoundedCornerShape(15.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 11.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(text, style = MaterialTheme.typography.labelLarge, color = color, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SmallIconButton(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(color.copy(alpha = 0.16f), RoundedCornerShape(14.dp))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = label, tint = color)
    }
}

private fun KeluargaEntity.belongsToRumah(rumah: RumahEntity): Boolean {
    val possibleIds = setOfNotNull(rumah.localId, rumah.serverId)
    return rumahId in possibleIds
}

private fun AnggotaEntity.belongsToKeluarga(keluarga: KeluargaEntity): Boolean {
    val possibleIds = setOfNotNull(keluarga.localId, keluarga.serverId)
    return keluargaId in possibleIds
}

private fun RumahEntity.matchesRumahKeluargaSearch(
    query: String,
    keluargaList: List<KeluargaEntity>,
    anggotaList: List<AnggotaEntity>
): Boolean {
    val keyword = query.trim()
    if (keyword.isBlank()) return true

    val keluargaRumah = keluargaList.filter { it.belongsToRumah(this) }
    val anggotaRumah = anggotaList.filter { anggota ->
        keluargaRumah.any { keluarga -> anggota.belongsToKeluarga(keluarga) }
    }

    return noRumah.orEmpty().contains(keyword, ignoreCase = true) ||
        dusun.orEmpty().contains(keyword, ignoreCase = true) ||
        alamat.orEmpty().contains(keyword, ignoreCase = true) ||
        keluargaRumah.any { it.noKK.contains(keyword, ignoreCase = true) } ||
        anggotaRumah.any { it.nama.contains(keyword, ignoreCase = true) }
}

private fun formatIndonesianDate(value: String?): String {
    val source = value?.trim().orEmpty()
    if (source.isBlank() || source == "null") return "-"

    val locale = Locale("id", "ID")
    val outputPattern = if (source.contains(":")) "d MMMM yyyy, HH.mm" else "d MMMM yyyy"
    val output = SimpleDateFormat(outputPattern, locale)
    val patterns = listOf(
        "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
        "yyyy-MM-dd'T'HH:mm:ss'Z'",
        "yyyy-MM-dd HH:mm:ss",
        "yyyy-MM-dd",
        "d MMMM yyyy, HH.mm",
        "d MMMM yyyy"
    )

    patterns.forEach { pattern ->
        val parser = SimpleDateFormat(pattern, locale).apply {
            isLenient = false
            if (pattern.endsWith("'Z'")) timeZone = TimeZone.getTimeZone("UTC")
        }
        runCatching { parser.parse(source) }.getOrNull()?.let { return output.format(it) }
    }

    return source
}
