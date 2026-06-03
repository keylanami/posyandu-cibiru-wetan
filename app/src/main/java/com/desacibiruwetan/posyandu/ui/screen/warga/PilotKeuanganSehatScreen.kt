package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint

@Composable
fun PilotKeuanganSehatScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
){


    var keluargaAsuransiKesehatan by remember { mutableStateOf("") }
    var kepalaKeluargaPengangguran by remember { mutableStateOf("") }
    var kepalaKeluargaTidakTetap by remember { mutableStateOf("") }
    var kepalaKeluargaPenghasilanTetap by remember { mutableStateOf("") }
    var tabulinIbuHamil by remember { mutableStateOf("") }
    var keluargaPunyaTabungan by remember { mutableStateOf("") }
    var keluargaPunyaAsetInvestasi by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(title = "Keuangan Sehat", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        containerColor = BgMint
    ) {
        paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Spacer(Modifier.height(24.dp))

            FormSectionCard(null) {
                AppTextField(
                    label = "Keluarga Asuransi Kesehatan",
                    value = keluargaAsuransiKesehatan,
                    placeholder = "Masukkan jumlah keluarga asuransi kesehatan",
                   keyboardType = KeyboardType.Number,
                    onValueChange = { keluargaAsuransiKesehatan = it }
                )

                AppTextField(
                    label = "Kepala Keluarga Pengangguran",
                    value = kepalaKeluargaPengangguran,
                    placeholder = "Masukkan jumlah kepala keluarga pengangguran",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kepalaKeluargaPengangguran = it }

                )

                AppTextField(
                    label = "Kepala Keluarga Tidak Tetap",
                    value = kepalaKeluargaTidakTetap,
                    placeholder = "Masukkan jumlah kepala keluarga tidak tetap",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kepalaKeluargaTidakTetap = it}
                )

                AppTextField(
                    label = "Kepala Keluarga Penghasilan Tetap",
                    value = kepalaKeluargaPenghasilanTetap,
                    placeholder = "Masukkan jumlah kepala keluarga penghasilan tetap",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kepalaKeluargaPenghasilanTetap = it}
                )

                AppTextField(
                    label = "Tabulin Ibu Hamil",
                    value = tabulinIbuHamil,
                    placeholder = "Masukkan jumlah tabulin ibu hamil",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { tabulinIbuHamil = it}
                )

                AppTextField(
                    label = "Keluarga Punya Tabungan",
                    value = keluargaPunyaTabungan,
                    placeholder = "Masukkan jumlah keluarga dengan tabungan",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { keluargaPunyaTabungan = it}

                )

                AppTextField(
                    label = "Keluarga Punya Aset Investasi",
                    value = keluargaPunyaAsetInvestasi,
                    placeholder = "Masukkan jumlah keluarga dengan aset investasi",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { keluargaPunyaAsetInvestasi = it}
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Keuangan Sehat",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(Modifier.height(24.dp))
        }

    }
}