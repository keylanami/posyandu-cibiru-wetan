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
import androidx.compose.material3.Scaffold
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
import com.desacibiruwetan.posyandu.ui.components.input.AppSwitch
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.DateVisualTransformation
import com.desacibiruwetan.posyandu.utils.SessionManager
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
fun UpdateBumilScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel
) {
    val context = LocalContext.current
    val token = SessionManager.getAuthorizationHeader(context)

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<AnggotaEntity?>(null) }
    var namaBumil by remember { mutableStateOf("") }

    var hamilKe by remember { mutableStateOf("") }
    var asiEksklusif by remember { mutableStateOf(false) }
    var tanggalMulaiAsi by remember { mutableStateOf("") }
    var tanggalSelesaiAsi by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    val detailBumil by anggotaViewModel.detailBumilState.collectAsState()

    LaunchedEffect(detailBumil) {
        if (detailBumil is UiState.Success) {
            val dataServer = (detailBumil as UiState.Success).data.data
            if (dataServer != null) {
                hamilKe = dataServer.hamilKe.toString()
                asiEksklusif = dataServer.asiEksklusif
                tanggalMulaiAsi = convertServerDateToRaw(dataServer.tanggalMulaiAsi)
                tanggalSelesaiAsi = convertServerDateToRaw(dataServer.tanggalSelesaiAsi)
            }
        }
    }

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                if (warga.jenisKelamin.equals("Laki-laki", ignoreCase = true)) {
                    Toast.makeText(context, "Bumil harus seorang Perempuan!", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    selectedWarga = warga
                    namaBumil = warga.nama
                    if (warga.serverId != null) {
                        anggotaViewModel.getDetailBumilFromServer(token, warga.serverId)
                    }
                }
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
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.clickable { showDialog = true }) {
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

            Spacer(modifier = Modifier.height(24.dp))

            FormSectionCard(title = "Data Kehamilan") {
                AppTextField(
                    label = "Hamil Ke-",
                    value = hamilKe,
                    placeholder = "Contoh: 1",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { if (it.all { char -> char.isDigit() }) hamilKe = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            FormSectionCard(title = "Data Menyusui & Catatan") {
                AppSwitch(
                    label = "ASI Eksklusif?",
                    description = "Apakah ibu memberikan ASI Eksklusif?",
                    checked = asiEksklusif,
                    onCheckedChange = { asiEksklusif = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    label = "Tanggal Mulai ASI",
                    value = tanggalMulaiAsi,
                    placeholder = "dd/mm/yyyy",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = DateVisualTransformation(),
                    onValueChange = {
                        if (it.length <= 8 && it.all { char -> char.isDigit() }) tanggalMulaiAsi =
                            it
                    }
                )

                AppTextField(
                    label = "Tanggal Selesai ASI (Opsional)",
                    value = tanggalSelesaiAsi,
                    placeholder = "dd/mm/yyyy",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = DateVisualTransformation(),
                    onValueChange = {
                        if (it.length <= 8 && it.all { char -> char.isDigit() }) tanggalSelesaiAsi =
                            it
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    label = "Keterangan",
                    value = keterangan,
                    placeholder = "Tambahkan catatan jika diperlukan...",
                    singleLine = false,
                    onValueChange = { keterangan = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Simpan Data Bumil",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        val warga = selectedWarga
                        if (warga == null) {
                            Toast.makeText(
                                context,
                                "Pilih Ibu Hamil terlebih dahulu",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButton
                        }

                        val hKe = hamilKe.toIntOrNull()
                        if (hKe == null || hKe < 1) {
                            Toast.makeText(
                                context,
                                "Hamil ke- harus diisi minimal 1",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButton
                        }

                        val apiTglMulai = formatToApiDate(tanggalMulaiAsi)
                        val apiTglSelesai = formatToApiDate(tanggalSelesaiAsi)

                        anggotaViewModel.updateDataBumil(
                            token = token,
                            anggotaLocalId = warga.localId,
                            anggotaServerId = warga.serverId,
                            hamilKe = hKe,
                            asiEksklusif = asiEksklusif,
                            tglMulaiAsi = apiTglMulai,
                            tglSelesaiAsi = apiTglSelesai,
                            createdAt = warga.createdAt ?: "",
                            updatedAt = warga.updatedAt ?: ""
                        )

                        if (keterangan.isNotBlank()) {
                            anggotaViewModel.updateAnggota(
                                token = token,
                                anggotaLokal = warga,
                                nikBaru = warga.nik,
                                namaBaru = warga.nama,
                                tanggalLahirBaru = warga.tanggalLahir,
                                jenisKelaminBaru = warga.jenisKelamin,
                                pendidikanTerakhirBaru = warga.pendidikanTerakhir ?: "",
                                pekerjaanBaru = warga.pekerjaan ?: "",
                                noBpjsBaru = warga.noBpjs ?: "",
                                keteranganBaru = keterangan,
                                statusKeluargaBaru = warga.statusKeluarga,
                                statusSipilBaru = warga.statusSipil,
                                statusWargaBaru = warga.statusWarga ?: "aktif",
                                usiaBaru = warga.usia ?: "",
                                kategoriUsiaBaru = warga.kategoriUsia ?: ""
                            )
                        }

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
