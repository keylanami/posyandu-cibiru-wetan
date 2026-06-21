package com.desacibiruwetan.posyandu.ui.screen.warga

import android.content.Context
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.viewmodel.PilotViewmodel

@Composable
fun PilotSiagaKebakaraanScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String,
    pilotViewModel: PilotViewmodel
){

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE)
    val token = "Bearer ${sharedPreferences.getString("TOKEN", "")}"

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
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kebakaranRumahTangga = it }
                )
                AppTextField(
                    label = "Kebakaran Non Rumah Tangga",
                    value = kebakaranNonRumahTangga,
                    placeholder = "Masukkan jumlah kebakaran non rumah tangga",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kebakaranNonRumahTangga = it }
                )
                AppTextField(
                    label = "Rumah Punya APAR atau Air",
                    value = rumahPunyaAPARatauAir,
                    placeholder = "Masukkan jumlah rumah dengan APAR atau air",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { rumahPunyaAPARatauAir = it }
                )
                AppTextField(
                    label = "Rumah Semi Permanen Kayu",
                    value = rumahSemiPermanenKayu,
                    placeholder = "Masukkan jumlah rumah semi permanen kayu",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { rumahSemiPermanenKayu = it }
                )
                AppTextField(
                    label = "Rumah Punya P3K",
                    value = rumahPunyaP3K,
                    placeholder = "Masukkan jumlah rumah dengan P3K",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { rumahPunyaP3K = it }
                )
                AppTextField(
                    label = "Kecelakaan Rumah Tangga",
                    value = kecelakaanRumahTangga,
                    placeholder = "Masukkan jumlah kecelakaan rumah tangga",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kecelakaanRumahTangga = it }
                )
                AppTextField(
                    label = "Instalasi Hydrant",
                    value = instalasiHydrant,
                    placeholder = "Masukkan jumlah instalasi hydrant",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { instalasiHydrant = it }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Siaga Kebakaran",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        pilotViewModel.submitSiagaKebakaran(
                            token = token,
                            kebakaranRumahTangga = kebakaranRumahTangga.toIntOrNull(),
                            kebakaranNonRumahTangga = kebakaranNonRumahTangga.toIntOrNull(),
                            rumahPunyaAparAtauAir = rumahPunyaAPARatauAir.toIntOrNull(),
                            rumahSemiPermanenKayu = rumahSemiPermanenKayu.toIntOrNull(),
                            rumahPunyaP3k = rumahPunyaP3K.toIntOrNull(),
                            kecelakaanRumahTangga = kecelakaanRumahTangga.toIntOrNull(),
                            instalasiHydrant = instalasiHydrant.toIntOrNull()
                        )
                        Toast.makeText(context, "Data Siaga Kebakaran berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                )

            }

            Spacer(Modifier.height(24.dp))
        }
    }
}