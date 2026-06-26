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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
import com.desacibiruwetan.posyandu.data.local.entity.RumahEntity
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppRadioButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
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
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.RumahViewmodel
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

@Composable
fun RumahKeluargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    rumahViewModel: RumahViewmodel,
    keluargaViewModel: KeluargaViewmodel
) {
    val context = LocalContext.current
    val preferences = remember { SessionManager.getPreferences(context) }
    val token = SessionManager.getAuthorizationHeader(context)
    val rt = preferences.getString("USER_RT", "00") ?: "00"
    val rw = preferences.getString("USER_RW", "00") ?: "00"

    val rumahList by rumahViewModel.listRumahLocal.collectAsState()
    val keluargaList by keluargaViewModel.listKeluargaLocal.collectAsState()

    var showAddRumahForm by remember { mutableStateOf(false) }
    var editingRumahId by remember { mutableStateOf<Int?>(null) }
    var addingKeluargaRumahId by remember { mutableStateOf<Int?>(null) }
    var editingKeluargaId by remember { mutableStateOf<Int?>(null) }

    fun closeForms() {
        showAddRumahForm = false
        editingRumahId = null
        addingKeluargaRumahId = null
        editingKeluargaId = null
    }

    LaunchedEffect(token) {
        if (token.isNotBlank()) {
            rumahViewModel.syncDataRumah(token)
            keluargaViewModel.syncDataKeluarga(token)
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Rumah & Keluarga", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
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
                RumahKeluargaHero(
                    rumahCount = rumahList.size,
                    keluargaCount = keluargaList.size,
                    onAddRumah = {
                        closeForms()
                        showAddRumahForm = true
                    }
                )
            }

            if (showAddRumahForm) {
                item {
                    RumahFormCard(
                        title = "Tambah rumah",
                        subtitle = "Buat data rumah dulu, lalu tambahkan satu atau beberapa KK di dalamnya.",
                        rumah = null,
                        rt = rt,
                        rw = rw,
                        onCancel = { showAddRumahForm = false },
                        onSubmit = { alamat ->
                            if (token.isBlank()) {
                                Toast.makeText(context, "Sesi habis, silakan login ulang.", Toast.LENGTH_SHORT).show()
                                return@RumahFormCard
                            }
                            rumahViewModel.tambahRumah(token, alamat, rt.toIntOrNull() ?: 0) {
                                Toast.makeText(context, "Rumah berhasil disimpan.", Toast.LENGTH_SHORT).show()
                                showAddRumahForm = false
                            }
                        }
                    )
                }
            }

            if (rumahList.isEmpty()) {
                item {
                    EmptyRumahCard(onAddRumah = {
                        closeForms()
                        showAddRumahForm = true
                    })
                }
            } else {
                items(rumahList, key = { it.localId }) { rumah ->
                    val keluargaRumah = keluargaList.filter { it.belongsToRumah(rumah) }
                    RumahManagementCard(
                        rumah = rumah,
                        keluargaList = keluargaRumah,
                        isEditingRumah = editingRumahId == rumah.localId,
                        isAddingKeluarga = addingKeluargaRumahId == rumah.localId,
                        editingKeluargaId = editingKeluargaId,
                        rt = rt,
                        rw = rw,
                        onEditRumah = {
                            closeForms()
                            editingRumahId = rumah.localId
                        },
                        onAddKeluarga = {
                            closeForms()
                            addingKeluargaRumahId = rumah.localId
                        },
                        onEditKeluarga = { keluarga ->
                            closeForms()
                            editingKeluargaId = keluarga.localId
                        },
                        onCancel = { closeForms() },
                        onSubmitRumah = { alamat ->
                            if (token.isBlank()) {
                                Toast.makeText(context, "Sesi habis, silakan login ulang.", Toast.LENGTH_SHORT).show()
                                return@RumahManagementCard
                            }
                            rumahViewModel.updateRumah(token, rumah, alamat)
                            Toast.makeText(context, "Perubahan rumah disimpan.", Toast.LENGTH_SHORT).show()
                            closeForms()
                        },
                        onSubmitKeluarga = { keluarga, noKk, isNgontrak, isGakin ->
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
                                    isNgontrak = isNgontrak,
                                    isGakin = isGakin
                                ) {
                                    Toast.makeText(context, "KK berhasil ditambahkan ke rumah ini.", Toast.LENGTH_SHORT).show()
                                    closeForms()
                                }
                            } else {
                                keluargaViewModel.updateKeluarga(token, keluarga, noKk, isNgontrak, isGakin)
                                Toast.makeText(context, "Perubahan KK disimpan.", Toast.LENGTH_SHORT).show()
                                closeForms()
                            }
                        }
                    )
                }
            }
            item { Spacer(modifier = Modifier.height(88.dp)) }
        }
    }
}

@Composable
private fun RumahKeluargaHero(
    rumahCount: Int,
    keluargaCount: Int,
    onAddRumah: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(26.dp))
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(modifier = Modifier.weight(1f)) {
                Text("Rumah & keluarga", style = MaterialTheme.typography.labelLarge, color = SurfaceWhite.copy(alpha = 0.72f))
                Text("Kelola rumah, lalu isi KK di dalamnya", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = SurfaceWhite)
            }
            SmallIconButton(icon = Icons.Default.Add, label = "Tambah rumah", color = ActionAmber, onClick = onAddRumah)
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            HeroMetric("Rumah", rumahCount.toString(), Modifier.weight(1f))
            HeroMetric("KK", keluargaCount.toString(), Modifier.weight(1f))
        }
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
private fun RumahManagementCard(
    rumah: RumahEntity,
    keluargaList: List<KeluargaEntity>,
    isEditingRumah: Boolean,
    isAddingKeluarga: Boolean,
    editingKeluargaId: Int?,
    rt: String,
    rw: String,
    onEditRumah: () -> Unit,
    onAddKeluarga: () -> Unit,
    onEditKeluarga: (KeluargaEntity) -> Unit,
    onCancel: () -> Unit,
    onSubmitRumah: (String) -> Unit,
    onSubmitKeluarga: (KeluargaEntity?, String, Boolean, Boolean) -> Unit
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
                Text("${keluargaList.size} KK - ${if (rumah.isSynced) "Tersinkron" else "Tersimpan lokal"}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
            CompactAction("Edit rumah", Icons.Default.Edit, HealthBlue, onEditRumah, Modifier.weight(1f))
            CompactAction("Tambah KK", Icons.Default.Groups, PrimaryGreen, onAddKeluarga, Modifier.weight(1f))
        }

        if (isEditingRumah) {
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

        if (isAddingKeluarga) {
            KeluargaFormCard(
                title = "Tambah KK ke Rumah ${rumah.noRumah ?: "-"}",
                subtitle = "Satu rumah bisa punya lebih dari satu KK.",
                keluarga = null,
                onCancel = onCancel,
                onSubmit = { noKk, isNgontrak, isGakin -> onSubmitKeluarga(null, noKk, isNgontrak, isGakin) }
            )
        }

        if (keluargaList.isEmpty()) {
            Text("Belum ada KK di rumah ini.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
        } else {
            keluargaList.forEach { keluarga ->
                KeluargaRow(
                    keluarga = keluarga,
                    isEditing = editingKeluargaId == keluarga.localId,
                    onEdit = { onEditKeluarga(keluarga) },
                    onCancel = onCancel,
                    onSubmit = { noKk, isNgontrak, isGakin -> onSubmitKeluarga(keluarga, noKk, isNgontrak, isGakin) }
                )
            }
        }
    }
}

@Composable
private fun KeluargaRow(
    keluarga: KeluargaEntity,
    isEditing: Boolean,
    onEdit: () -> Unit,
    onCancel: () -> Unit,
    onSubmit: (String, Boolean, Boolean) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(BgMint, RoundedCornerShape(16.dp))
                .clickable(onClick = onEdit)
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
                        if (keluarga.isNgontrak) "Ngontrak" else "Milik sendiri",
                        if (keluarga.isGakin == true) "Gakin" else "Non-gakin",
                        if (keluarga.isSynced) "Tersinkron" else "Lokal"
                    ).joinToString(" - "),
                    style = MaterialTheme.typography.labelSmall,
                    color = TextMuted
                )
                Text("Diubah ${formatIndonesianDate(keluarga.updatedAt)}", style = MaterialTheme.typography.labelSmall, color = TextMuted)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
        }

        if (isEditing) {
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

@Composable
private fun RumahFormCard(
    title: String,
    subtitle: String,
    rumah: RumahEntity?,
    rt: String,
    rw: String,
    onCancel: () -> Unit,
    onSubmit: (String) -> Unit
) {
    var alamat by remember(rumah?.localId) { mutableStateOf(rumah?.alamat.orEmpty()) }

    FormPanel(title = title, subtitle = subtitle, icon = Icons.Default.Home, color = HealthBlue, onCancel = onCancel) {
        AppTextField(
            label = "Alamat Lengkap",
            value = alamat,
            singleLine = false,
            onValueChange = { alamat = it }
        )
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            AppTextField(
                label = "RT",
                value = rt,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.weight(1f),
            )
            AppTextField(
                label = "RW",
                value = rw,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier.weight(1f),
            )
        }
        Text(
            text = if (rumah == null) "No rumah akan dibuat otomatis oleh server." else "No rumah ${rumah.noRumah ?: "-"} dibuat otomatis dan tidak dapat diubah dari mobile.",
            style = MaterialTheme.typography.bodySmall,
            color = TextMuted
        )
        PrimaryButton(
            text = if (rumah == null) "Simpan rumah" else "Simpan perubahan rumah",
            icon = Icons.Default.Save,
            enabled = alamat.isNotBlank(),
            onClick = {
                onSubmit(alamat.trim())
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
    onSubmit: (String, Boolean, Boolean) -> Unit
) {
    var noKk by remember(keluarga?.localId) { mutableStateOf(keluarga?.noKK.orEmpty()) }
    var isNgontrak by remember(keluarga?.localId) { mutableStateOf(keluarga?.isNgontrak ?: false) }
    var isGakin by remember(keluarga?.localId) { mutableStateOf(keluarga?.isGakin ?: false) }

    FormPanel(title = title, subtitle = subtitle, icon = Icons.Default.Groups, color = PrimaryGreen, onCancel = onCancel) {
        AppTextField(
            label = "No KK",
            value = noKk,
            placeholder = "16 digit nomor KK",
            keyboardType = KeyboardType.Number,
            onValueChange = { if (it.length <= 16 && it.all { char -> char.isDigit() }) noKk = it }
        )
        ChoiceGroup(
            title = "Status tempat tinggal",
            first = "Milik sendiri",
            second = "Ngontrak",
            selectedSecond = isNgontrak,
            onFirst = { isNgontrak = false },
            onSecond = { isNgontrak = true }
        )
        ChoiceGroup(
            title = "Kategori Gakin",
            first = "Tidak",
            second = "Ya",
            selectedSecond = isGakin,
            onFirst = { isGakin = false },
            onSecond = { isGakin = true }
        )
        PrimaryButton(
            text = if (keluarga == null) "Simpan KK" else "Simpan perubahan KK",
            icon = Icons.Default.Save,
            enabled = noKk.length == 16,
            onClick = { onSubmit(noKk, isNgontrak, isGakin) }
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
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            AppRadioButton(first, !selectedSecond, onFirst)
            AppRadioButton(second, selectedSecond, onSecond)
        }
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
