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
fun PilotKelSehatBerkualitasScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
){

//    Keluarga Dengan 2 Anak
//            Berobat Faskes
//            Penyakit Menular
//            Penyakit Tidak Menular
//    Bayi Lahir Sehat
//    Bayi Lahir Cukup Bulan
//            Gangguan Jiwa Keluarga

    var keluargaDengan2Anak by remember { mutableStateOf("") }
    var berobatFaskes by remember { mutableStateOf("") }
    var penyakitMenular by remember { mutableStateOf("") }
    var penyakitTidakMenular by remember { mutableStateOf("") }
    var bayiLahirSehat by remember { mutableStateOf("") }
    var bayiLahirCukupBulan by remember { mutableStateOf("") }
    var gangguanJiwaKeluarga by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(title = "Keluarga Sehat Berkualitas", onBackClick = onBackClick) },
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

            FormSectionCard(title = null) {
                AppTextField(
                    label = "Keluarga Dengan 2 Anak",
                    value = "",
                    placeholder = "Masukkan jumlah keluarga dengan 2 anak",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { keluargaDengan2Anak = it}
                )

                AppTextField(
                    label = "Berobat Faskes",
                    value = "",
                    placeholder = "Masukkan jumlah berobat faskes",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { berobatFaskes = it }
                )

                AppTextField(
                    label = "Penyakit Menular",
                    value = "",
                    placeholder = "Masukkan jumlah penyakit menular",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { penyakitMenular = it}
                )

                AppTextField(
                    label = "Penyakit Tidak Menular",
                    value = "",
                    placeholder = "Masukkan jumlah penyakit tidak menular",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { penyakitTidakMenular = it}
                )

                AppTextField(
                    label = "Bayi Lahir Sehat",
                    value = "",
                    placeholder = "Masukkan jumlah bayi lahir sehat",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { bayiLahirSehat = it }
                )

                AppTextField(
                    label = "Bayi Lahir Cukup Bulan",
                    value = "",
                    placeholder = "Masukkan jumlah bayi lahir cukup bulan",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { bayiLahirCukupBulan = it}
                )

                AppTextField(
                    label = "Gangguan Jiwa Keluarga",
                    value = "",
                    placeholder = "Masukkan jumlah gangguan jiwa keluarga",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { gangguanJiwaKeluarga = it }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Keluarga Sehat Berkualitas",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}