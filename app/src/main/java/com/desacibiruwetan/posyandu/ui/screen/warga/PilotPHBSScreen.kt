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
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint

@Composable
fun PilotPHBSScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {


    var patuhProtokolKesehatan by remember { mutableStateOf("") }
    var rumahJambanSehat by remember { mutableStateOf("") }
    var rumahAirBersih by remember { mutableStateOf("") }
    var kasusDiare by remember { mutableStateOf("") }
    var rumahTanpaAsapRokok by remember { mutableStateOf("") }
    var babs by remember { mutableStateOf("") }


    Scaffold(
        topBar = { AppTopBar(title = "PHBS", onBackClick = onBackClick) },
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
                    label = "Patuh Protokol Kesehatan",
                    value = patuhProtokolKesehatan,
                    placeholder = "Masukkan jumlah patuh protokol kesehatan",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { patuhProtokolKesehatan = it }
                )
                AppTextField(
                    label = "Rumah Jamban Sehat",
                    value = rumahJambanSehat,
                    placeholder = "Masukkan jumlah rumah jamban sehat",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { rumahJambanSehat = it }
                )
                AppTextField(
                    label = "Rumah Air Bersih",
                    value = rumahAirBersih,
                    placeholder = "Masukkan jumlah rumah air bersih",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { rumahAirBersih = it }
                )
                AppTextField(
                    label = "Kasus Diare",
                    value = kasusDiare,
                    placeholder = "Masukkan jumlah kasus diare",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kasusDiare = it }
                )
                AppTextField(
                    label = "Rumah Tanpa Asap Rokok",
                    value = rumahTanpaAsapRokok,
                    placeholder = "Masukkan jumlah rumah tanpa asap rokok",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { rumahTanpaAsapRokok = it }
                )
                AppTextField(
                    label = "BABS",
                    value = babs,
                    placeholder = "Masukkan jumlah BABS",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { babs = it }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data PHBS",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(Modifier.height(24.dp))


        }

    }


}