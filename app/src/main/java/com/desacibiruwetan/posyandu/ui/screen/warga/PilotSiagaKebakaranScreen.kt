package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.gestures.scrollable
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
fun PilotSiagaKebakaraanScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
){

//    Kebakaran Rumah Tangga
//    Kebakaran Non Rumah Tangga
//            Rumah Punya APAR atau Air
//    Rumah Semi Permanen Kayu
//            Rumah Punya P3K
//    Kecelakaan Rumah Tangga
//    Instalasi Hydrant


    var kebakaranRumahTangga by remember { mutableStateOf("") }
    var kebakaranNonRumahTangga by remember { mutableStateOf("") }
    var rumahPunyaAPARatauAir by remember { mutableStateOf("") }
    var rumahSemiPermanenKayu by remember { mutableStateOf("") }
    var rumahPunyaP3K by remember { mutableStateOf("") }
    var kecelakaanRumahTangga by remember { mutableStateOf("") }
    var instalasiHydrant by remember { mutableStateOf("") }



    Scaffold(
        topBar = { AppTopBar(title = "Siaga Kebakaran", onBackClick = onBackClick) },
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
                    label = "Kebakaran Rumah Tangga",
                    value = kebakaranRumahTangga,
                    placeholder = "Masukkan jumlah kebakaran rumah tangga",
                    onValueChange = { kebakaranRumahTangga = it }
                )
                AppTextField(
                    label = "Kebakaran Non Rumah Tangga",
                    value = kebakaranNonRumahTangga,
                    placeholder = "Masukkan jumlah kebakaran non rumah tangga",
                    onValueChange = { kebakaranNonRumahTangga = it }
                )
                AppTextField(
                    label = "Rumah Punya APAR atau Air",
                    value = rumahPunyaAPARatauAir,
                    placeholder = "Masukkan jumlah rumah dengan APAR atau air",
                    onValueChange = { rumahPunyaAPARatauAir = it }
                )
                AppTextField(
                    label = "Rumah Semi Permanen Kayu",
                    value = rumahSemiPermanenKayu,
                    placeholder = "Masukkan jumlah rumah semi permanen kayu",
                    onValueChange = { rumahSemiPermanenKayu = it }
                )
                AppTextField(
                    label = "Rumah Punya P3K",
                    value = rumahPunyaP3K,
                    placeholder = "Masukkan jumlah rumah dengan P3K",
                    onValueChange = { rumahPunyaP3K = it }
                )
                AppTextField(
                    label = "Kecelakaan Rumah Tangga",
                    value = kecelakaanRumahTangga,
                    placeholder = "Masukkan jumlah kecelakaan rumah tangga",
                    onValueChange = { kecelakaanRumahTangga = it }
                )
                AppTextField(
                    label = "Instalasi Hydrant",
                    value = instalasiHydrant,
                    placeholder = "Masukkan jumlah instalasi hydrant",
                    onValueChange = { instalasiHydrant = it }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Siaga Kebakaran",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )

            }

            Spacer(Modifier.height(24.dp))
        }
    }
}