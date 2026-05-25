package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.model.DummyDetailWarga
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

@Composable
fun UpdateBumilScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<DummyDetailWarga?>(null) }

    var namaIbu by remember { mutableStateOf("") }
    var namaSuami by remember { mutableStateOf("") }
    var noKk by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }

    var hamilKe by remember { mutableStateOf("") }
    var usiaAnakTerkecil by remember { mutableStateOf("") }
    var usiaKehamilan by remember { mutableStateOf("") }
    var perkiraanHpl by remember { mutableStateOf("") }

    var sedangMenyusui by remember { mutableStateOf(false) }
    var keterangan by remember { mutableStateOf("") }

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                selectedWarga = warga
                namaIbu = warga.name
                namaSuami = warga.namaPasangan
                noKk = warga.noKk
                nik = warga.nik
            }
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
                        name = selectedWarga!!.name,
                        icon = Icons.Default.PregnantWoman
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AppTextField(
                            label = "Nama Warga",
                            value = namaIbu,
                            onValueChange = { namaIbu = it }
                        )
                    }
                } else {
                    UpdateHeaderCard(
                        title = "Pilih Ibu Hamil",
                        name = "Ketuk untuk mencari data",
                        icon = Icons.Default.Search
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            FormSectionCard(title = "Identitas Utama") {
                AppTextField(
                    label = "Nama Ibu Hamil",
                    value = namaIbu,
                    onValueChange = { namaIbu = it })
                AppTextField(
                    label = "Nama Suami",
                    value = namaSuami,
                    onValueChange = { namaSuami = it })
                AppTextField(
                    label = "Nomor Kartu Keluarga",
                    value = noKk,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { if (it.all { char -> char.isDigit() }) noKk = it }
                )
                AppTextField(
                    label = "NIK",
                    value = nik,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { if (it.all { char -> char.isDigit() }) nik = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            FormSectionCard(title = "Rincian Kehamilan") {
                AppTextField(
                    label = "Hamil ke-",
                    value = hamilKe,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { hamilKe = it })
                AppTextField(
                    label = "Usia Anak Terkecil",
                    value = usiaAnakTerkecil,
                    placeholder = "Contoh: 2 Tahun",
                    onValueChange = { usiaAnakTerkecil = it })
                AppTextField(
                    label = "Usia Kehamilan (minggu)",
                    value = usiaKehamilan,
                    keyboardType = KeyboardType.Number,
                    onValueChange = { usiaKehamilan = it })
                AppTextField(
                    label = "Perkiraan HPL",
                    value = perkiraanHpl,
                    placeholder = "dd/mm/yyyy",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = DateVisualTransformation(),
                    onValueChange = { if (it.length <= 8) perkiraanHpl = it }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            FormSectionCard(title = "Status & Catatan") {
                AppSwitch(
                    label = "Sedang Menyusui?",
                    description = "Apakah ibu masih menyusui anak sebelumnya?",
                    checked = sedangMenyusui,
                    onCheckedChange = { sedangMenyusui = it }
                )

                AppTextField(
                    label = "Keterangan",
                    value = keterangan,
                    placeholder = "Tambahkan catatan jika diperlukan...",
                    singleLine = false,
                    onValueChange = { keterangan = it }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Bumil",
                    onClick = { /* TODO: save*/ }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}