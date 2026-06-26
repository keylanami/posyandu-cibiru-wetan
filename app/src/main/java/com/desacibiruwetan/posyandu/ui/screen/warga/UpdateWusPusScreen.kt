package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Female
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
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
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.utils.normalizeDateForForm
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun UpdateWusPusScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
) {
    val context = LocalContext.current
    val token = SessionManager.getAuthorizationHeader(context)

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<AnggotaEntity?>(null) }

    var namaWarga by remember { mutableStateOf("") }
    var namaPasangan by remember { mutableStateOf("") }
    var kategoriStatus by remember { mutableStateOf("WUS") }
    var tanggalMulaiKb by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }
    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val detailWusPus by anggotaViewModel.detailWusPusState.collectAsState()

    LaunchedEffect(detailWusPus) {
        if (detailWusPus is UiState.Success) {
            val dataServer = (detailWusPus as UiState.Success).data.data
            if (dataServer != null) {
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
                if (warga.jenisKelamin.equals("Laki-laki", ignoreCase = true)) {
                    Toast.makeText(context, "WUS/PUS harus seorang Perempuan!", Toast.LENGTH_SHORT).show()
                } else {
                    selectedWarga = warga
                    namaWarga = warga.nama
                    fieldErrors = fieldErrors - "warga_id"
                    if (warga.serverId != null) {
                        anggotaViewModel.getDetailWusPusFromServer(token, warga.serverId)
                    }
                }
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.clickable { showDialog = true }) {
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
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        AppRadioButton(
                            text = "WUS",
                            isSelected = kategoriStatus == "WUS",
                            onClick = { kategoriStatus = "WUS" }
                        )
                        AppRadioButton(
                            text = "PUS",
                            isSelected = kategoriStatus == "PUS",
                            onClick = { kategoriStatus = "PUS" }
                        )
                    }
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
