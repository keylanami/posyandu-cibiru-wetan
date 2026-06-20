package com.desacibiruwetan.posyandu.ui.screen.warga

import android.content.Context
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
import com.desacibiruwetan.posyandu.ui.components.input.AppRadioButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.DateVisualTransformation
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

private fun convertServerDateToRaw(serverDate: String?): String {
    if (serverDate.isNullOrEmpty()) return ""
    if (serverDate.contains("-")) {
        val parts = serverDate.split("-")
        if (parts.size == 3) {
            if (parts[0].length == 4) return "${parts[2]}${parts[1]}${parts[0]}"
            if (parts[2].length == 4) return "${parts[0]}${parts[1]}${parts[2]}"
        }
        return serverDate.replace("-", "").take(8)
    }
    if (serverDate.contains("T")) {
        val datePart = serverDate.substringBefore("T")
        val parts = datePart.split("-")
        if (parts.size == 3) return "${parts[2]}${parts[1]}${parts[0]}"
    }
    return serverDate.filter { it.isDigit() }.take(8)
}

private fun formatToApiDate(raw: String): String? {
    if (raw.isEmpty() || raw.length != 8) return null
    val d = raw.substring(0, 2)
    val m = raw.substring(2, 4)
    val y = raw.substring(4, 8)
    return "$d-$m-$y"
}

@Composable
fun UpdateWusPusScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE)
    val token = "Bearer ${sharedPreferences.getString("TOKEN", "")}"

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<AnggotaEntity?>(null) }

    var namaWarga by remember { mutableStateOf("") }
    var namaPasangan by remember { mutableStateOf("") }
    var kategoriStatus by remember { mutableStateOf("WUS") }
    var tanggalMulaiKb by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    val detailWusPus by anggotaViewModel.detailWusPusState.collectAsState()

    LaunchedEffect(detailWusPus) {
        if (detailWusPus is UiState.Success) {
            val dataServer = (detailWusPus as UiState.Success).data.data
            if (dataServer != null) {
                namaPasangan = dataServer.namaSuami ?: ""
                kategoriStatus = dataServer.statusKategori
                tanggalMulaiKb = convertServerDateToRaw(dataServer.tanggalMulaiStatus)
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

                AppTextField(
                    label = "Tanggal Mulai KB (Opsional)",
                    value = tanggalMulaiKb,
                    placeholder = "dd/mm/yyyy",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = DateVisualTransformation(),
                    onValueChange = { if (it.length <= 8 && it.all { char -> char.isDigit() }) tanggalMulaiKb = it }
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
                            Toast.makeText(context, "Pilih warga terlebih dahulu", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }

                        val apiTglMulai = formatToApiDate(tanggalMulaiKb)

                        anggotaViewModel.updateDataWusPus(
                            token = token,
                            anggotaLocalId = warga.localId,
                            anggotaServerId = warga.serverId,
                            namaSuami = namaPasangan.ifBlank { null },
                            statusKategori = kategoriStatus,
                            tanggalMulaiStatus = apiTglMulai,
                            keterangan = keterangan.ifBlank { null },
                            createdAt = warga.createdAt ?: "",
                            updatedAt = warga.updatedAt ?: ""
                        )

                        if (keterangan.isNotBlank()) {
                            anggotaViewModel.updateAnggota(
                                token = token, anggotaLokal = warga,
                                nikBaru = warga.nik, namaBaru = warga.nama,
                                tanggalLahirBaru = warga.tanggalLahir, jenisKelaminBaru = warga.jenisKelamin,
                                pendidikanTerakhirBaru = warga.pendidikanTerakhir ?: "", pekerjaanBaru = warga.pekerjaan ?: "",
                                noBpjsBaru = warga.noBpjs ?: "", keteranganBaru = keterangan,
                                statusKeluargaBaru = warga.statusKeluarga, statusSipilBaru = warga.statusSipil,
                                statusWargaBaru = warga.statusWarga ?: "aktif",
                                usiaBaru = warga.usia ?: "", kategoriUsiaBaru = warga.kategoriUsia ?: ""
                            )
                        }

                        Toast.makeText(context, "Data WUS/PUS berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}