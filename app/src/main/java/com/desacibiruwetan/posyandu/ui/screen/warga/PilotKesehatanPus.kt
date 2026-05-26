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
fun PilotKesehatanPusScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
){

    var ibuMelahirkanBayiSehat by remember { mutableStateOf("") }
    var kbWanita by remember { mutableStateOf("") }
    var kbPria by remember { mutableStateOf("") }
    var pusMasalahReproduksi by remember { mutableStateOf("") }
    var menikahUsiaUnder20 by remember { mutableStateOf("") }
    var wusKehamilanBeresiko by remember { mutableStateOf("") }
    var imsPadaPUS by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(title = "Kesehatan Pus", onBackClick = onBackClick) },
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

            FormSectionCard(
                title = null
            ) {
                AppTextField(
                    label = "Ibu Melahirkan Bayi Sehat",
                    value = ibuMelahirkanBayiSehat,
                    placeholder = "Masukkan jumlah ibu hamil yang sehat",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { ibuMelahirkanBayiSehat = it }
                )

                AppTextField(
                    label = "KB Wanita",
                    value = kbWanita,
                    placeholder = "Masukkan jumlah KB wanita",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kbWanita = it }
                )

                AppTextField(
                    label = "KB Pria",
                    value = kbPria,
                    placeholder = "Masukkan jumlah KB pria",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kbPria = it }
                )

                AppTextField(
                    label = "PUS Masalah Reproduksi",
                    value = pusMasalahReproduksi,
                    placeholder = "Masukkan jumlah PUS masalah reproduksi",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { pusMasalahReproduksi = it}
                )

                AppTextField(
                    label = "Menikah Usia Dibawah 20",
                    value = menikahUsiaUnder20,
                    placeholder = "Masukkan jumlah menikah usia dibawah 20",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { menikahUsiaUnder20 = it}
                )

                AppTextField(
                    label = "WUS Kehamilan Berisiko",
                    value = wusKehamilanBeresiko,
                    placeholder = "Masukkan jumlah WUS kehamilan berisiko",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { wusKehamilanBeresiko = it}
                )

                AppTextField(
                    label = "IMS Pada PUS",
                    value = imsPadaPUS,
                    placeholder = "Masukkan jumlah IMS pada PUS",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { imsPadaPUS = it}
                )


                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Kesehatan Pus",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }
        }

    }

}