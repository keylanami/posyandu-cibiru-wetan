package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.dialog.SearchWargaDialog
import com.desacibiruwetan.posyandu.ui.components.input.AppDateField
import com.desacibiruwetan.posyandu.ui.components.input.AppSwitch
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.utils.isSameOrAfter
import com.desacibiruwetan.posyandu.utils.normalizeDateForForm
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun UpdateBumilScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    initialLocalId: Int? = null
) {
    val context = LocalContext.current
    val token = SessionManager.getAuthorizationHeader(context)
    val listAnggota by anggotaViewModel.listAnggotaLocal.collectAsState()
    val lockedToWarga = initialLocalId != null

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<AnggotaEntity?>(null) }
    var namaBumil by remember { mutableStateOf("") }

    var hamilKe by remember { mutableStateOf("") }
    var asiEksklusif by remember { mutableStateOf(false) }
    var tanggalMulaiAsi by remember { mutableStateOf("") }
    var tanggalSelesaiAsi by remember { mutableStateOf("") }
    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val detailBumil by anggotaViewModel.detailBumilState.collectAsState()

    fun selectWarga(warga: AnggotaEntity) {
        if (warga.jenisKelamin.equals("Laki-laki", ignoreCase = true)) {
            Toast.makeText(context, "Bumil harus seorang Perempuan!", Toast.LENGTH_SHORT)
                .show()
        } else {
            anggotaViewModel.resetDetailBumilState()
            selectedWarga = warga
            namaBumil = warga.nama

            // Reset local form fields when switching citizens
            hamilKe = ""
            asiEksklusif = false
            tanggalMulaiAsi = ""
            tanggalSelesaiAsi = ""
            fieldErrors = fieldErrors - "warga_id"

            if (warga.serverId != null) {
                anggotaViewModel.getDetailBumilByAnggotaFromServer(token, warga.serverId)
            }
        }
    }

    fun resetScreenState() {
        anggotaViewModel.resetDetailBumilState()
        selectedWarga = null
        namaBumil = ""
        hamilKe = ""
        asiEksklusif = false
        tanggalMulaiAsi = ""
        tanggalSelesaiAsi = ""
        fieldErrors = emptyMap()
    }

    LaunchedEffect(initialLocalId, listAnggota) {
        if (initialLocalId != null) {
            val warga = listAnggota.find { it.localId == initialLocalId }
            if (warga != null && selectedWarga?.localId != initialLocalId) {
                selectWarga(warga)
            }
        }
    }

    val lifecycleOwner = LocalLifecycleOwner.current
    if (!lockedToWarga) {
        DisposableEffect(lifecycleOwner) {
            val observer = LifecycleEventObserver { _, event ->
                if (event == Lifecycle.Event.ON_RESUME) {
                    resetScreenState()
                }
            }
            lifecycleOwner.lifecycle.addObserver(observer)
            onDispose {
                lifecycleOwner.lifecycle.removeObserver(observer)
            }
        }
    }

    LaunchedEffect(detailBumil) {
        if (detailBumil is UiState.Success) {
            val warga = selectedWarga ?: return@LaunchedEffect
            val dataServer = (detailBumil as UiState.Success).data.data
            if (dataServer != null && dataServer.anggotaId == warga.serverId) {
                hamilKe = dataServer.hamilKe.toString()
                asiEksklusif = dataServer.asiEksklusif
                tanggalMulaiAsi = normalizeDateForForm(dataServer.tanggalMulaiAsi)
                tanggalSelesaiAsi = normalizeDateForForm(dataServer.tanggalSelesaiAsi)
            }
        }
    }

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                selectWarga(warga)
            },
            anggotaViewModel = anggotaViewModel
        )
    }

    Scaffold(
        topBar = { AppTopBar(title = "Update Data Bumil", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .responsiveScreenPadding()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = if (lockedToWarga) Modifier else Modifier.clickable { showDialog = true }) {
                if (selectedWarga != null) {
                    UpdateHeaderCard(
                        title = "Update untuk",
                        name = selectedWarga!!.nama,
                        icon = Icons.Default.PregnantWoman
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AppTextField(
                            label = "Nama Ibu Hamil",
                            value = namaBumil,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = "Pilih dari pencarian"
                        )
                    }
                } else {
                    UpdateHeaderCard(
                        title = "Pilih Warga",
                        name = "Ketuk untuk mencari Ibu Hamil",
                        icon = Icons.Default.Search
                    )
                }
            }
            fieldErrors["warga_id"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            FormSectionCard(title = "Data Kehamilan") {
                AppTextField(
                    label = "Hamil Ke-",
                    value = hamilKe,
                    placeholder = "Contoh: 1",
                    keyboardType = KeyboardType.Number,
                    error = fieldErrors["hamil_ke"],
                    onValueChange = {
                        if (it.all { char -> char.isDigit() }) {
                            hamilKe = it
                            fieldErrors = fieldErrors - "hamil_ke"
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            FormSectionCard(title = "Data Menyusui") {
                AppSwitch(
                    label = "ASI Eksklusif?",
                    description = "Apakah ibu memberikan ASI Eksklusif?",
                    checked = asiEksklusif,
                    onCheckedChange = { asiEksklusif = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppDateField(
                    label = "Tanggal Mulai ASI",
                    value = tanggalMulaiAsi,
                    onValueChange = { tanggalMulaiAsi = it }
                )

                AppDateField(
                    label = "Tanggal Selesai ASI (Opsional)",
                    value = tanggalSelesaiAsi,
                    error = fieldErrors["tanggal_selesai_asi"],
                    onValueChange = {
                        tanggalSelesaiAsi = it
                        fieldErrors = fieldErrors - "tanggal_selesai_asi"
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Simpan Data Bumil",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        val warga = selectedWarga
                        if (warga == null) {
                            fieldErrors = mapOf("warga_id" to "Ibu hamil wajib dipilih.")
                            return@PrimaryButton
                        }

                        val hKe = hamilKe.toIntOrNull()
                        if (hKe == null || hKe < 1) {
                            fieldErrors = mapOf("hamil_ke" to "Hamil ke- harus diisi minimal 1.")
                            return@PrimaryButton
                        }

                        if (!isSameOrAfter(tanggalMulaiAsi, tanggalSelesaiAsi)) {
                            fieldErrors = mapOf("tanggal_selesai_asi" to "Tanggal selesai ASI tidak boleh sebelum tanggal mulai.")
                            return@PrimaryButton
                        }

                        anggotaViewModel.updateDataBumil(
                            token = token,
                            anggotaLocalId = warga.localId,
                            anggotaServerId = warga.serverId,
                            hamilKe = hKe,
                            asiEksklusif = asiEksklusif,
                            tglMulaiAsi = tanggalMulaiAsi.ifBlank { null },
                            tglSelesaiAsi = tanggalSelesaiAsi.ifBlank { null },
                            createdAt = warga.createdAt ?: "",
                            updatedAt = warga.updatedAt ?: ""
                        )

                        Toast.makeText(context, "Data Bumil berhasil disimpan!", Toast.LENGTH_SHORT)
                            .show()
                        onBackClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
