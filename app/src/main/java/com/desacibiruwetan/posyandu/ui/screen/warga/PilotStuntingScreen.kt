package com.desacibiruwetan.posyandu.ui.screen.warga

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
fun PilotStuntingScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {

    var bayiLahirPrematur by remember { mutableStateOf("") }
    var bayiBBLR by remember { mutableStateOf("") }
    var balitaKurangGizi by remember { mutableStateOf("") }
    var balitaStunting by remember { mutableStateOf("") }
    var balitaRutinPemeriksaanTumbuhKembang by remember { mutableStateOf("") }
    var kehamilanTidakDirencanakan by remember { mutableStateOf("") }
    var jarakKehamilanTerlaluDekat by remember { mutableStateOf("") }


    Scaffold(
        topBar = { AppTopBar(title = "Stunting", onBackClick = onBackClick) },
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

            Spacer(modifier = Modifier.height(24.dp))

            FormSectionCard(title = null) {
                AppTextField(
                    label = "Bayi Lahir Prematur",
                    value = bayiLahirPrematur,
                    placeholder = "Masukkan jumlah bayi lahir prematur",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { bayiLahirPrematur = it }
                )
                AppTextField(
                    label = "Bayi BBLR",
                    value = bayiBBLR,
                    placeholder = "Masukkan jumlah bayi BBLR",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { bayiBBLR = it }
                )
                AppTextField(
                    label = "Balita Kurang Gizi",
                    value = balitaKurangGizi,
                    placeholder = "Masukkan jumlah balita kurang gizi",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { balitaKurangGizi = it }
                )
                AppTextField(
                    label = "Balita Stunting",
                    value = balitaStunting,
                    placeholder = "Masukkan jumlah balita stunting",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { balitaStunting = it }
                )
                AppTextField(
                    label = "Balita Rutin Pemeriksaan Tumbuh Kembang",
                    value = balitaRutinPemeriksaanTumbuhKembang,
                    placeholder = "Masukkan jumlah balita rutin pemeriksaan tumbuh kembang",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { balitaRutinPemeriksaanTumbuhKembang = it }
                )
                AppTextField(
                    label = "Kelahiran tidak direncanakan",
                    value = kehamilanTidakDirencanakan,
                    placeholder = "Masukkan jumlah kelahiran tidak direncanakan",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kehamilanTidakDirencanakan = it }
                )
                AppTextField(
                    label = "Jarak Kehamilan Terlalu Dekat",
                    value = jarakKehamilanTerlaluDekat,
                    placeholder = "Masukkan jumlah jarak kehamilan terlalu dekat",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { jarakKehamilanTerlaluDekat = it}
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Stunting",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


        }
    }
}