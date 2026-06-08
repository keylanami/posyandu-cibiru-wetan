package com.desacibiruwetan.posyandu.ui.screen.warga

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Female
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppRadioButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.RumahViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RumahKeluargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    rumahViewModel: RumahViewmodel,
    keluargaViewModel: KeluargaViewmodel
) {
    val context = LocalContext.current

    val sharedPreferences = context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE)
    val rawToken = sharedPreferences.getString("TOKEN", "") ?: ""
    val realToken = if (rawToken.isNotEmpty()) "Bearer $rawToken" else ""

    var namaWarga by remember { mutableStateOf("") }
    var noRumah by remember { mutableStateOf("") }
    var alamat by remember { mutableStateOf("") }

    val listKeluarga by keluargaViewModel.listKeluargaLocal.collectAsState()

    val rt = sharedPreferences.getString("USER_RT", "00") ?: "00"
    val rw = sharedPreferences.getString("USER_RW", "00") ?: "00"

    var noKk by remember { mutableStateOf("") }
    var expandedKK by remember { mutableStateOf(false) }

    var isNgontrak by remember { mutableStateOf(false) }
    var isGakin by remember { mutableStateOf(false) }


    LaunchedEffect(Unit) {
        rumahViewModel.syncDataRumah(realToken)
        keluargaViewModel.syncDataKeluarga(realToken)
    }

    Scaffold(
        topBar = { AppTopBar(title = "Rumah Keluarga", onBackClick = onBackClick) },
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
            Spacer(modifier = Modifier.height(24.dp))

            UpdateHeaderCard(
                title = "Update untuk Warga",
                name = "Form Pendataan Rumah & Keluarga",
                icon = Icons.Default.Female
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                AppTextField(
                    label = "Nama Pemilik/Perwakilan",
                    value = namaWarga,
                    onValueChange = { namaWarga = it })
            }

            Spacer(modifier = Modifier.height(24.dp))

            FormSectionCard(title = "Data Rumah") {
                AppTextField(label = "No Rumah", value = noRumah, onValueChange = { noRumah = it })
                AppTextField(
                    label = "Alamat Lengkap",
                    value = alamat,
                    singleLine = false,
                    onValueChange = { alamat = it })
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    AppTextField(
                        label = "RT",
                        value = rt,
                        readOnly = true,
                        onValueChange = {},
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                    AppTextField(
                        label = "RW",
                        value = rw,
                        readOnly = true,
                        onValueChange = {},
                        keyboardType = KeyboardType.Number,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            FormSectionCard(title = "Data Keluarga") {

                Text(
                    text = "Pilih Nomor Kartu Keluarga (KK)",
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )


                ExposedDropdownMenuBox(
                    expanded = expandedKK,
                    onExpandedChange = { expandedKK = !expandedKK },
                    modifier = Modifier.fillMaxWidth()
                ) {

                    OutlinedTextField(
                        value = noKk,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("No KK") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKK)
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = BgMint,
                            unfocusedContainerColor = BgMint
                        ),
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expandedKK,
                        onDismissRequest = { expandedKK = false }
                    ) {
                        if (listKeluarga.isEmpty()) {
                            DropdownMenuItem(
                                text = { Text("Data Kosong (Lakukan Sinkronisasi)") },
                                onClick = { expandedKK = false }
                            )
                        } else {
                            listKeluarga.forEach { keluarga ->
                                DropdownMenuItem(
                                    text = { Text(keluarga.noKK) },
                                    onClick = {
                                        noKk = keluarga.noKK
                                        expandedKK = false
                                    }
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text("Status Tempat Tinggal", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        AppRadioButton("Milik Sendiri", !isNgontrak) { isNgontrak = false }
                        AppRadioButton("Ngontrak", isNgontrak) { isNgontrak = true }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Text(
                        "Kategori Gakin (Keluarga Miskin)?",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                        AppRadioButton("Tidak", !isGakin) { isGakin = false }
                        AppRadioButton("Ya", isGakin) { isGakin = true }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Simpan Data",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        if (realToken.isEmpty()) {
                            Toast.makeText(
                                context,
                                "Sesi habis, silakan login ulang.",
                                Toast.LENGTH_SHORT
                            ).show()
                            return@PrimaryButton
                        }

                        val rtInt = rt.toIntOrNull() ?: 0
                        val noRumahInt = noRumah.toIntOrNull() ?: 0


                        rumahViewModel.tambahRumah(
                            realToken,
                            alamat,
                            noRumahInt,
                            rtInt,
                            onSuccess = { newRumahId ->
                                keluargaViewModel.tambahKeluarga(
                                    realToken,
                                    newRumahId,
                                    noKk,
                                    isNgontrak,
                                    isGakin
                                ) { newKeluargaId ->
                                    Toast.makeText(
                                        context,
                                        "Data Rumah & Keluarga berhasil disimpan!",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    onBackClick()
                                }
                            }
                        )
                    }
                )
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}