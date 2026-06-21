package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.dialog.SearchWargaDialog
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun UpdateBalitaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String,
    anggotaViewModel: AnggotaViewmodel
) {
    val context = LocalContext.current
    val token = SessionManager.getAuthorizationHeader(context)

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<AnggotaEntity?>(null) }

    var namaBalita by remember { mutableStateOf("") }
    var namaAyah by remember { mutableStateOf("") }
    var namaIbu by remember { mutableStateOf("") }
    var tinggiBadan by remember { mutableStateOf("") }
    var beratBadan by remember { mutableStateOf("") }

    val detailBalita by anggotaViewModel.detailBalitaState.collectAsState()

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                selectedWarga = warga
                namaBalita = warga.nama

                val ket = warga.keterangan ?: ""
                if (ket.contains("Ayah:") && ket.contains("Ibu:")) {
                    try {
                        namaAyah = ket.substringAfter("Ayah: ").substringBefore(", Ibu:").trim()
                        namaIbu = ket.substringAfter("Ibu: ").substringBefore(", BB:").trim()
                        beratBadan = ket.substringAfter("BB: ").substringBefore(" kg").trim()
                        tinggiBadan = ket.substringAfter("TB: ").substringBefore(" cm").trim()
                    } catch (e: Exception) {
                    }
                }

                if (warga.serverId != null) {
                    anggotaViewModel.getDetailBalitaFromServer(token, warga.serverId)
                }
            },
            anggotaViewModel = anggotaViewModel,
            filterByKategori = "Balita"
        )
    }

    LaunchedEffect(detailBalita) {
        if (detailBalita is UiState.Success) {
            val dataServer = (detailBalita as UiState.Success).data.data
            if (dataServer != null) {
                if (!dataServer.namaAyah.isNullOrBlank()) namaAyah = dataServer.namaAyah
                if (!dataServer.namaIbu.isNullOrBlank()) namaIbu = dataServer.namaIbu
                if (dataServer.tinggiBadan != null) tinggiBadan = dataServer.tinggiBadan.toString()
                if (dataServer.beratBadan != null) beratBadan = dataServer.beratBadan.toString()
            }
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Update Data Balita", onBackClick = onBackClick) },
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
                        icon = Icons.Default.ChildCare
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AppTextField(
                            label = "Nama Balita",
                            value = namaBalita,
                            onValueChange = {},
                            readOnly = true,
                            placeholder = "Pilih dari pencarian"
                        )
                    }
                } else {
                    UpdateHeaderCard(
                        title = "Pilih Balita",
                        name = "Ketuk untuk mencari data",
                        icon = Icons.Default.Search
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            FormSectionCard(title = "Pertumbuhan & Identitas Ortu") {
                Spacer(modifier = Modifier.height(8.dp))

                AppTextField(
                    label = "Nama Ayah",
                    value = namaAyah,
                    placeholder = "Masukkan nama ayah",
                    onValueChange = { namaAyah = it })
                AppTextField(
                    label = "Nama Ibu",
                    value = namaIbu,
                    placeholder = "Masukkan nama ibu",
                    onValueChange = { namaIbu = it })

                Row(Modifier.fillMaxWidth()) {
                    Box(Modifier.weight(1f)) {
                        AppTextField(
                            label = "Tinggi Badan (cm)",
                            value = tinggiBadan,
                            keyboardType = KeyboardType.Decimal,
                            placeholder = "0.0",
                            onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) tinggiBadan = it })
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(Modifier.weight(1f)) {
                        AppTextField(
                            label = "Berat Badan (kg)",
                            value = beratBadan,
                            keyboardType = KeyboardType.Decimal,
                            placeholder = "0.0",
                            onValueChange = { if (it.matches(Regex("^\\d*\\.?\\d*$"))) beratBadan = it })
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Simpan Update Balita",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        val warga = selectedWarga
                        if (warga == null) {
                            Toast.makeText(
                                context,
                                "Pilih Balita terlebih dahulu",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButton
                        }

                        if (namaAyah.isBlank() || namaIbu.isBlank()) {
                            Toast.makeText(
                                context,
                                "Nama ayah dan ibu wajib diisi",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButton
                        }

                        val tb = tinggiBadan.toDoubleOrNull()
                        val bb = beratBadan.toDoubleOrNull()
                        if (tb == null || tb <= 0.0 || bb == null || bb <= 0.0) {
                            Toast.makeText(
                                context,
                                "Tinggi dan berat badan harus lebih dari 0",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButton
                        }

                        anggotaViewModel.updateDataBalita(
                            token = token,
                            anggotaLocalId = warga.localId,
                            anggotaServerId = warga.serverId,
                            namaAyah = namaAyah,
                            namaIbu = namaIbu,
                            tb = tb,
                            bb = bb
                        )

                        Toast.makeText(
                            context,
                            "Data balita berhasil disimpan!",
                            Toast.LENGTH_SHORT
                        ).show()
                        onBackClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CircularIconBox(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(59.dp)
            .clip(CircleShape)
            .background(Color(0xFFC7FFEC)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(24.dp)
        )
    }
}
