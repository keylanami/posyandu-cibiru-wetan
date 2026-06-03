package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.data.model.DummyDetailWarga
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.dialog.SearchWargaDialog
import com.desacibiruwetan.posyandu.ui.components.input.AnimatedPillToggle
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.utils.DateVisualTransformation

@Composable
fun UpdateBalitaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<DummyDetailWarga?>(null) }

    var namaBalita by remember { mutableStateOf("") }
    var isAsiEksklusif by remember { mutableStateOf(true) }
    var tanggalMulaiAsi by remember { mutableStateOf("") }
    var tanggalSelesaiAsi by remember { mutableStateOf("") }

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                selectedWarga = warga
                namaBalita = warga.name
            }
        )
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Update Data Balita",
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            AppNavBar(
                selectedIndex = 1,
                onItemSelected = onNavItemSelected
            )
        },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Box(modifier = Modifier.clickable { showDialog = true }) {
                if (selectedWarga != null) {
                    UpdateHeaderCard(
                        title = "Update untuk",
                        name = selectedWarga!!.name,
                        icon = Icons.Default.ChildCare
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))
                        AppTextField(
                            label = "Nama Balita",
                            value = namaBalita,
                            onValueChange = { namaBalita = it },
                            placeholder = "Masukkan nama balita"
                        )
                    }
                } else {
                    UpdateHeaderCard(
                        title = "Pilih Balita",
                        name = "Ketuk untuk mencari data",
                        icon = Icons.Default.Search
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            FormSectionCard(title = null) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CircularIconBox(icon = Icons.Default.Face)
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "ASI Eksklusif",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF272727)
                        )
                    }

                    AnimatedPillToggle(
                        isYes = isAsiEksklusif,
                        onToggle = { isAsiEksklusif = it }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                AppTextField(
                    label = "Tanggal Mulai ASI",
                    value = tanggalMulaiAsi,
                    placeholder = "dd/mm/yyyy",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = DateVisualTransformation(),
                    onValueChange = {
                        if (it.length <= 8 && it.all { char -> char.isDigit() }) tanggalMulaiAsi =
                            it
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))

                AppTextField(
                    label = "Tanggal Selesai ASI",
                    value = tanggalSelesaiAsi,
                    placeholder = "dd/mm/yyyy",
                    keyboardType = KeyboardType.Number,
                    visualTransformation = DateVisualTransformation(),
                    onValueChange = {
                        if (it.length <= 8 && it.all { char -> char.isDigit() }) tanggalSelesaiAsi =
                            it
                    }
                )

                Text(
                    text = "*Kosongkan jika masih dalam masa ASI",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    fontSize = 12.sp,
                    color = Color(0xFFC9C9C9),
                    modifier = Modifier.padding(top = 4.dp, bottom = 24.dp)
                )

                PrimaryButton(
                    text = "Update Data Balita",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ }
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun CircularIconBox(icon: ImageVector) {
    Box(
        modifier = Modifier
            .size(59.dp)
            .clip(CircleShape)
            .background(Color(0xFFC7FFEC)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryGreen,
            modifier = Modifier.size(24.dp)
        )
    }
}