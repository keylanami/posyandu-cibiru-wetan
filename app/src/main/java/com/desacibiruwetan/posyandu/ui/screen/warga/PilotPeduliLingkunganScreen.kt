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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun PilotPeduliLingkunganScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
){
//
//    Keluarga Punya Bak Sampah
//            Anggota Bank Sampah
//    Keluarga Pakai Spal
//    Kasus Banjir
//            Bak Sampah Desa
//    Rumah Ventilasi Baik
//    KLB

    var keluargaPunyaBakSampah by remember { mutableStateOf("") }
    var anggotaBankSampah by remember { mutableStateOf("") }
    var keluargaPakaiSPal by remember { mutableStateOf("") }
    var kasusBanjir by remember { mutableStateOf("") }
    var bakSampahDesa by remember { mutableStateOf("") }
    var rumahVentilasiBaik by remember { mutableStateOf("") }
    var klb by remember { mutableStateOf("") }


    Scaffold(
        topBar = { AppTopBar(title = "Peduli Lingkungan", onBackClick = onBackClick) },
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
                    label = "Keluarga Punya Bak Sampah",
                    value = keluargaPunyaBakSampah,
                    placeholder = "Masukkan jumlah keluarga dengan bak sampah",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { keluargaPunyaBakSampah = it}
                )

                AppTextField(
                    label = "Anggota Bank Sampah",
                    value = anggotaBankSampah,
                    placeholder = "Masukkan jumlah anggota bank sampah",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { anggotaBankSampah = it }
                )

                AppTextField(
                    label = "Keluarga Pakai Spal",
                    value = keluargaPakaiSPal,
                    placeholder = "Masukkan jumlah keluarga dengan pal",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { keluargaPakaiSPal = it }

                )

                AppTextField(
                    label = "Kasus Banjir",
                    value = kasusBanjir,
                    placeholder = "Masukkan jumlah kasus banjir",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kasusBanjir = it}
                )

                AppTextField(
                    label = "Bak Sampah Desa",
                    value = bakSampahDesa,
                    placeholder = "Masukkan jumlah bak sampah desa",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { bakSampahDesa = it }
                )

                AppTextField(
                    label = "Rumah Ventilasi Baik",
                    value = rumahVentilasiBaik,
                    placeholder = "Masukkan jumlah rumah ventilasi baik",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { rumahVentilasiBaik = it }
                )

                AppTextField(
                    label = "KLB",
                    value = klb,
                    placeholder = "Masukkan jumlah KLB",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { klb = it }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Peduli Lingkungan",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}