package com.desacibiruwetan.posyandu.ui.screen.warga

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
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.model.DummyDetailWarga
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.dialog.SearchWargaDialog
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint

@Composable
fun RumahKeluargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<DummyDetailWarga?>(null) }

    var namaWarga by remember { mutableStateOf("") }
    var noRumah by remember { mutableStateOf("") }
    var rt by remember { mutableStateOf("04") }
    var rw by remember { mutableStateOf("02") }
    var noKk by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                selectedWarga = warga
                namaWarga = warga.name
                noRumah = warga.noRumah
                noKk = warga.noKk

                val splitRtRw = warga.rtRw.split(" / ")
                if (splitRtRw.size == 2) {
                    rt = splitRtRw[0].replace("RT", "")
                    rw = splitRtRw[1].replace("RW", "")
                }
            }
        )
    }

    Scaffold(
        topBar = { AppTopBar(title = "Rumah Keluarga", onBackClick = onBackClick) },
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
                        name = selectedWarga!!.name,
                        icon = Icons.Default.Female
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AppTextField(
                            label = "Nama Warga",
                            value = namaWarga,
                            onValueChange = { namaWarga = it }
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
                    label = "No Rumah",
                    value = noRumah,
                    placeholder = "Masukkan No Rumah",
                    onValueChange = { noRumah = it }
                )

                AppTextField(
                    label = "RT",
                    value = rt,
                    placeholder = "00",
                    readOnly = true,
                    onValueChange = {},
                    keyboardType = KeyboardType.Number
                )

                AppTextField(
                    label = "RW",
                    value = rw,
                    placeholder = "00",
                    readOnly = true,
                    onValueChange = {},
                    keyboardType = KeyboardType.Number
                )

                AppTextField(
                    label = "No KK",
                    value = noKk,
                    placeholder = "Masukkan no KK",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { if (it.all { char -> char.isDigit() }) noKk = it }
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
                    text = "Update Data Keluarga",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}