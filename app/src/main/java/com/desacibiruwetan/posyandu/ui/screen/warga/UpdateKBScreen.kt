package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.dialog.SearchWargaDialog
import com.desacibiruwetan.posyandu.ui.components.input.AppDateField
import com.desacibiruwetan.posyandu.ui.components.input.AppDropdownField
import com.desacibiruwetan.posyandu.ui.components.input.AppSwitch
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun UpdateKbScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String,
    anggotaViewModel: AnggotaViewmodel,
) {
    val context = LocalContext.current
    val token = SessionManager.getAuthorizationHeader(context)

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<AnggotaEntity?>(null) }

    var namaWarga by remember { mutableStateOf("") }
    var jenisKb by remember { mutableStateOf("") }
    var tanggalMulaiKb by remember { mutableStateOf("") }
    var statusAktif by remember { mutableStateOf(true) }
    var keterangan by remember { mutableStateOf("") }
    var isSaving by remember { mutableStateOf(false) }
    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val jenisKbOptions = listOf("IUD", "Suntik", "Pil", "Kondom", "Implan", "MOW", "MOP")
    val detailWusPus by anggotaViewModel.detailWusPusState.collectAsState()

    LaunchedEffect(detailWusPus) {
        if (detailWusPus is UiState.Error) {
            Toast.makeText(context, "Warga ini belum punya data WUS/PUS", Toast.LENGTH_SHORT).show()
        }
    }

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                selectedWarga = warga
                namaWarga = warga.nama
                fieldErrors = fieldErrors - "warga_id"
                if (warga.serverId != null) {
                    anggotaViewModel.getDetailWusPusFromServer(token, warga.serverId)
                }
            },
            anggotaViewModel = anggotaViewModel
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Update Data KB",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            AppNavBar(
                selectedIndex = 1,
                onItemSelected = onNavItemSelected
            )
        },
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
                    UpdateHeaderCard(
                        title = "Update untuk",
                        name = selectedWarga!!.nama,
                        icon = Icons.Default.People
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AppTextField(
                            label = "Nama Warga",
                            value = namaWarga,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = "Pilih dari pencarian"
                        )
                    }
                } else {
                    UpdateHeaderCard(
                        title = "Pilih Warga",
                        name = "Ketuk untuk mencari data",
                        icon = Icons.Default.Search
                    )
                }
            }
            fieldErrors["warga_id"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))


            FormSectionCard(title = null) {
                AppDropdownField(
                    label = "Jenis KB",
                    value = jenisKb,
                    options = jenisKbOptions,
                    error = fieldErrors["jenis_kb"],
                    onValueChange = {
                        jenisKb = it
                        fieldErrors = fieldErrors - "jenis_kb"
                    }
                )

                AppDateField(
                    label = "Tanggal Mulai KB",
                    value = tanggalMulaiKb,
                    onValueChange = { tanggalMulaiKb = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppSwitch(
                    label = "Status Aktif",
                    description = "Apakah penggunaan KB masih berjalan?",
                    checked = statusAktif,
                    onCheckedChange = { statusAktif = it }
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppTextField(
                    label = "Keterangan",
                    value = keterangan,
                    placeholder = "Tambahkan catatan tambahan jika diperlukan...",
                    singleLine = false,
                    onValueChange = { keterangan = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = if (isSaving) "Menyimpan..." else "Simpan Data KB",
                    icon = Icons.Default.AddCircleOutline,
                    enabled = !isSaving,
                    onClick = {
                        if (selectedWarga == null) {
                            fieldErrors = mapOf("warga_id" to "Warga wajib dipilih.")
                            return@PrimaryButton
                        }
                        if (jenisKb.isBlank()) {
                            fieldErrors = mapOf("jenis_kb" to "Jenis KB wajib dipilih.")
                            return@PrimaryButton
                        }
                        val wusPusId = (detailWusPus as? UiState.Success)?.data?.data?.id
                        if (wusPusId == null) {
                            fieldErrors = mapOf("warga_id" to "Pilih warga yang sudah terdaftar WUS/PUS.")
                            return@PrimaryButton
                        }

                        isSaving = true
                        anggotaViewModel.createKb(
                            token = token,
                            wusPusId = wusPusId,
                            jenisKb = jenisKb,
                            tanggalMulaiKb = tanggalMulaiKb.ifBlank { null },
                            statusAktif = statusAktif,
                            keterangan = keterangan.ifBlank { null }
                        ) { success, message ->
                            isSaving = false
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) onBackClick()
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
