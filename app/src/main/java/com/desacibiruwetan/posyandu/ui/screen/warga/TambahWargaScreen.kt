package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppDateField
import com.desacibiruwetan.posyandu.ui.components.input.AppDropdownField
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.utils.calculateAgeInfo
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel

@Composable
fun TambahWargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    keluargaViewModel: KeluargaViewmodel
) {
    val context = LocalContext.current
    val sharedPreferences = remember { SessionManager.getPreferences(context) }
    val token = SessionManager.getAuthorizationHeader(context)
    val userRt = sharedPreferences.getString("USER_RT", "04") ?: "04"
    val userRw = sharedPreferences.getString("USER_RW", "02") ?: "02"

    val listKeluargaOptions by keluargaViewModel.keluargaOptions.collectAsState()
    var selectedKeluargaDisplay by remember { mutableStateOf("") }
    var selectedKeluargaId by remember { mutableStateOf<Int?>(null) }

    var namaLengkap by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("Laki-laki") }
    var nik by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }

    var statusKeluarga by remember { mutableStateOf("") }
    var statusSipil by remember { mutableStateOf("") }
    var pendidikan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var noBpjs by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    val statusKeluargaOptions = listOf(
        "Kepala Keluarga", "Suami", "Istri", "Anak", "Menantu",
        "Cucu", "Orang Tua", "Mertua", "Famili Lain", "Pembantu", "Lainnya"
    )
    val statusSipilOptions =
        listOf("Belum Kawin", "Kawin Tercatat", "Kawin Belum Tercatat", "Cerai Hidup", "Cerai Mati")
    val pendidikanOptions =
        listOf("Tidak/Belum Sekolah", "SD", "SMP", "SMA/SMK", "Diploma", "Sarjana", "Pascasarjana")


    LaunchedEffect(Unit) {
        if (token.isNotEmpty()) {
            keluargaViewModel.fetchKeluargaOptions(token)
        }
    }

    val dropdownKeluargaStrings = listKeluargaOptions.map {
        "${it.noKk} - ${it.kepalaKeluarga ?: "Tanpa Kepala"}"
    }

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
                "Data Identitas",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF292929)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(18.dp))
                    .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        AppTextField(
                            label = "RT",
                            value = userRt,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                        AppTextField(
                            label = "RW",
                            value = userRw,
                            onValueChange = {},
                            readOnly = true,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    AppDropdownField(
                        label = "Pilih Keluarga",
                        value = selectedKeluargaDisplay,
                        options = dropdownKeluargaStrings,
                        onValueChange = { selectedStr ->
                            selectedKeluargaDisplay = selectedStr
                            val matched =
                                listKeluargaOptions.find { "${it.noKk} - ${it.kepalaKeluarga ?: "Tanpa Kepala"}" == selectedStr }
                            selectedKeluargaId = matched?.id
                        }
                    )

                    AppTextField(
                        label = "Nama Lengkap",
                        value = namaLengkap,
                        placeholder = "Masukkan Nama Lengkap",
                        onValueChange = { if (it.length <= 100) namaLengkap = it })

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)) {
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
                        label = "NIK",
                        value = nik,
                        placeholder = "Masukkan 16 digit NIK",
                        keyboardType = KeyboardType.Number,
                        onValueChange = {
                            if (it.length <= 16 && it.all { char -> char.isDigit() }) nik = it
                        })

                    AppDateField(
                        label = "Tanggal Lahir",
                        value = tanggalLahir,
                        onValueChange = { tanggalLahir = it }
                    )

                    AppDropdownField(
                        label = "Status dalam Keluarga",
                        value = statusKeluarga,
                        options = statusKeluargaOptions,
                        onValueChange = { statusKeluarga = it })
                    AppDropdownField(
                        label = "Status Sipil",
                        value = statusSipil,
                        options = statusSipilOptions,
                        onValueChange = { statusSipil = it })
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                "Data Sosial",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color(0xFF292929)
            )
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(18.dp))
                    .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
                    .padding(24.dp)
            ) {
                Column {
                    AppDropdownField(
                        label = "Pendidikan Terakhir",
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
                        onValueChange = { if (it.all { char -> char.isDigit() }) noBpjs = it })
                    AppTextField(
                        label = "Keterangan Tambahan",
                        value = keterangan,
                        placeholder = "Tambah catatan jika diperlukan...",
                        singleLine = false,
                        onValueChange = { keterangan = it })
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Simpan Warga Baru",
                icon = Icons.Default.AddCircleOutline,
                onClick = {
                    val finalKeluargaId = selectedKeluargaId ?: run {
                        Toast.makeText(
                            context,
                            "Harap Pilih Keluarga dari Dropdown!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@PrimaryButton
                    }

                    if (nik.length < 16) {
                        Toast.makeText(context, "NIK harus 16 digit!", Toast.LENGTH_SHORT).show()
                        return@PrimaryButton
                    }
                    if (namaLengkap.isBlank()) {
                        Toast.makeText(context, "Nama lengkap wajib diisi!", Toast.LENGTH_SHORT).show()
                        return@PrimaryButton
                    }
                    if (tanggalLahir.isBlank()) {
                        Toast.makeText(
                            context,
                            "Pilih tanggal lahir!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@PrimaryButton
                    }
                    if (statusKeluarga.isBlank() || statusSipil.isBlank()) {
                        Toast.makeText(context, "Status keluarga dan status sipil wajib dipilih!", Toast.LENGTH_SHORT).show()
                        return@PrimaryButton
                    }
                    val (calculatedUsia, calculatedKategori) = calculateAgeInfo(tanggalLahir)

                    anggotaViewModel.tambahAnggota(
                        token = token,
                        keluargaId = finalKeluargaId,
                        nik = nik,
                        nama = namaLengkap,
                        tanggalLahir = tanggalLahir,
                        jenisKelamin = jenisKelamin,
                        pendidikanTerakhir = pendidikan,
                        pekerjaan = pekerjaan,
                        noBpjs = noBpjs,
                        statusKeluarga = statusKeluarga,
                        statusSipil = statusSipil,
                        statusWarga = "aktif",
                        keterangan = keterangan,
                        usia = calculatedUsia,
                        kategoriUsia = calculatedKategori
                    )
                    Toast.makeText(context, "Anggota berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    onBackClick()
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
