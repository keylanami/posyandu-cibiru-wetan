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
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint

@Composable
fun PilotKesBuNakScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
){
//    Ibu Hamil Rutin Periksa
//            Persalinan Tenaga Kesehatan
//    Kematian Ibu Nifas
//    Kanker Serviks
//            Imunisasi Bayi Balita
//    Bayi Balita Sakit Terdata
//            Kematian Bayi Balita


    var ibuHamilRutinPeriksa by remember { mutableStateOf("") }
    var persalinanTenagaKesehatan by remember { mutableStateOf("") }
    var kematianIbuNifas by remember { mutableStateOf("") }
    var kankerServiks by remember { mutableStateOf("") }
    var imunisasiBayiBalita by remember { mutableStateOf("") }
    var bayiBalitaSakitTerdata by remember { mutableStateOf("") }
    var kematianBayiBalita by remember { mutableStateOf("") }



    Scaffold(
        topBar = { AppTopBar(title = "Kesehatan Ibu & Anak", onBackClick = onBackClick) },
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
                    label = "Ibu Hamil Rutin Periksa",
                    value = ibuHamilRutinPeriksa,
                    placeholder = "Masukkan jumlah ibu hamil rutin periksa",
                    onValueChange = { ibuHamilRutinPeriksa = it }
                )

                AppTextField(
                    label = "Persalinan Tenaga Kesehatan",
                    value = persalinanTenagaKesehatan,
                    placeholder = "Masukkan jumlah persalinan tenaga kesehatan",
                    onValueChange = { persalinanTenagaKesehatan = it }
                )

                AppTextField(
                    label = "Kematian Ibu Nifas",
                    value = kematianIbuNifas,
                    placeholder = "Masukkan jumlah kematian ibu nifas",
                    onValueChange = { kematianIbuNifas = it }
                )

                AppTextField(
                    label = "Kanker Serviks",
                    value = kankerServiks,
                    placeholder = "Masukkan jumlah kanker serviks",
                    onValueChange = { kankerServiks = it }
                )

                AppTextField(
                    label = "Imunisasi Bayi Balita",
                    value = imunisasiBayiBalita,
                    placeholder = "Masukkan jumlah imunisasi bayi balita",
                    onValueChange = { imunisasiBayiBalita = it }
                )

                AppTextField(
                    label = "Bayi Balita Sakit Terdata",
                    value = bayiBalitaSakitTerdata,
                    placeholder = "Masukkan jumlah bayi balita sakit terdata",
                    onValueChange = { bayiBalitaSakitTerdata = it }
                )

                AppTextField(
                    label = "Kematian Bayi Balita",
                    value = kematianBayiBalita,
                    placeholder = "Masukkan jumlah kematian bayi balita",
                    onValueChange = { kematianBayiBalita = it }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Kesehatan Ibu dan Anak",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(Modifier.height(24.dp))

        }

    }
}