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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppDropdownField
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.utils.DateVisualTransformation

@Composable
fun TambahWargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    // --- State Data Identitas ---
    val rt = "04"
    val rw = "02"
    var noRumah by remember { mutableStateOf("") }
    var noKeluarga by remember { mutableStateOf("") }
    var noKk by remember { mutableStateOf("") }
    var namaLengkap by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }
    var statusKeluarga by remember { mutableStateOf("") }

    // --- State Data Sosial ---
    var pendidikan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var noBpjs by remember { mutableStateOf("") }
    var kategoriGakin by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    // Option Lists
    val statusKeluargaOptions = listOf("Kepala Keluarga", "Istri", "Anak")
    val pendidikanOptions = listOf("Tidak Sekolah", "SD", "SMP", "SMA", "Diploma", "S1", "S2", "S3")
    val gakinOptions = listOf("Non GAKIN (Mampu)", "GAKIN (Keluarga Miskin)")

    Scaffold(
        topBar = { AppTopBar(title = "Tambah Warga Baru", onBackClick = onBackClick) },
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

            Text(
                text = "Data Identitas",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF292929)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, spotColor = Color(0x40DFDFDF), shape = RoundedCornerShape(15.dp))
                    .background(SurfaceWhite, RoundedCornerShape(15.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AppTextField(
                            label = "RT",
                            value = rt,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                        AppTextField(
                            label = "RW",
                            value = rw,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AppTextField(
                            label = "No Rumah",
                            value = noRumah,
                            placeholder = "00",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.weight(1f),
                            onValueChange = { if (it.all { char -> char.isDigit() }) noRumah = it }
                        )
                        AppTextField(
                            label = "No Keluarga",
                            value = noKeluarga,
                            placeholder = "00",
                            keyboardType = KeyboardType.Number,
                            modifier = Modifier.weight(1f),
                            onValueChange = {
                                if (it.all { char -> char.isDigit() }) noKeluarga = it
                            }
                        )
                    }

                    AppTextField(
                        label = "No Kartu Keluarga",
                        value = noKk,
                        placeholder = "Masukkan Nomor Kartu Keluarga",
                        keyboardType = KeyboardType.Number,
                        onValueChange = { if (it.all { char -> char.isDigit() }) noKk = it }
                    )

                    AppTextField(
                        label = "Nama Lengkap",
                        value = namaLengkap,
                        placeholder = "Masukkan Nama Lengkap",
                        onValueChange = { if (it.length <= 100) namaLengkap = it }
                    )


                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(text = "Jenis Kelamin", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            RadioButtonItem(
                                "Laki-laki",
                                jenisKelamin == "Laki-laki"
                            ) { jenisKelamin = "Laki-laki" }
                            RadioButtonItem(
                                "Perempuan",
                                jenisKelamin == "Perempuan"
                            ) { jenisKelamin = "Perempuan" }
                        }
                    }

                    AppTextField(
                        label = "Nomor Induk Keluarga",
                        value = nik,
                        placeholder = "Masukkan 16 digit NIK",
                        keyboardType = KeyboardType.Number,
                        onValueChange = {
                            if (it.length <= 16 && it.all { char -> char.isDigit() }) nik = it
                        }
                    )

                    AppTextField(
                        label = "Tanggal Lahir",
                        value = tanggalLahir,
                        placeholder = "dd/mm/yyyy",
                        keyboardType = KeyboardType.Number,
                        visualTransformation = DateVisualTransformation(),
                        onValueChange = {
                            if (it.length <= 8 && it.all { char -> char.isDigit() }) tanggalLahir =
                                it
                        }
                    )

                    AppDropdownField(
                        label = "Status dalam Keluarga",
                        value = statusKeluarga,
                        options = statusKeluargaOptions,
                        onValueChange = { statusKeluarga = it })
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            Text(
                text = "Data Sosial",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF292929)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, spotColor = Color(0x40DFDFDF), shape = RoundedCornerShape(15.dp))
                    .background(SurfaceWhite, RoundedCornerShape(15.dp))
                    .padding(24.dp)
            ) {
                Column {
                    AppDropdownField(
                        label = "Pendidikan",
                        value = pendidikan,
                        options = pendidikanOptions,
                        onValueChange = { pendidikan = it })

                    AppTextField(
                        label = "Pekerjaan",
                        value = pekerjaan,
                        placeholder = "Contoh: Buruh, Pedagang, PNS",
                        onValueChange = { pekerjaan = it })

                    AppTextField(
                        label = "No BPJS",
                        value = noBpjs,
                        placeholder = "Masukkan nomor BPJS",
                        keyboardType = KeyboardType.Number,
                        onValueChange = { if (it.all { char -> char.isDigit() }) noBpjs = it }
                    )

                    AppDropdownField(
                        label = "Kategori GAKIN",
                        value = kategoriGakin,
                        options = gakinOptions,
                        onValueChange = { kategoriGakin = it })

                    AppTextField(
                        label = "Keterangan Tambahan",
                        value = keterangan,
                        placeholder = "Tambah catatan jika diperlukan...",
                        singleLine = false,
                        onValueChange = { keterangan = it }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))


            PrimaryButton(
                text = "Simpan Warga Baru",
                icon = Icons.Default.AddCircleOutline,
                onClick = {
                    // TODO: Validasi form dan proses simpan data ke Backend/Room Database
                    println("Simpan Data: $namaLengkap, $nik, $tanggalLahir")
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun RadioButtonItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.clickable { onClick() }) {
        Icon(
            imageVector = if (isSelected) Icons.Default.RadioButtonChecked else Icons.Default.RadioButtonUnchecked,
            contentDescription = null,
            tint = if (isSelected) PrimaryGreen else Color(0xFFC9C9C9),
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color(0xFF272727)
        )
    }
}