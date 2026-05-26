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
                    value = "",
                    placeholder = "Masukkan jumlah keluarga dengan bak sampah",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { /* TODO */ }
                )

                AppTextField(
                    label = "Anggota Bank Sampah",
                    value = "",
                    placeholder = "Masukkan jumlah anggota bank sampah",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { /* TODO */ }
                )

                AppTextField(
                    label = "Keluarga Pakai Spal",
                    value = "",
                    placeholder = "Masukkan jumlah keluarga dengan pal",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { /* TODO */ }

                )

                AppTextField(
                    label = "Kasus Banjir",
                    value = "",
                    placeholder = "Masukkan jumlah kasus banjir",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { /* TODO */ }
                )

                AppTextField(
                    label = "Bak Sampah Desa",
                    value = "",
                    placeholder = "Masukkan jumlah bak sampah desa",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { /* TODO */ }
                )

                AppTextField(
                    label = "Rumah Ventilasi Baik",
                    value = "",
                    placeholder = "Masukkan jumlah rumah ventilasi baik",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { /* TODO */ }
                )

                AppTextField(
                    label = "KLB",
                    value = "",
                    placeholder = "Masukkan jumlah KLB",
                    keyboardType = KeyboardType.Number,
                    onValueChange = { /* TODO */ }
                )

                Spacer(Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data Peduli Lingkungan",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }
        }
    }
}