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
fun PilotBencanaAlamScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
){


    var bencanaAlam by remember { mutableStateOf("") }
    var kerusakanEkosistemEksploitasi by remember { mutableStateOf("") }
    var kerusakanEkosistemBencana by remember { mutableStateOf("") }
    var abrasi by remember { mutableStateOf("") }
    var alihFungsiLahan by remember { mutableStateOf("") }
    var restorasiLahan by remember { mutableStateOf("") }
    var kerusakanFasilitas by remember { mutableStateOf("") }




    Scaffold(
        topBar = { AppTopBar(title = "Bencana Alam", onBackClick = onBackClick) },
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

            Spacer(Modifier.height(24.dp))

            FormSectionCard(title = null) {
                AppTextField(
                    label = "Bencana Alam",
                    value = bencanaAlam,
                    placeholder = "Masukkan jumlah bencana alam",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { bencanaAlam = it }
                )

                AppTextField(
                    label = "Kerusakan Ekosistem Eksploitasi",
                    value = kerusakanEkosistemEksploitasi,
                    placeholder = "Masukkan jumlah kerusakan ekosistem eksploitasi",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kerusakanEkosistemEksploitasi = it }
                )

                AppTextField(
                    label = "Kerusakan Ekosistem Bencana",
                    value = kerusakanEkosistemBencana,
                    placeholder = "Masukkan jumlah kerusakan ekosistem bencana",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kerusakanEkosistemBencana = it }
                )

                AppTextField(
                    label = "Abrasi",
                    value = abrasi,
                    placeholder = "Masukkan jumlah abrasi",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { abrasi = it }
                )

                AppTextField(
                    label = "Alih Fungsi Lahan",
                    value = alihFungsiLahan,
                    placeholder = "Masukkan jumlah alih fungsi lahan",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { alihFungsiLahan = it }
                )

                AppTextField(
                    label = "Restorasi Lahan",
                    value = restorasiLahan,
                    placeholder = "Masukkan jumlah restorasi lahan",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { restorasiLahan = it }
                )

                AppTextField(
                    label = "Kerusakan Fasilitas",
                    value = kerusakanFasilitas,
                    placeholder = "Masukkan jumlah kerusakan fasilitas",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kerusakanFasilitas = it }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Bencana Alam",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}