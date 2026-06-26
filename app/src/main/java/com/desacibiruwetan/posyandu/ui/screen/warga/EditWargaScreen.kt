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
import com.desacibiruwetan.posyandu.ui.components.input.AppDateField
import com.desacibiruwetan.posyandu.ui.components.input.AppDropdownField
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.utils.calculateAgeInfo
import com.desacibiruwetan.posyandu.utils.normalizeDateForForm
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

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
    var tempatLahir by remember { mutableStateOf("") }
    var tanggalLahir by remember { mutableStateOf("") }
    var golonganDarah by remember { mutableStateOf("") }
    var suku by remember { mutableStateOf("") }
    var kewarganegaraan by remember { mutableStateOf("WNI") }

    var statusKeluarga by remember { mutableStateOf("") }
    var statusSipil by remember { mutableStateOf("") }
    var pendidikan by remember { mutableStateOf("") }
    var pekerjaan by remember { mutableStateOf("") }
    var noBpjs by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }
    var statusWarga by remember { mutableStateOf("aktif") }
    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    val statusKeluargaOptions = listOf(
        "Kepala Keluarga", "Suami", "Istri", "Anak", "Menantu",
        "Cucu", "Orang Tua", "Mertua", "Famili Lain", "Pembantu", "Lainnya"
    )
    val statusSipilOptions =
        listOf("Belum Kawin", "Kawin Tercatat", "Kawin Belum Tercatat", "Cerai Hidup", "Cerai Mati")
    val pendidikanOptions =
        listOf("Tidak/Belum Sekolah", "SD", "SMP", "SMA/SMK", "Diploma", "Sarjana", "Pascasarjana")
    val golonganDarahOptions = listOf("A", "B", "AB", "O", "Tidak Tahu")

    LaunchedEffect(anggotaLokal) {
        anggotaLokal?.let {
            namaLengkap = it.nama
            jenisKelamin = it.jenisKelamin
            nik = it.nik
            tempatLahir = it.tempatLahir.orEmpty()

            tanggalLahir = normalizeDateForForm(it.tanggalLahir)
            golonganDarah = it.golonganDarah.orEmpty()
            suku = it.suku.orEmpty()
            kewarganegaraan = it.kewarganegaraan ?: "WNI"

            statusKeluarga = it.statusKeluarga
            statusSipil = it.statusSipil
            pendidikan = it.pendidikanTerakhir ?: "Tidak/Belum Sekolah"
            pekerjaan = it.pekerjaan ?: "Tidak Bekerja"
            noBpjs = it.noBpjs ?: ""
            keterangan = it.keterangan ?: ""
            statusWarga = it.statusWarga ?: "aktif"
        }
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
                .responsiveScreenPadding()
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
                        label = "Nama Lengkap",
                        value = namaLengkap,
                        error = fieldErrors["nama"],
                        maxLength = 100,
                        onValueChange = {
                            namaLengkap = it
                            fieldErrors = fieldErrors - "nama"
                        })

                    Column(modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)) {
                        Text(text = "Jenis Kelamin", style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        ResponsiveTwoColumn(
                            first = { fieldModifier ->
                            EditRadioButtonItem(
                                "Laki-laki",
                                jenisKelamin == "Laki-laki",
                                modifier = fieldModifier
                            ) { jenisKelamin = "Laki-laki" }
                            },
                            second = { fieldModifier ->
                            EditRadioButtonItem(
                                "Perempuan",
                                jenisKelamin == "Perempuan",
                                modifier = fieldModifier
                            ) { jenisKelamin = "Perempuan" }
                            }
                        )
                    }

                    AppTextField(
                        label = "NIK",
                        value = nik,
                        keyboardType = KeyboardType.Number,
                        maxLength = 16,
                        counterLabel = "NIK",
                        error = fieldErrors["nik"],
                        onValueChange = {
                            nik = it.filter(Char::isDigit).take(16)
                            fieldErrors = fieldErrors - "nik"
                        })

                    AppTextField(
                        label = "Tempat Lahir",
                        value = tempatLahir,
                        onValueChange = { tempatLahir = it })

                    AppDateField(
                        label = "Tanggal Lahir",
                        value = tanggalLahir,
                        error = fieldErrors["tanggal_lahir"],
                        onValueChange = {
                            tanggalLahir = it
                            fieldErrors = fieldErrors - "tanggal_lahir"
                        }
                    )

                    AppDropdownField(
                        label = "Golongan Darah",
                        value = golonganDarah,
                        options = golonganDarahOptions,
                        onValueChange = { golonganDarah = it })

                    AppTextField(
                        label = "Suku",
                        value = suku,
                        onValueChange = { suku = it })

                    AppTextField(
                        label = "Kewarganegaraan",
                        value = kewarganegaraan,
                        onValueChange = { kewarganegaraan = it })

                    AppDropdownField(
                        label = "Status dalam Keluarga",
                        value = statusKeluarga,
                        options = statusKeluargaOptions,
                        error = fieldErrors["status_keluarga"],
                        onValueChange = {
                            statusKeluarga = it
                            fieldErrors = fieldErrors - "status_keluarga"
                        })
                    AppDropdownField(
                        label = "Status Sipil",
                        value = statusSipil,
                        options = statusSipilOptions,
                        error = fieldErrors["status_sipil"],
                        onValueChange = {
                            statusSipil = it
                            fieldErrors = fieldErrors - "status_sipil"
                        })
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
                        maxLength = 16,
                        counterLabel = "NO BPJS",
                        error = fieldErrors["no_bpjs"],
                        onValueChange = {
                            noBpjs = it.filter(Char::isDigit).take(16)
                            fieldErrors = fieldErrors - "no_bpjs"
                        })
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

                    val errors = buildMap {
                        if (namaLengkap.isBlank()) put("nama", "Nama lengkap wajib diisi.")
                        if (nik.isBlank()) put("nik", "NIK wajib diisi.")
                        else if (nik.length != 16) put("nik", "NIK harus 16 digit.")
                        if (tanggalLahir.isBlank()) put("tanggal_lahir", "Tanggal lahir wajib dipilih.")
                        if (statusKeluarga.isBlank()) put("status_keluarga", "Status keluarga wajib dipilih.")
                        if (statusSipil.isBlank()) put("status_sipil", "Status sipil wajib dipilih.")
                        if (noBpjs.isNotBlank() && noBpjs.length != 16) put("no_bpjs", "No BPJS harus 16 digit atau dikosongkan.")
                    }
                    if (errors.isNotEmpty()) {
                        fieldErrors = errors
                        return@PrimaryButton
                    }
                    val (calculatedUsia, calculatedKategori) = calculateAgeInfo(tanggalLahir)

                    anggotaViewModel.updateAnggota(
                        token = token,
                        anggotaLokal = anggotaLokal,
                        nikBaru = nik,
                        namaBaru = namaLengkap,
                        tanggalLahirBaru = tanggalLahir,
                        jenisKelaminBaru = jenisKelamin,
                        pekerjaanBaru = pekerjaan,
                        pendidikanTerakhirBaru = pendidikan,
                        noBpjsBaru = noBpjs,
                        keteranganBaru = keterangan,
                        statusKeluargaBaru = statusKeluarga,
                        statusSipilBaru = statusSipil,
                        statusWargaBaru = statusWarga,
                        usiaBaru = calculatedUsia,
                        kategoriUsiaBaru = calculatedKategori,
                        tempatLahirBaru = tempatLahir.ifBlank { null },
                        golonganDarahBaru = golonganDarah.ifBlank { null },
                        sukuBaru = suku.ifBlank { null },
                        kewarganegaraanBaru = kewarganegaraan.ifBlank { "WNI" }
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
private fun EditRadioButtonItem(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.clickable { onClick() }) {
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
