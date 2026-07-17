package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.CoPresent
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HeartBroken
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Output
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.CategoryCard
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.dialog.SearchWargaDialog
import com.desacibiruwetan.posyandu.ui.components.input.AppDateField
import com.desacibiruwetan.posyandu.ui.components.input.AppRadioButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.utils.calculateAgeInfo
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun CatatKejadianScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    initialLocalId: Int? = null
) {
    val context = LocalContext.current
    val listWarga by anggotaViewModel.listAnggotaLocal.collectAsState()

    val token = SessionManager.getAuthorizationHeader(context)

    var showDialog by remember { mutableStateOf(false) }
    var selectedWarga by remember { mutableStateOf<AnggotaEntity?>(null) }

    val categories =
        listOf("Kelahiran", "Pindah Keluar", "Pindah Masuk", "Nikah", "Cerai", "Meninggal")
    var selectedCategory by remember { mutableStateOf(categories[0]) }

    var tanggalKejadian by remember { mutableStateOf("") }
    var keterangan by remember { mutableStateOf("") }

    var namaBayi by remember { mutableStateOf("") }
    var jenisKelaminBayi by remember { mutableStateOf("") }
    var namaAyah by remember { mutableStateOf("") }
    var namaIbu by remember { mutableStateOf("") }
    var bbLahir by remember { mutableStateOf("") }
    var tbLahir by remember { mutableStateOf("") }
    var nik by remember { mutableStateOf("") }

    var asalAlamat by remember { mutableStateOf("") }
    var tujuanAlamat by remember { mutableStateOf("") }
    var namaPasangan by remember { mutableStateOf("") }
    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    LaunchedEffect(initialLocalId, listWarga) {
        if (initialLocalId != null && listWarga.isNotEmpty()) {
            val found = listWarga.find { it.localId == initialLocalId }
            if (found != null) {
                selectedWarga = found
                fieldErrors = fieldErrors - "warga_id"
                if (found.jenisKelamin == "Perempuan") {
                    namaIbu = found.nama
                }
            }
        }
    }

    val simpanKejadian = save@{
        if (selectedWarga == null) {
            fieldErrors = mapOf("warga_id" to "Warga wajib dipilih.")
        } else if (tanggalKejadian.isBlank()) {
            fieldErrors = mapOf("tanggal_kejadian" to "Tanggal kejadian wajib dipilih.")
        } else {
            val apiDate = tanggalKejadian

            when (selectedCategory) {
                "Kelahiran" -> {
                    val tb = tbLahir.toDoubleOrNull()
                    val bb = bbLahir.toDoubleOrNull()
                    val errors = buildMap {
                        if (namaBayi.isBlank()) put("nama_bayi", "Nama bayi wajib diisi.")
                        if (nik.isBlank()) put("nik", "NIK bayi wajib diisi.")
                        else if (nik.length != 16) put("nik", "NIK harus 16 digit.")
                        if (jenisKelaminBayi.isBlank()) put("jenis_kelamin", "Jenis kelamin wajib dipilih.")
                        if (namaAyah.isBlank()) put("nama_ayah", "Nama ayah wajib diisi.")
                        if (namaIbu.isBlank()) put("nama_ibu", "Nama ibu wajib diisi.")
                        if (bb == null || bb <= 0.0) put("bb_lahir", "BB lahir harus lebih dari 0.")
                        if (tb == null || tb <= 0.0) put("tb_lahir", "TB lahir harus lebih dari 0.")
                    }
                    if (errors.isNotEmpty()) {
                        fieldErrors = errors
                        return@save
                    }
                    val validatedTb = tb ?: return@save
                    val validatedBb = bb ?: return@save

                    val catatanKelahiran =
                        "Bayi: $namaBayi, Ayah: $namaAyah, Ibu: $namaIbu, BB: $bbLahir kg, TB: $tbLahir cm. $keterangan"

                    anggotaViewModel.tambahAnggota(
                        token = token,
                        keluargaId = selectedWarga?.keluargaId ?: 0,
                        nik = nik,
                        nama = namaBayi,
                        tanggalLahir = apiDate,
                        jenisKelamin = jenisKelaminBayi,
                        pendidikanTerakhir = "Tidak/Belum Sekolah",
                        pekerjaan = "Belum Bekerja",
                        jaminanKesehatan = false,
                        statusKeluarga = "Anak",
                        statusSipil = "Belum Kawin",
                        statusWarga = "kelahiran",
                        keterangan = catatanKelahiran,
                        usia = "0",
                        kategoriUsia = "Balita",
                        onSuccess = { localId, serverId ->
                            if (serverId != null) {
                                anggotaViewModel.createDataBalita(
                                    token,
                                    localId,
                                    serverId,
                                    validatedTb,
                                    validatedBb
                                )
                            }
                        }
                    )
                }

                "Pindah Masuk" -> {
                    selectedWarga?.let { warga ->
                        val (calcUsia, calcKat) = calculateAgeInfo(warga.tanggalLahir)
                        anggotaViewModel.updateAnggota(
                            token = token,
                            anggotaLokal = warga,
                            nikBaru = warga.nik,
                            namaBaru = warga.nama,
                            tanggalLahirBaru = warga.tanggalLahir,
                            jenisKelaminBaru = warga.jenisKelamin,
                            pendidikanTerakhirBaru = warga.pendidikanTerakhir ?: "",
                            pekerjaanBaru = warga.pekerjaan ?: "",
                            jaminanKesehatanBaru = warga.jaminanKesehatan,
                            statusKeluargaBaru = warga.statusKeluarga,
                            statusSipilBaru = warga.statusSipil,
                            statusWargaBaru = "aktif",
                            keteranganBaru = "Tgl Masuk: $apiDate. Asal: $asalAlamat. $keterangan",
                            usiaBaru = calcUsia,
                            kategoriUsiaBaru = calcKat
                        )
                    }
                }

                "Pindah Keluar" -> {
                    selectedWarga?.let { warga ->
                        val (calcUsia, calcKat) = calculateAgeInfo(warga.tanggalLahir)
                        anggotaViewModel.updateAnggota(
                            token = token,
                            anggotaLokal = warga,
                            nikBaru = warga.nik,
                            namaBaru = warga.nama,
                            tanggalLahirBaru = warga.tanggalLahir,
                            jenisKelaminBaru = warga.jenisKelamin,
                            pendidikanTerakhirBaru = warga.pendidikanTerakhir ?: "",
                            pekerjaanBaru = warga.pekerjaan ?: "",
                            jaminanKesehatanBaru = warga.jaminanKesehatan,
                            statusKeluargaBaru = warga.statusKeluarga,
                            statusSipilBaru = warga.statusSipil,
                            statusWargaBaru = "pindah_keluar",
                            keteranganBaru = "Tgl Keluar: $apiDate. Tujuan: $tujuanAlamat. $keterangan",
                            usiaBaru = calcUsia,
                            kategoriUsiaBaru = calcKat
                        )
                    }
                }

                "Meninggal" -> {
                    selectedWarga?.let { warga ->
                        val (calcUsia, calcKat) = calculateAgeInfo(warga.tanggalLahir)
                        anggotaViewModel.updateAnggota(
                            token = token,
                            anggotaLokal = warga,
                            nikBaru = warga.nik,
                            namaBaru = warga.nama,
                            tanggalLahirBaru = warga.tanggalLahir,
                            jenisKelaminBaru = warga.jenisKelamin,
                            pendidikanTerakhirBaru = warga.pendidikanTerakhir ?: "",
                            pekerjaanBaru = warga.pekerjaan ?: "",
                            jaminanKesehatanBaru = warga.jaminanKesehatan,
                            statusKeluargaBaru = warga.statusKeluarga,
                            statusSipilBaru = warga.statusSipil,
                            statusWargaBaru = "meninggal",
                            keteranganBaru = "Wafat: $apiDate. Penyebab: $keterangan",
                            usiaBaru = calcUsia,
                            kategoriUsiaBaru = calcKat
                        )
                    }
                }

                "Nikah", "Cerai" -> {
                    selectedWarga?.let { warga ->
                        val (calcUsia, calcKat) = calculateAgeInfo(warga.tanggalLahir)
                        anggotaViewModel.updateAnggota(
                            token = token,
                            anggotaLokal = warga,
                            nikBaru = warga.nik,
                            namaBaru = warga.nama,
                            tanggalLahirBaru = warga.tanggalLahir,
                            jenisKelaminBaru = warga.jenisKelamin,
                            pendidikanTerakhirBaru = warga.pendidikanTerakhir ?: "",
                            pekerjaanBaru = warga.pekerjaan ?: "",
                            jaminanKesehatanBaru = warga.jaminanKesehatan,
                            statusKeluargaBaru = warga.statusKeluarga,
                            statusSipilBaru = if (selectedCategory == "Nikah") "Kawin Tercatat" else "Cerai Hidup",
                            statusWargaBaru = warga.statusWarga
                                ?: "aktif",
                            keteranganBaru = "Status baru: $selectedCategory dengan $namaPasangan. $keterangan",
                            usiaBaru = calcUsia,
                            kategoriUsiaBaru = calcKat
                        )
                    }
                }
            }
            Toast.makeText(context, "Data $selectedCategory berhasil dicatat", Toast.LENGTH_SHORT)
                .show()
            onBackClick()
        }
    }

    if (showDialog) {
        SearchWargaDialog(
            onDismiss = { showDialog = false },
            onWargaSelected = { warga ->
                selectedWarga = warga
                fieldErrors = fieldErrors - "warga_id"
                if (warga.jenisKelamin == "Perempuan") {
                    namaIbu = warga.nama
                    namaAyah = ""
                } else {
                    namaAyah = warga.nama
                    namaIbu = ""
                }
            },
            anggotaViewModel = anggotaViewModel
        )
    }

    Scaffold(
        topBar = { AppTopBar(title = "Catat Kejadian", onBackClick = onBackClick) },
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

            Box(modifier = Modifier.clickable { showDialog = true }) {
                if (selectedWarga != null) {
                    UpdateHeaderCard(
                        title = "Catat kejadian untuk",
                        name = selectedWarga!!.nama,
                        icon = Icons.Default.EditNote
                    )
                } else {
                    UpdateHeaderCard(
                        title = "Pilih Warga",
                        name = "Ketuk untuk mencari data warga",
                        icon = Icons.Default.Search
                    )
                }
            }
            fieldErrors["warga_id"]?.let {
                Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
            }

            Spacer(modifier = Modifier.height(24.dp))

            ResponsiveTwoColumn(
                first = { itemModifier ->
                CategoryCard(
                    "Kelahiran",
                    Icons.Default.ChildCare,
                    selectedCategory == "Kelahiran",
                    { selectedCategory = "Kelahiran" },
                    itemModifier
                )
                },
                second = { itemModifier ->
                CategoryCard(
                    "Pindah Keluar",
                    Icons.Default.Output,
                    selectedCategory == "Pindah Keluar",
                    { selectedCategory = "Pindah Keluar" },
                    itemModifier
                )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ResponsiveTwoColumn(
                first = { itemModifier ->
                CategoryCard(
                    "Pindah Masuk",
                    Icons.AutoMirrored.Default.Input,
                    selectedCategory == "Pindah Masuk",
                    { selectedCategory = "Pindah Masuk" },
                    itemModifier
                )
                },
                second = { itemModifier ->
                CategoryCard(
                    "Nikah",
                    Icons.Default.Favorite,
                    selectedCategory == "Nikah",
                    { selectedCategory = "Nikah" },
                    itemModifier
                )
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ResponsiveTwoColumn(
                first = { itemModifier ->
                CategoryCard(
                    "Cerai",
                    Icons.Default.HeartBroken,
                    selectedCategory == "Cerai",
                    { selectedCategory = "Cerai" },
                    itemModifier
                )
                },
                second = { itemModifier ->
                CategoryCard(
                    "Meninggal",
                    Icons.Default.CoPresent,
                    selectedCategory == "Meninggal",
                    { selectedCategory = "Meninggal" },
                    itemModifier
                )
                }
            )

            Spacer(modifier = Modifier.height(32.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(18.dp))
                    .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Info,
                            contentDescription = null,
                            tint = PrimaryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Detail $selectedCategory",
                            fontFamily = Inter,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp,
                            color = PrimaryGreen
                        )
                    }
                    Spacer(modifier = Modifier.height(24.dp))

                    when (selectedCategory) {
                        "Kelahiran" -> {
                            AppTextField(
                                label = "Nama Bayi",
                                value = namaBayi,
                                error = fieldErrors["nama_bayi"],
                                onValueChange = {
                                    namaBayi = it
                                    fieldErrors = fieldErrors - "nama_bayi"
                                })

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

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 12.dp)
                            ) {
                                Text(
                                    text = "Jenis Kelamin",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                ResponsiveTwoColumn(
                                    first = { fieldModifier ->
                                        AppRadioButton(
                                            text = "Laki-laki",
                                            isSelected = jenisKelaminBayi == "Laki-laki",
                                            onClick = { jenisKelaminBayi = "Laki-laki" },
                                            modifier = fieldModifier
                                        )
                                    },
                                    second = { fieldModifier ->
                                        AppRadioButton(
                                            text = "Perempuan",
                                            isSelected = jenisKelaminBayi == "Perempuan",
                                            onClick = { jenisKelaminBayi = "Perempuan" },
                                            modifier = fieldModifier
                                        )
                                    }
                                )
                                fieldErrors["jenis_kelamin"]?.let {
                                    Text(it, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.bodySmall)
                                }
                            }

                            ResponsiveTwoColumn(
                                first = { fieldModifier ->
                                AppTextField(
                                    label = "Nama Ayah",
                                    value = namaAyah,
                                    error = fieldErrors["nama_ayah"],
                                    onValueChange = {
                                        namaAyah = it
                                        fieldErrors = fieldErrors - "nama_ayah"
                                    },
                                    modifier = fieldModifier
                                )
                                },
                                second = { fieldModifier ->
                                AppTextField(
                                    label = "Nama Ibu",
                                    value = namaIbu,
                                    error = fieldErrors["nama_ibu"],
                                    onValueChange = {
                                        namaIbu = it
                                        fieldErrors = fieldErrors - "nama_ibu"
                                    },
                                    modifier = fieldModifier
                                )
                                }
                            )

                            ResponsiveTwoColumn(
                                first = { fieldModifier ->
                                AppTextField(
                                    label = "BB Lahir (kg)",
                                    value = bbLahir,
                                    keyboardType = KeyboardType.Decimal,
                                    error = fieldErrors["bb_lahir"],
                                    onValueChange = {
                                        if (it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                            bbLahir = it
                                            fieldErrors = fieldErrors - "bb_lahir"
                                        }
                                    },
                                    modifier = fieldModifier
                                )
                                },
                                second = { fieldModifier ->
                                AppTextField(
                                    label = "TB Lahir (cm)",
                                    value = tbLahir,
                                    keyboardType = KeyboardType.Decimal,
                                    error = fieldErrors["tb_lahir"],
                                    onValueChange = {
                                        if (it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                            tbLahir = it
                                            fieldErrors = fieldErrors - "tb_lahir"
                                        }
                                    },
                                    modifier = fieldModifier
                                )
                                }
                            )
                        }

                        "Pindah Masuk" -> {
                            AppTextField(
                                label = "Asal Alamat",
                                value = asalAlamat,
                                onValueChange = { asalAlamat = it })
                        }

                        "Pindah Keluar" -> {
                            AppTextField(
                                label = "Tujuan Alamat",
                                value = tujuanAlamat,
                                onValueChange = { tujuanAlamat = it })
                        }

                        "Nikah" -> {
                            AppTextField(
                                label = "Nama Pasangan",
                                value = namaPasangan,
                                onValueChange = { namaPasangan = it })
                        }
                    }

                    val labelTanggal = when (selectedCategory) {
                        "Kelahiran" -> "Tanggal Lahir"
                        "Pindah Masuk" -> "Tanggal Pindah Masuk"
                        "Pindah Keluar" -> "Tanggal Pindah Keluar"
                        "Nikah" -> "Tanggal Menikah"
                        "Cerai" -> "Tanggal Cerai"
                        "Meninggal" -> "Tanggal Meninggal"
                        else -> "Tanggal Kejadian"
                    }

                    AppDateField(
                        label = labelTanggal,
                        value = tanggalKejadian,
                        error = fieldErrors["tanggal_kejadian"],
                        onValueChange = {
                            tanggalKejadian = it
                            fieldErrors = fieldErrors - "tanggal_kejadian"
                        }
                    )

                    AppTextField(
                        label = if (selectedCategory == "Meninggal") "Penyebab / Catatan" else "Keterangan",
                        value = keterangan,
                        placeholder = "Tambahkan catatan jika diperlukan...",
                        singleLine = false,
                        onValueChange = { keterangan = it })

                    Spacer(modifier = Modifier.height(24.dp))

                    PrimaryButton(
                        text = "Simpan Data Kejadian",
                        icon = Icons.Default.AddCircleOutline,
                        onClick = { simpanKejadian() })
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
