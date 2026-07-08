package com.desacibiruwetan.posyandu.ui.screen.warga

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.BgMint

@Composable
fun AdministrasiRtScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    val context = LocalContext.current

    var bulan by remember { mutableStateOf("") }
    var tahun by remember { mutableStateOf("") }


    var anggotaPkk by remember { mutableStateOf("") }
    var dataWisma by remember { mutableStateOf("") }
    var bankSampah by remember { mutableStateOf("") }
    var kegiatanPosyandu by remember { mutableStateOf("") }
    var kunjunganRumah by remember { mutableStateOf("") }
    var pertemuanRutin by remember { mutableStateOf("") }

    Scaffold(
        topBar = { AppTopBar(title = "Administrasi RT", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .responsiveScreenPadding()
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))


            FormSectionCard(title = "Status & Catatan") {
                Text(
                    text = "Pilih Periode Pelaporan",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 12.dp)
                )

                ResponsiveTwoColumn(
                    first = { fieldModifier ->
                        AppTextField(
                            label = "Bulan",
                            value = bulan,
                            placeholder = "mm",
                            keyboardType = KeyboardType.Number,
                            modifier = fieldModifier,
                            onValueChange = { if (it.length <= 2 && it.all { char -> char.isDigit() }) bulan = it }
                        )
                    },
                    second = { fieldModifier ->
                        AppTextField(
                            label = "Tahun",
                            value = tahun,
                            placeholder = "yyyy",
                            keyboardType = KeyboardType.Number,
                            modifier = fieldModifier,
                            onValueChange = { if (it.length <= 4 && it.all { char -> char.isDigit() }) tahun = it }
                        )
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))




            FormSectionCard(title = "Status & Catatan") {
                ResponsiveTwoColumn(
                    first = { fieldModifier ->
                    AppTextField(
                        label = "Anggota PKK",
                        value = anggotaPkk,
                        placeholder = "Jumlah Anggota",
                        keyboardType = KeyboardType.Number,
                        modifier = fieldModifier,
                        onValueChange = { if (it.all { char -> char.isDigit() }) anggotaPkk = it }
                    )
                    },
                    second = { fieldModifier ->
                    AppTextField(
                        label = "Data Wisma",
                        value = dataWisma,
                        placeholder = "Jumlah Wisma",
                        keyboardType = KeyboardType.Number,
                        modifier = fieldModifier,
                        onValueChange = { if (it.all { char -> char.isDigit() }) dataWisma = it }
                    )
                    }
                )



                ResponsiveTwoColumn(
                    first = { fieldModifier ->
                    AppTextField(
                        label = "Bank Sampah",
                        value = bankSampah,
                        placeholder = "Jumlah Bank",
                        keyboardType = KeyboardType.Number,
                        modifier = fieldModifier,
                        onValueChange = { if (it.all { char -> char.isDigit() }) bankSampah = it }
                    )
                    },
                    second = { fieldModifier ->
                    AppTextField(
                        label = "Kegiatan Posyandu",
                        value = kegiatanPosyandu,
                        placeholder = "Jumlah Kegiatan",
                        keyboardType = KeyboardType.Number,
                        modifier = fieldModifier,
                        onValueChange = { if (it.all { char -> char.isDigit() }) kegiatanPosyandu = it }
                    )
                    }
                )



                ResponsiveTwoColumn(
                    first = { fieldModifier ->
                    AppTextField(
                        label = "Kunjungan Rumah",
                        value = kunjunganRumah,
                        placeholder = "Jumlah Kunjungan",
                        keyboardType = KeyboardType.Number,
                        modifier = fieldModifier,
                        onValueChange = { if (it.all { char -> char.isDigit() }) kunjunganRumah = it }
                    )
                    },
                    second = { fieldModifier ->
                    AppTextField(
                        label = "Pertemuan Rutin",
                        value = pertemuanRutin,
                        placeholder = "Jumlah Pertemuan",
                        keyboardType = KeyboardType.Number,
                        modifier = fieldModifier,
                        onValueChange = { if (it.all { char -> char.isDigit() }) pertemuanRutin = it }
                    )
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Administrasi RT",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        Toast.makeText(context, "Data administrasi tersimpan sebagai draf", Toast.LENGTH_SHORT).show()
                    }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
