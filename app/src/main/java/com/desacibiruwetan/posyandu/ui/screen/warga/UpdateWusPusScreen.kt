package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import com.desacibiruwetan.posyandu.ui.components.input.AppRadioButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.utils.DateVisualTransformation

@Composable
fun UpdateWusPusScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<DummyDetailWarga?>(null) }

    var namaWarga by remember { mutableStateOf("") }
    var namaPasangan by remember { mutableStateOf("") }
    var kategoriStatus by remember { mutableStateOf("WUS") }
    var tanggalMulaiKb by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                selectedWarga = warga
                namaWarga = warga.name
                namaPasangan = warga.namaPasangan
            }
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Update Wus/Pus",
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
                        icon = Icons.Default.Female
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
                AppTextField(
                    label = "Nama Pasangan",
                    value = namaPasangan,
                    placeholder = "Masukkan nama lengkap pasangan",
                    onValueChange = { namaPasangan = it }
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                ) {
                    Text(
                        text = "Kategori Status (Wus/Pus)",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        AppRadioButton(
                            text = "WUS (Wanita Usia Subur)",
                            isSelected = kategoriStatus == "WUS",
                            onClick = { kategoriStatus = "WUS" }
                        )
                        AppRadioButton(
                            text = "PUS (Pasangan Usia Subur)",
                            isSelected = kategoriStatus == "PUS",
                            onClick = { kategoriStatus = "PUS" }
                        )
                    }
                }

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
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}