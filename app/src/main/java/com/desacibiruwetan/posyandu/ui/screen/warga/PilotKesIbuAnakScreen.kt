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
fun PilotKesBuNakScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String,
    pilotViewModel: PilotViewmodel
){

    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE)
    val token = "Bearer ${sharedPreferences.getString("TOKEN", "")}"


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
                    keyboardType = KeyboardType.Number,
                    onValueChange = { ibuHamilRutinPeriksa = it }
                )

                AppTextField(
                    label = "Persalinan Tenaga Kesehatan",
                    value = persalinanTenagaKesehatan,
                    keyboardType = KeyboardType.Number,
                    placeholder = "Masukkan jumlah persalinan tenaga kesehatan",
                    onValueChange = { persalinanTenagaKesehatan = it }
                )

                AppTextField(
                    label = "Kematian Ibu Nifas",
                    value = kematianIbuNifas,
                    placeholder = "Masukkan jumlah kematian ibu nifas",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kematianIbuNifas = it }
                )

                AppTextField(
                    label = "Kanker Serviks",
                    value = kankerServiks,
                    placeholder = "Masukkan jumlah kanker serviks",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kankerServiks = it }
                )

                AppTextField(
                    label = "Imunisasi Bayi Balita",
                    value = imunisasiBayiBalita,
                    placeholder = "Masukkan jumlah imunisasi bayi balita",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { imunisasiBayiBalita = it }
                )

                AppTextField(
                    label = "Bayi Balita Sakit Terdata",
                    value = bayiBalitaSakitTerdata,
                    placeholder = "Masukkan jumlah bayi balita sakit terdata",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { bayiBalitaSakitTerdata = it }
                )

                AppTextField(
                    label = "Kematian Bayi Balita",
                    value = kematianBayiBalita,
                    placeholder = "Masukkan jumlah kematian bayi balita",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { kematianBayiBalita = it }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Kesehatan Ibu dan Anak",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        pilotViewModel.submitKia(
                            token = token,
                            ibuHamilRutinPeriksa = ibuHamilRutinPeriksa.toIntOrNull(),
                            persalinanTenagaKesehatan = persalinanTenagaKesehatan.toIntOrNull(),
                            kematianIbuNifas = kematianIbuNifas.toIntOrNull(),
                            kankerServiks = kankerServiks.toIntOrNull(),
                            imunisasiBayiBalita = imunisasiBayiBalita.toIntOrNull(),
                            batiBalitaSakitTerdata = bayiBalitaSakitTerdata.toIntOrNull(),
                            kematianBayiBalita = kematianBayiBalita.toIntOrNull()
                        )
                        Toast.makeText(context, "Data KIA berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                )
            }

            Spacer(Modifier.height(24.dp))

        }

    }
}