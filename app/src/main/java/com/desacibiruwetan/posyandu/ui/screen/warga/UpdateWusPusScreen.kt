package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LocalLifecycleOwner
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
import com.desacibiruwetan.posyandu.ui.components.input.AppRadioButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.utils.normalizeDateForForm
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun UpdateWusPusScreen(
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

    var namaWarga by remember { mutableStateOf("") }
    var namaPasangan by remember { mutableStateOf("") }
    var kategoriStatus by remember { mutableStateOf("WUS") }
    var tanggalMulaiKb by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }
    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val detailWusPus by anggotaViewModel.detailWusPusState.collectAsState()

    fun selectWarga(warga: AnggotaEntity) {
        if (warga.jenisKelamin.equals("Laki-laki", ignoreCase = true)) {
            Toast.makeText(context, "WUS/PUS harus seorang Perempuan!", Toast.LENGTH_SHORT).show()
        } else {
            anggotaViewModel.resetDetailWusPusState()
            selectedWarga = warga
            namaWarga = warga.nama

            // Reset local form fields when switching citizens
            namaPasangan = ""
            kategoriStatus = "WUS"
            tanggalMulaiKb = ""
            keterangan = ""
            fieldErrors = fieldErrors - "warga_id"

            if (warga.serverId != null) {
                anggotaViewModel.getDetailWusPusFromServer(token, warga.serverId)
            }
        }
    }

    fun resetScreenState() {
        anggotaViewModel.resetDetailWusPusState()
        selectedWarga = null
        namaWarga = ""
        namaPasangan = ""
        kategoriStatus = "WUS"
        tanggalMulaiKb = ""
        keterangan = ""
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

    LaunchedEffect(detailWusPus) {
        if (detailWusPus is UiState.Success) {
            val warga = selectedWarga ?: return@LaunchedEffect
            val dataServer = (detailWusPus as UiState.Success).data.data
            if (dataServer != null && dataServer.anggotaId == warga.serverId) {
                namaPasangan = dataServer.namaSuami ?: ""
                kategoriStatus = dataServer.statusKategori
                tanggalMulaiKb = normalizeDateForForm(dataServer.tanggalMulaiStatus)
                keterangan = dataServer.keterangan ?: ""
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
        topBar = { AppTopBar(title = "Update Wus/Pus", onBackClick = onBackClick) },
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
                    UpdateHeaderCard(title = "Update untuk", name = selectedWarga!!.nama, icon = Icons.Default.Female) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AppTextField(label = "Nama Warga", value = namaWarga, onValueChange = {}, readOnly = true)
                    }
                } else {
                    UpdateHeaderCard(title = "Pilih Warga", name = "Ketuk untuk mencari data", icon = Icons.Default.Search)
                }
            }
            fieldErrors["warga_id"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            FormSectionCard(title = null) {
                AppTextField(
                    label = "Nama Pasangan",
                    value = namaPasangan,
                    placeholder = "Masukkan nama lengkap pasangan",
                    onValueChange = { namaPasangan = it }
                )

                Column(modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)) {
                    Text(text = "Kategori Status (Wus/Pus)", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    ResponsiveTwoColumn(
                        first = { fieldModifier ->
                        AppRadioButton(
                            text = "WUS",
                            isSelected = kategoriStatus == "WUS",
                            onClick = { kategoriStatus = "WUS" },
                            modifier = fieldModifier
                        )
                        },
                        second = { fieldModifier ->
                        AppRadioButton(
                            text = "PUS",
                            isSelected = kategoriStatus == "PUS",
                            onClick = { kategoriStatus = "PUS" },
                            modifier = fieldModifier
                        )
                        }
                    )
                }

                AppDateField(
                    label = "Tanggal Mulai Status (Opsional)",
                    value = tanggalMulaiKb,
                    onValueChange = { tanggalMulaiKb = it }
                )

                AppTextField(
                    label = "Keterangan",
                    value = keterangan,
                    placeholder = "Tambahkan catatan tambahan jika diperlukan...",
                    singleLine = false,
                    onValueChange = { keterangan = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Wus/Pus",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        val warga = selectedWarga
                        if (warga == null) {
                            fieldErrors = mapOf("warga_id" to "Warga wajib dipilih.")
                            return@PrimaryButton
                        }

                        anggotaViewModel.updateDataWusPus(
                            token = token,
                            anggotaLocalId = warga.localId,
                            anggotaServerId = warga.serverId,
                            namaSuami = namaPasangan.ifBlank { null },
                            statusKategori = kategoriStatus,
                            tanggalMulaiStatus = tanggalMulaiKb.ifBlank { null },
                            keterangan = keterangan.ifBlank { null },
                            createdAt = warga.createdAt ?: "",
                            updatedAt = warga.updatedAt ?: ""
                        )

                        Toast.makeText(context, "Data WUS/PUS berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
