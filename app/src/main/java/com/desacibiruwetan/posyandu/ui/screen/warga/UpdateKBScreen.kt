package com.desacibiruwetan.posyandu.ui.screen.warga

import android.content.Context
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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.dialog.SearchWargaDialog
import com.desacibiruwetan.posyandu.ui.components.input.AppDropdownField
import com.desacibiruwetan.posyandu.ui.components.input.AppSwitch
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.DateVisualTransformation
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


private fun formatToApiDate(raw: String): String? {
    if (raw.isEmpty() || raw.length != 8) return null
    val d = raw.substring(0, 2)
    val m = raw.substring(2, 4)
    val y = raw.substring(4, 8)
    return "$d-$m-$y"
}

@Composable
fun UpdateKbScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String,
    anggotaViewModel: AnggotaViewmodel,
) {

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE)
    val token = "Bearer ${sharedPreferences.getString("TOKEN", "")}"

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<AnggotaEntity?>(null) }

    var namaWarga by remember { mutableStateOf("") }
    var jenisKb by remember { mutableStateOf("") }
    var tanggalMulaiKb by remember { mutableStateOf("") }
    var statusAktif by remember { mutableStateOf(true) }
    var keterangan by remember { mutableStateOf("") }

    val jenisKbOptions = listOf("IUD", "Suntik", "Pil", "Kondom", "Implan", "MOW", "MOP")

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                if (warga.jenisKelamin.equals("Laki-laki", ignoreCase = true)) {
                    Toast.makeText(context, "Akseptor KB harus WUS/PUS (Perempuan)!", Toast.LENGTH_SHORT).show()
                } else {
                    selectedWarga = warga
                    namaWarga = warga.nama
                    // Catatan: Jika ada state GET KB Autofill, panggil di sini
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
                            onValueChange = { namaWarga = it },
                            placeholder = "Masukkan nama warga"
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

            Spacer(modifier = Modifier.height(24.dp))


            FormSectionCard(title = null) {
                AppDropdownField(
                    label = "Jenis KB",
                    value = jenisKb,
                    options = jenisKbOptions,
                    onValueChange = { jenisKb = it }
                )

                AppTextField(
                    label = "Tanggal Mulai KB",
                    value = tanggalMulaiKb,
                    placeholder = "dd/mm/yyyy",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = DateVisualTransformation(),
                    onValueChange = {
                        if (it.length <= 8 && it.all { char -> char.isDigit() }) tanggalMulaiKb = it
                    }
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
                    text = "Simpan Data KB",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        val warga = selectedWarga
                        if (warga == null) {
                            Toast.makeText(context, "Pilih WUS/PUS terlebih dahulu!", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }
                        if (jenisKb.isBlank()) {
                            Toast.makeText(context, "Jenis KB wajib diisi!", Toast.LENGTH_SHORT).show()
                            return@PrimaryButton
                        }

                        val currentDate = SimpleDateFormat(
                            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                            Locale.getDefault()
                        ).format(
                            Date()
                        )

                        anggotaViewModel.updateDataKb(
                            token = token,
                            kbLocalId = 0,
                            kbServerId = null,
                            wusPusIdServer = warga.serverId ?: 0,
                            jenisKb = jenisKb,
                            tanggalMulaiKb = formatToApiDate(tanggalMulaiKb),
                            statusAktif = statusAktif,
                            keterangan = keterangan.ifBlank { null },
                            createdAt = currentDate,
                            updatedAt = currentDate
                        )

                        Toast.makeText(context, "Data KB berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}