package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.desacibiruwetan.posyandu.ui.components.input.AppDropdownField
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.utils.DateVisualTransformation
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import java.util.Calendar

private fun isDateValidForEdit(input: String): Boolean {
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

// FIX: Fungsi Penerjemah Tanggal (Teks -> Angka 8 Digit)
private fun convertServerDateToRaw(serverDate: String): String {
    // 1. Jika backend mengembalikan format "25-08-2024"
    if (serverDate.contains("-") && serverDate.length == 10) {
        return serverDate.replace("-", "")
    }

    // 2. Jika backend mengembalikan format teks bahasa Indonesia "25 Agustus 2024"
    val monthMap = mapOf(
        "januari" to "01", "februari" to "02", "maret" to "03", "april" to "04",
        "mei" to "05", "juni" to "06", "juli" to "07", "agustus" to "08",
        "september" to "09", "oktober" to "10", "november" to "11", "desember" to "12"
    )

    val parts = serverDate.split(" ")
    if (parts.size == 3) {
        val day = parts[0].padStart(2, '0') // Memastikan "8" jadi "08"
        val monthText = parts[1].lowercase()
        val month = monthMap[monthText] ?: "01"
        val year = parts[2]

        if (year.length == 4 && day.all { it.isDigit() }) {
            return "$day$month$year" // Menghasilkan "25082024"
        }
    }

    // 3. Fallback darurat (buang semua karakter huruf, ambil angkanya saja maksimal 8 digit)
    return serverDate.filter { it.isDigit() }.take(8)
}

@Composable
fun EditWargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    nikWarga: String?
) {
    val context = LocalContext.current
    val token = SessionManager.getAuthorizationHeader(context)

    val listWarga by anggotaViewModel.listAnggotaLocal.collectAsState()
    val anggotaLokal = listWarga.find { it.nik == nikWarga }

    // --- State Data ---
    var namaLengkap by remember { mutableStateOf("") }
    var jenisKelamin by remember { mutableStateOf("Laki-laki") }
    var nik by remember { mutableStateOf("") }
    var tanggalLahirRaw by remember { mutableStateOf("") }

    var statusKeluarga by remember { mutableStateOf("") }
    var statusSipil by remember { mutableStateOf("") }
    var pendidikan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var noBpjs by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }
    var statusWarga by remember { mutableStateOf("aktif") }

    val statusKeluargaOptions = listOf(
        "Kepala Keluarga", "Suami", "Istri", "Anak", "Menantu",
        "Cucu", "Orang Tua", "Mertua", "Famili Lain", "Pembantu", "Lainnya"
    )
    val statusSipilOptions =
        listOf("Belum Kawin", "Kawin Tercatat", "Kawin Belum Tercatat", "Cerai Hidup", "Cerai Mati")
    val pendidikanOptions =
        listOf("Tidak/Belum Sekolah", "SD", "SMP", "SMA/SMK", "Diploma", "Sarjana", "Pascasarjana")

    LaunchedEffect(anggotaLokal) {
        anggotaLokal?.let {
            namaLengkap = it.nama
            jenisKelamin = it.jenisKelamin
            nik = it.nik

            tanggalLahirRaw = convertServerDateToRaw(it.tanggalLahir)

            statusKeluarga = it.statusKeluarga
            statusSipil = it.statusSipil
            pendidikan = it.pendidikanTerakhir ?: "Tidak/Belum Sekolah"
            pekerjaan = it.pekerjaan ?: "Tidak Bekerja"
            noBpjs = it.noBpjs ?: ""
            keterangan = it.keterangan ?: ""
            statusWarga = it.statusWarga ?: "aktif"
        }
    }

    fun prosesDataLahir(raw: String): Triple<String, String, String> {
        if (raw.length != 8) return Triple("", "", "")
        val day = raw.substring(0, 2)
        val month = raw.substring(2, 4)
        val year = raw.substring(4, 8)

        val apiDate = "$day-$month-$year"

        val birthDate =
            Calendar.getInstance().apply { set(year.toInt(), month.toInt() - 1, day.toInt()) }
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
        topBar = { AppTopBar(title = "Edit Data Warga", onBackClick = onBackClick) },
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
                "Perbarui Identitas",
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
                    AppTextField(
                        label = "Keluarga ID Terdaftar",
                        value = anggotaLokal?.keluargaId?.toString() ?: "",
                        onValueChange = {},
                        readOnly = true
                    )

                    AppTextField(
                        label = "Nama Lengkap",
                        value = namaLengkap,
                        onValueChange = { if (it.length <= 100) namaLengkap = it })

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)) {
                        Text(text = "Jenis Kelamin", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            EditRadioButtonItem(
                                "Laki-laki",
                                jenisKelamin == "Laki-laki"
                            ) { jenisKelamin = "Laki-laki" }
                            EditRadioButtonItem(
                                "Perempuan",
                                jenisKelamin == "Perempuan"
                            ) { jenisKelamin = "Perempuan" }
                        }
                    }

                    AppTextField(
                        label = "Nomor Induk Keluarga",
                        value = nik,
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
                            if (isDateValidForEdit(newValue)) tanggalLahirRaw = newValue
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
                "Perbarui Data Sosial",
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
                        onValueChange = { pekerjaan = it })
                    AppTextField(
                        label = "No BPJS",
                        value = noBpjs,
                        keyboardType = KeyboardType.Number,
                        onValueChange = { if (it.all { char -> char.isDigit() }) noBpjs = it })
                    AppTextField(
                        label = "Keterangan Tambahan",
                        value = keterangan,
                        singleLine = false,
                        onValueChange = { keterangan = it })
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Simpan Perubahan",
                icon = Icons.Default.Edit,
                onClick = {
                    if (anggotaLokal == null) return@PrimaryButton

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
                        Toast.makeText(context, "Format tanggal tidak valid!", Toast.LENGTH_SHORT)
                            .show()
                        return@PrimaryButton
                    }

                    anggotaViewModel.updateAnggota(
                        token = token,
                        anggotaLokal = anggotaLokal,
                        nikBaru = nik,
                        namaBaru = namaLengkap,
                        tanggalLahirBaru = apiDate,
                        jenisKelaminBaru = jenisKelamin,
                        pekerjaanBaru = pekerjaan,
                        pendidikanTerakhirBaru = pendidikan,
                        noBpjsBaru = noBpjs,
                        keteranganBaru = keterangan,
                        statusKeluargaBaru = statusKeluarga,
                        statusSipilBaru = statusSipil,
                        statusWargaBaru = statusWarga,
                        usiaBaru = calculatedUsia,
                        kategoriUsiaBaru = calculatedKategori
                    )
                    Toast.makeText(context, "Perubahan berhasil disimpan!", Toast.LENGTH_SHORT)
                        .show()
                    onBackClick()
                }
            )

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
private fun EditRadioButtonItem(text: String, isSelected: Boolean, onClick: () -> Unit) {
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
        Text(text = text, fontFamily = Inter, fontSize = 12.sp, color = Color(0xFF272727))
    }
}
