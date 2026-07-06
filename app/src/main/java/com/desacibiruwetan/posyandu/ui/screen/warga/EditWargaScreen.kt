package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EditWargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    nikWarga: String?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
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
    var jaminanKesehatan by remember { mutableStateOf(false) }
    var keterangan by remember { mutableStateOf("") }
    var statusWarga by remember { mutableStateOf("aktif") }
    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // Requesters for scrolling
    val namaRequester = remember { BringIntoViewRequester() }
    val nikRequester = remember { BringIntoViewRequester() }
    val tempatLahirRequester = remember { BringIntoViewRequester() }
    val tanggalLahirRequester = remember { BringIntoViewRequester() }
    val statusKeluargaRequester = remember { BringIntoViewRequester() }
    val statusSipilRequester = remember { BringIntoViewRequester() }

    val statusKeluargaOptions = listOf(
        "Kepala Keluarga", "Suami", "Istri", "Anak", "Menantu",
        "Cucu", "Orang Tua", "Mertua", "Famili Lain", "Pembantu", "Lainnya"
    )
    val statusSipilOptions =
        listOf("Belum Kawin", "Kawin Tercatat", "Kawin Belum Tercatat", "Cerai Hidup", "Cerai Mati")
    val pendidikanOptions = listOf(
        "Belum masuk TK/Kelompok Bermain",
        "Sedang D-1/sederajat",
        "Sedang D-2/sederajat",
        "Sedang D-3/sederajat",
        "Sedang S-1/sederajat",
        "Sedang S-2/sederajat",
        "Sedang S-3/sederajat",
        "Sedang SD/sederajat",
        "Sedang SLB B/sederajat",
        "Sedang SLTA/sederajat",
        "Sedang SLTP/Sederajat",
        "Sedang TK/Kelompok Bermain",
        "Tamat D-1/sederajat",
        "Tamat D-2/sederajat",
        "Tamat D-3/sederajat",
        "Tamat D-4/sederajat",
        "Tamat S-1/sederajat",
        "Tamat S-2/sederajat",
        "Tamat S-3/sederajat",
        "Tamat SD/sederajat",
        "Tamat SLB B/sederajat",
        "Tamat SLB C/sederajat",
        "Tamat SLTA/sederajat",
        "Tamat SLTP/sederajat",
        "Tidak pernah sekolah",
        "Tidak tamat SD/sederajat"
    )
    val golonganDarahOptions = listOf("A", "B", "AB", "O", "Tidak Tahu")
    val kewarganegaraanOptions = listOf("WNI", "WNA")
    val jaminanKesehatanOptions = listOf("Punya", "Tidak")

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
            pendidikan = it.pendidikanTerakhir ?: "Tidak pernah sekolah"
            pekerjaan = it.pekerjaan ?: "Tidak Bekerja"
            jaminanKesehatan = it.jaminanKesehatan
            keterangan = it.keterangan ?: ""
            statusWarga = it.statusWarga ?: "aktif"
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Edit Data Warga", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        containerColor = BgMint
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .responsiveScreenPadding()
        ) {
            item { Spacer(modifier = Modifier.height(24.dp)) }

            item {
                Text(
                    "Perbarui Identitas",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF292929)
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
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
                            modifier = Modifier.bringIntoViewRequester(namaRequester),
                            onValueChange = { input ->
                                val filtered = input.filter { ch ->
                                    ch.isLetter() ||
                                            ch.isWhitespace() ||
                                            ch == '-' ||
                                            ch == '\'' ||
                                            ch == '.'
                                }

                                namaLengkap = filtered
                                fieldErrors = fieldErrors - "nama"
                            })

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
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
                            modifier = Modifier.bringIntoViewRequester(nikRequester),
                            onValueChange = {
                                nik = it.filter(Char::isDigit).take(16)
                                fieldErrors = fieldErrors - "nik"
                            })

                        AppTextField(
                            label = "Tempat Lahir",
                            value = tempatLahir,
                            placeholder = "Contoh: Bandung",
                            modifier = Modifier.bringIntoViewRequester(tempatLahirRequester),
                            onValueChange = {
                                tempatLahir = it
                                fieldErrors = fieldErrors - "tempat_lahir"
                            },
                            capitalization = KeyboardCapitalization.Words,
                            error = fieldErrors["tempat_lahir"]
                        )

                        AppDateField(
                            label = "Tanggal Lahir",
                            value = tanggalLahir,
                            error = fieldErrors["tanggal_lahir"],
                            modifier = Modifier.bringIntoViewRequester(tanggalLahirRequester),
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

                        AppDropdownField(
                            label = "Kewarganegaraan",
                            value = kewarganegaraan,
                            placeholder = "WNI",
                            options = kewarganegaraanOptions,
                            onValueChange = {
                                kewarganegaraan = it
                                fieldErrors = fieldErrors - "kewarganegaraan"
                            })

                        AppDropdownField(
                            label = "Status dalam Keluarga",
                            value = statusKeluarga,
                            options = statusKeluargaOptions,
                            error = fieldErrors["status_keluarga"],
                            modifier = Modifier.bringIntoViewRequester(statusKeluargaRequester),
                            onValueChange = {
                                statusKeluarga = it
                                fieldErrors = fieldErrors - "status_keluarga"
                            })
                        AppDropdownField(
                            label = "Status Sipil",
                            value = statusSipil,
                            options = statusSipilOptions,
                            error = fieldErrors["status_sipil"],
                            modifier = Modifier.bringIntoViewRequester(statusSipilRequester),
                            onValueChange = {
                                statusSipil = it
                                fieldErrors = fieldErrors - "status_sipil"
                            })
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                Text(
                    "Perbarui Data Sosial",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF292929)
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }

            item {
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
                        AppDropdownField(
                            label = "Jaminan Kesehatan",
                            value = if (jaminanKesehatan) "Punya" else "Tidak",
                            options = jaminanKesehatanOptions,
                            onValueChange = {
                                jaminanKesehatan = it == "Punya"
                            }
                        )
                        AppTextField(
                            label = "Keterangan Tambahan",
                            value = keterangan,
                            singleLine = false,
                            onValueChange = { keterangan = it })
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
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
                            if (tempatLahir.isBlank()) put("tempat_lahir", "Tempat Lahir wajib diisi.")
                            if (statusKeluarga.isBlank()) put("status_keluarga", "Status keluarga wajib dipilih.")
                            if (statusSipil.isBlank()) put("status_sipil", "Status sipil wajib dipilih.")
                            
                        }
                        if (errors.isNotEmpty()) {
                            fieldErrors = errors
                            val firstErrorKey = errors.keys.first()
                            coroutineScope.launch {
                                when (firstErrorKey) {
                                    "nama" -> namaRequester.bringIntoView()
                                    "nik" -> nikRequester.bringIntoView()
                                    "tempat_lahir" -> tempatLahirRequester.bringIntoView()
                                    "tanggal_lahir" -> tanggalLahirRequester.bringIntoView()
                                    "status_keluarga" -> statusKeluargaRequester.bringIntoView()
                                    "status_sipil" -> statusSipilRequester.bringIntoView()
                                }
                            }
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
                            jaminanKesehatanBaru = jaminanKesehatan,
                            keteranganBaru = keterangan,
                            statusKeluargaBaru = statusKeluarga,
                            statusSipilBaru = statusSipil,
                            statusWargaBaru = statusWarga,
                            usiaBaru = calculatedUsia,
                            kategoriUsiaBaru = calculatedKategori,
                            tempatLahirBaru = tempatLahir,
                            golonganDarahBaru = golonganDarah.ifBlank { null },
                            sukuBaru = suku.ifBlank { null },
                            kewarganegaraanBaru = kewarganegaraan.ifBlank { "WNI" }
                        )
                        Toast.makeText(context, "Perubahan berhasil disimpan!", Toast.LENGTH_SHORT)
                            .show()
                        onBackClick()
                    }
                )
            }

            //item { Spacer(modifier = Modifier.height(80.dp)) }
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
