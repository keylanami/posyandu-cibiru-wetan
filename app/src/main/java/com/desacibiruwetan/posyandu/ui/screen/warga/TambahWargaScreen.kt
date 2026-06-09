package com.desacibiruwetan.posyandu.ui.screen.warga

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel
import java.util.Calendar

fun isValidDateInput(input: String): Boolean {
    if (input.isEmpty()) return true
    if (!input.all { it.isDigit() }) return false
    if (input.length > 8) return false

    if (input.length >= 1 && input[0] > '3') return false
    if (input.length >= 2) {
        val day = input.substring(0, 2).toIntOrNull() ?: 0
        if (day !in 1..31) return false
    }

    if (input.length >= 3 && input[2] > '1') return false
    if (input.length >= 4) {
        val month = input.substring(2, 4).toIntOrNull() ?: 0
        if (month !in 1..12) return false

        val day = input.substring(0, 2).toIntOrNull() ?: 0
        if (month == 2 && day > 29) return false
        if ((month == 4 || month == 6 || month == 9 || month == 11) && day > 30) return false
    }
    return true
}

@Composable
fun TambahWargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    keluargaViewModel: KeluargaViewmodel
) {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE)
    val rawToken = sharedPreferences.getString("TOKEN", "") ?: ""
    val token = if (rawToken.isNotEmpty()) "Bearer $rawToken" else ""
    val userRt = sharedPreferences.getString("USER_RT", "04") ?: "04"
    val userRw = sharedPreferences.getString("USER_RW", "02") ?: "02"

    var noRumah by remember { mutableStateOf("") }

    val listKeluargaOptions by keluargaViewModel.keluargaOptions.collectAsState()
    var selectedKeluargaDisplay by remember { mutableStateOf("") }
    var selectedKeluargaId by remember { mutableStateOf<Int?>(null) }

    var noKk by remember { mutableStateOf("") }
    var namaLengkap by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("Laki-laki") }
    var nik by remember { mutableStateOf("") }
    var tanggalLahirRaw by remember { mutableStateOf("") }

    var statusKeluarga by remember { mutableStateOf("") }
    var statusSipil by remember { mutableStateOf("") }
    var pendidikan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var noBpjs by remember { mutableStateOf("") }
    var kategoriGakin by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    val statusKeluargaOptions = listOf(
        "Kepala Keluarga", "Suami", "Istri", "Anak", "Menantu",
        "Cucu", "Orang Tua", "Mertua", "Famili Lain", "Pembantu", "Lainnya"
    )
    val statusSipilOptions =
        listOf("Belum Kawin", "Kawin Tercatat", "Kawin Belum Tercatat", "Cerai Hidup", "Cerai Mati")
    val pendidikanOptions =
        listOf("Tidak/Belum Sekolah", "SD", "SMP", "SMA/SMK", "Diploma", "Sarjana", "Pascasarjana")
    val gakinOptions = listOf("Non GAKIN (Mampu)", "GAKIN (Keluarga Miskin)")


    LaunchedEffect(Unit) {
        if (token.isNotEmpty()) {
            keluargaViewModel.fetchKeluargaOptions(token)
        }
    }

    val dropdownKeluargaStrings = listKeluargaOptions.map {
        "${it.noKk} - ${it.kepalaKeluarga ?: "Tanpa Kepala"}"
    }

    fun prosesDataLahir(raw: String): Triple<String, String, String> {
        if (raw.length != 8) return Triple("", "", "")

        val day = raw.substring(0, 2)
        val month = raw.substring(2, 4)
        val year = raw.substring(4, 8)

        val apiDate = "$day-$month-$year"

        val birthDate = Calendar.getInstance().apply {
            set(year.toInt(), month.toInt() - 1, day.toInt())
        }
        val today = Calendar.getInstance()
        var age = today.get(Calendar.YEAR) - birthDate.get(Calendar.YEAR)
        if (today.get(Calendar.DAY_OF_YEAR) < birthDate.get(Calendar.DAY_OF_YEAR)) age--

        val kategori = when {
            age < 5 -> "Balita"
            age < 12 -> "Anak-anak"
            age < 18 -> "Remaja"
            age < 60 -> "Dewasa"
            else -> "Lansia"
        }

        return Triple(apiDate, age.toString(), kategori)
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
                    .shadow(16.dp, spotColor = Color(0x40DFDFDF), shape = RoundedCornerShape(15.dp))
                    .background(SurfaceWhite, RoundedCornerShape(15.dp))
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
                            noKk = matched?.noKk
                                ?: ""
                        }
                    )

                    AppTextField(
                        label = "No Kartu Keluarga",
                        value = noKk,
                        placeholder = "Pilih dari Dropdown Keluarga di atas",
                        keyboardType = KeyboardType.Number,
                        onValueChange = { if (it.all { char -> char.isDigit() }) noKk = it })
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
                        label = "Nomor Induk Keluarga",
                        value = nik,
                        placeholder = "Masukkan 16 digit NIK",
                        keyboardType = KeyboardType.Number,
                        onValueChange = {
                            if (it.length <= 16 && it.all { char -> char.isDigit() }) nik = it
                        })

                    AppTextField(
                        label = "Tanggal Lahir",
                        value = tanggalLahirRaw,
                        placeholder = "dd/mm/yyyy",
                        keyboardType = KeyboardType.Number,
                        visualTransformation = DateVisualTransformation(),
                        onValueChange = { newValue ->
                            if (isValidDateInput(newValue)) tanggalLahirRaw = newValue
                        }
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
                    .shadow(16.dp, spotColor = Color(0x40DFDFDF), shape = RoundedCornerShape(15.dp))
                    .background(SurfaceWhite, RoundedCornerShape(15.dp))
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
                    if (tanggalLahirRaw.length < 8) {
                        Toast.makeText(
                            context,
                            "Lengkapi Tanggal Lahir (8 digit)!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@PrimaryButton
                    }

                    val (apiDate, calculatedUsia, calculatedKategori) = prosesDataLahir(
                        tanggalLahirRaw
                    )

                    if (apiDate.isEmpty()) {
                        Toast.makeText(
                            context,
                            "Format tanggal lahir tidak sesuai kalender!",
                            Toast.LENGTH_SHORT
                        ).show()
                        return@PrimaryButton
                    }

                    anggotaViewModel.tambahAnggota(
                        token = token,
                        keluargaId = finalKeluargaId,
                        nik = nik,
                        nama = namaLengkap,
                        tanggalLahir = apiDate,
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