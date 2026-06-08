package com.desacibiruwetan.posyandu.ui.screen.warga

import android.content.Context
import android.widget.Toast
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
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
import com.desacibiruwetan.posyandu.ui.components.input.AppRadioButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.UpdateHeaderCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.utils.DateVisualTransformation

@Composable
fun CatatKejadianScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    initialNik: String? = null
) {

    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val listWarga by anggotaViewModel.listAnggotaLocal.collectAsState()


    val sharedPreferences = context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE)
    val token = "Bearer ${sharedPreferences.getString("TOKEN", "")}"

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


    LaunchedEffect(initialNik, listWarga) {
        if (!initialNik.isNullOrEmpty() && listWarga.isNotEmpty()) {
            val found = listWarga.find {
                it.nik == initialNik
            }

            if (found != null) {
                selectedWarga = found
                if (found.jenisKelamin == "Perempuan") {
                    namaIbu = found.nama
                }
            }
        }
    }

    val simpanKejadian = {
        if (selectedWarga == null && selectedCategory != "Pindah Masuk") {
            Toast.makeText(context, "Pilih warga terlebih dahulu", Toast.LENGTH_SHORT).show()
        } else if (tanggalKejadian.length < 8) {
            Toast.makeText(context, "Tanggal tidak valid", Toast.LENGTH_SHORT).show()
        } else {
            when (selectedCategory) {
                "Kelahiran" -> {
                    val catatanKelahiran = "Bayi: $namaBayi, Ayah: $namaAyah, Ibu: $namaIbu, BB: $bbLahir kg, TB: $tbLahir cm. $keterangan"

                    val tb = tbLahir.toDoubleOrNull() ?: 0.0
                    val bb = bbLahir.toDoubleOrNull() ?: 0.0

                    anggotaViewModel.tambahAnggota(
                        token = token,
                        keluargaId = selectedWarga?.keluargaId ?: 0,
                        nik = nik,
                        nama = namaBayi,
                        tanggalLahir = tanggalKejadian,
                        jenisKelamin = jenisKelaminBayi,
                        pendidikanTerakhir = "Belum Sekolah",
                        pekerjaan = "Tidak Bekerja",
                        noBpjs = "",
                        statusKeluarga = "Anak",
                        statusSipil = "Belum Kawin",
                        statusWarga = "aktif",
                        keterangan = catatanKelahiran,
                        usia = "0 th",
                        kategoriUsia = "Balita",
                        onSuccess = { serverId ->
                            if (serverId != null) {
                                anggotaViewModel.updateDataBalita(token, serverId, namaAyah, namaIbu, tb, bb)
                            }
                        }
                    )
                }

                "Meninggal", "Pindah Keluar" -> {
                    selectedWarga?.let { warga ->
                        anggotaViewModel.updateAnggota(
                            token = token,
                            anggotaLokal = warga,
                            nikBaru = warga.nik,
                            namaBaru = warga.nama,
                            tanggalLahirBaru = warga.tanggalLahir,
                            jenisKelaminBaru = warga.jenisKelamin,
                            pendidikanTerakhirBaru = warga.pendidikanTerakhir ?: "",
                            pekerjaanBaru = warga.pekerjaan ?: "",
                            noBpjsBaru = warga.noBpjs ?: "",
                            statusKeluargaBaru = warga.statusKeluarga,
                            statusSipilBaru = warga.statusSipil,
                            statusWargaBaru = if (selectedCategory == "Meninggal") "meninggal" else "pindah",
                            keteranganBaru = "Tanggal $selectedCategory: $tanggalKejadian. Catatan: $keterangan",
                            usiaBaru = warga.usia ?: "",
                            kategoriUsiaBaru = warga.kategoriUsia ?: ""
                        )
                    }
                }

                "Nikah", "Cerai" -> {
                    selectedWarga?.let { warga ->
                        anggotaViewModel.updateAnggota(
                            token = token,
                            anggotaLokal = warga,
                            nikBaru = warga.nik,
                            namaBaru = warga.nama,
                            tanggalLahirBaru = warga.tanggalLahir,
                            jenisKelaminBaru = warga.jenisKelamin,
                            pendidikanTerakhirBaru = warga.pendidikanTerakhir ?: "",
                            pekerjaanBaru = warga.pekerjaan ?: "",
                            noBpjsBaru = warga.noBpjs ?: "",
                            statusKeluargaBaru = warga.statusKeluarga,
                            statusSipilBaru = if (selectedCategory == "Nikah") "Kawin" else "Cerai",
                            statusWargaBaru = warga.statusWarga ?: "aktif",
                            keteranganBaru = "Status baru: $selectedCategory dengan $namaPasangan. $keterangan",
                            usiaBaru = warga.usia ?: "",
                            kategoriUsiaBaru = warga.kategoriUsia ?: ""
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
                .padding(horizontal = 24.dp)
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

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryCard(
                    "Kelahiran",
                    Icons.Default.ChildCare,
                    selectedCategory == "Kelahiran",
                    { selectedCategory = "Kelahiran" },
                    Modifier.weight(1f)
                )
                CategoryCard(
                    "Pindah Keluar",
                    Icons.Default.Output,
                    selectedCategory == "Pindah Keluar",
                    { selectedCategory = "Pindah Keluar" },
                    Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryCard(
                    "Pindah Masuk",
                    Icons.AutoMirrored.Default.Input,
                    selectedCategory == "Pindah Masuk",
                    { selectedCategory = "Pindah Masuk" },
                    Modifier.weight(1f)
                )
                CategoryCard(
                    "Nikah",
                    Icons.Default.Favorite,
                    selectedCategory == "Nikah",
                    { selectedCategory = "Nikah" },
                    Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                CategoryCard(
                    "Cerai",
                    Icons.Default.HeartBroken,
                    selectedCategory == "Cerai",
                    { selectedCategory = "Cerai" },
                    Modifier.weight(1f)
                )
                CategoryCard(
                    "Meninggal",
                    Icons.Default.CoPresent,
                    selectedCategory == "Meninggal",
                    { selectedCategory = "Meninggal" },
                    Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))


            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(16.dp, spotColor = Color(0x40DFDFDF), shape = RoundedCornerShape(15.dp))
                    .background(SurfaceWhite, RoundedCornerShape(15.dp))
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
                                onValueChange = { namaBayi = it })

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                AppTextField(
                                    label = "NIK",
                                    value = nik,
                                    onValueChange = { nik = it })
                            }


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
                                Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                                    AppRadioButton(
                                        "Laki-laki", jenisKelaminBayi == "Laki-laki"
                                    ) { jenisKelaminBayi = "Laki-laki" }
                                    AppRadioButton(
                                        "Perempuan", jenisKelaminBayi == "Perempuan"
                                    ) { jenisKelaminBayi = "Perempuan" }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                AppTextField(
                                    label = "Nama Ayah",
                                    value = namaAyah,
                                    onValueChange = { namaAyah = it },
                                    modifier = Modifier.weight(1f)
                                )
                                AppTextField(
                                    label = "Nama Ibu",
                                    value = namaIbu,
                                    onValueChange = { namaIbu = it },
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                                AppTextField(
                                    label = "BB Lahir (kg)",
                                    value = bbLahir,
                                    keyboardType = KeyboardType.Number,
                                    onValueChange = { bbLahir = it },
                                    modifier = Modifier.weight(1f)
                                )
                                AppTextField(
                                    label = "TB Lahir (cm)",
                                    value = tbLahir,
                                    keyboardType = KeyboardType.Number,
                                    onValueChange = { tbLahir = it },
                                    modifier = Modifier.weight(1f)
                                )
                            }
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

                        "Cerai" -> {
                            // gada field khusus kecuali mau nambah nama pasangan theyre divorced to
                        }

                        "Meninggal" -> {
                            // gada input khusus
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

                    AppTextField(
                        label = labelTanggal,
                        value = tanggalKejadian,
                        placeholder = "dd/mm/yyyy",
                        keyboardType = KeyboardType.Number,
                        visualTransformation = DateVisualTransformation(),
                        onValueChange = {
                            if (it.length <= 8 && it.all { char -> char.isDigit() }) tanggalKejadian =
                                it
                        })

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