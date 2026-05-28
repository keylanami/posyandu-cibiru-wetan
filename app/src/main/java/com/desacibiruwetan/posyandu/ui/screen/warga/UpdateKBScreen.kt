package com.desacibiruwetan.posyandu.ui.screen.warga

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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.model.DummyDetailWarga
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

@Composable
fun UpdateKbScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<DummyDetailWarga?>(null) }

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
                selectedWarga = warga
                namaWarga = warga.name
            }
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
                        name = selectedWarga!!.name,
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
                    text = "Update Data KB",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}