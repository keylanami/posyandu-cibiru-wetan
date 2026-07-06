package com.desacibiruwetan.posyandu.ui.screen.warga

import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.RadioButtonChecked
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.data.local.entity.KeluargaEntity
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
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel
import com.desacibiruwetan.posyandu.viewmodel.KeluargaViewmodel

private data class KeluargaDropdownOption(
    val saveId: Int,
    val display: String
)

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TambahWargaScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    keluargaViewModel: KeluargaViewmodel
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val sharedPreferences = remember { SessionManager.getPreferences(context) }
    val token = SessionManager.getAuthorizationHeader(context)
    val userRt = sharedPreferences.getString("USER_RT", "04") ?: "04"
    val userRw = sharedPreferences.getString("USER_RW", "02") ?: "02"

    val apiKeluargaOptions by keluargaViewModel.keluargaOptions.collectAsState()
    val localKeluarga by keluargaViewModel.listKeluargaLocal.collectAsState()
    val localAnggota by anggotaViewModel.listAnggotaLocal.collectAsState()
    var selectedKeluargaDisplay by remember { mutableStateOf("") }
    var selectedKeluargaId by remember { mutableStateOf<Int?>(null) }

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
    var fieldErrors by remember { mutableStateOf<Map<String, String>>(emptyMap()) }

    // Requesters for scrolling
    val keluargaRequester = remember { BringIntoViewRequester() }
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


    LaunchedEffect(Unit) {
        if (token.isNotEmpty()) {
            keluargaViewModel.syncDataKeluarga(token)
            anggotaViewModel.syncDataAnggotaDariServer(token)
            keluargaViewModel.fetchKeluargaOptions(token)
        }
    }

    val localDropdownOptions = remember(localKeluarga, localAnggota) {
        localKeluarga.map { keluarga ->
            val kepalaKeluarga = localAnggota.firstOrNull { anggota ->
                anggota.belongsToKeluarga(keluarga) &&
                    anggota.statusKeluarga.equals("Kepala Keluarga", ignoreCase = true)
            }?.nama
            val saveId = keluarga.serverId ?: keluarga.localId
            val status = if (keluarga.isSynced) "" else " - tersimpan lokal"
            KeluargaDropdownOption(
                saveId = saveId,
                display = "KK ${keluarga.noKK} - ${kepalaKeluarga ?: "Tanpa Kepala"}$status"
            )
        }
    }
    val apiOnlyDropdownOptions = remember(apiKeluargaOptions, localDropdownOptions) {
        val knownServerIds = localKeluarga.mapNotNull { it.serverId }.toSet()
        apiKeluargaOptions
            .filter { it.id !in knownServerIds }
            .map { option ->
                KeluargaDropdownOption(
                    saveId = option.id,
                    display = "KK ${option.noKk} - ${option.kepalaKeluarga ?: "Tanpa Kepala"}"
                )
            }
    }
    val keluargaDropdownOptions = remember(localDropdownOptions, apiOnlyDropdownOptions) {
        (localDropdownOptions + apiOnlyDropdownOptions).distinctBy { it.saveId }
    }
    val dropdownKeluargaStrings = keluargaDropdownOptions.map { it.display }

    LaunchedEffect(keluargaDropdownOptions, selectedKeluargaDisplay) {
        if (selectedKeluargaDisplay.isBlank()) return@LaunchedEffect
        val selected = keluargaDropdownOptions.firstOrNull { it.display == selectedKeluargaDisplay }
        selectedKeluargaId = selected?.saveId
        if (selected == null) {
            selectedKeluargaDisplay = ""
        }
    }

    Scaffold(
        topBar = { AppTopBar(title = "Tambah Warga Baru", onBackClick = onBackClick) },
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
                    "Data Identitas",
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
                        ResponsiveTwoColumn(
                            first = { fieldModifier ->
                                AppTextField(
                                    label = "RT",
                                    value = userRt,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = fieldModifier
                                )
                            },
                            second = { fieldModifier ->
                                AppTextField(
                                    label = "RW",
                                    value = userRw,
                                    onValueChange = {},
                                    readOnly = true,
                                    modifier = fieldModifier
                                )
                            }
                        )

                        AppDropdownField(
                            label = "Pilih Keluarga",
                            value = selectedKeluargaDisplay,
                            options = dropdownKeluargaStrings,
                            error = fieldErrors["keluarga_id"],
                            modifier = Modifier.bringIntoViewRequester(keluargaRequester),
                            onValueChange = { selectedStr ->
                                selectedKeluargaDisplay = selectedStr
                                fieldErrors = fieldErrors - "keluarga_id"
                                selectedKeluargaId = keluargaDropdownOptions
                                    .firstOrNull { it.display == selectedStr }
                                    ?.saveId
                            }
                        )
                        if (keluargaDropdownOptions.isEmpty()) {
                            Text(
                                "Belum ada KK tersimpan. Buat rumah dan KK dulu, lalu kembali ke form ini.",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF66756F),
                                modifier = Modifier.padding(bottom = 12.dp)
                            )
                        }

                        AppTextField(
                            label = "Nama Lengkap",
                            value = namaLengkap,
                            placeholder = "Masukkan Nama Lengkap",
                            error = fieldErrors["nama"],
                            maxLength = 100,
                            capitalization = KeyboardCapitalization.Words,
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
                                    RadioButtonItem(
                                        "Laki-laki",
                                        jenisKelamin == "Laki-laki",
                                        modifier = fieldModifier
                                    ) { jenisKelamin = "Laki-laki" }
                                },
                                second = { fieldModifier ->
                                    RadioButtonItem(
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
                            placeholder = "Masukkan 16 digit NIK",
                            keyboardType = KeyboardType.Number,
                            maxLength = 16,
                            counterLabel = "NIK",
                            error = fieldErrors["nik"],
                            modifier = Modifier.bringIntoViewRequester(nikRequester),
                            onValueChange = {
                                val digits = it.filter(Char::isDigit).take(16)
                                nik = digits
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
                            modifier = Modifier.bringIntoViewRequester(tanggalLahirRequester),
                            onValueChange = {
                                tanggalLahir = it
                                fieldErrors = fieldErrors - "tanggal_lahir"
                            },
                            error = fieldErrors["tanggal_lahir"]
                        )

                        AppDropdownField(
                            label = "Golongan Darah",
                            value = golonganDarah,
                            options = golonganDarahOptions,
                            onValueChange = { golonganDarah = it })

                        AppTextField(
                            label = "Suku",
                            value = suku,
                            placeholder = "Contoh: Sunda",
                            capitalization = KeyboardCapitalization.Words,
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
                    "Data Sosial",
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
                            placeholder = "Contoh: Buruh, Pedagang, PNS",
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
                            placeholder = "Tambah catatan jika diperlukan...",
                            singleLine = false,
                            onValueChange = { keterangan = it })
                    }
                }
            }

            item { Spacer(modifier = Modifier.height(32.dp)) }

            item {
                PrimaryButton(
                    text = "Simpan Warga Baru",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = {
                        val errors = buildMap {
                            if (selectedKeluargaId == null) put("keluarga_id", "Keluarga wajib dipilih.")
                            if (namaLengkap.isBlank()) put("nama", "Nama lengkap wajib diisi.")
                            if (nik.isBlank()) put("nik", "NIK wajib diisi.")
                            else if (nik.length != 16) put("nik", "NIK harus 16 digit.")
                            if (tanggalLahir.isBlank()) put("tanggal_lahir", "Tanggal lahir wajib dipilih.")
                            if (statusKeluarga.isBlank()) put("status_keluarga", "Status keluarga wajib dipilih.")
                            if (statusSipil.isBlank()) put("status_sipil", "Status sipil wajib dipilih.")
                            if (tempatLahir.isBlank()) put("tempat_lahir", "Tempat Lahir wajib diisi.")
                            if (statusKeluarga == "Kepala Keluarga" && selectedKeluargaId != null) {
                                val alreadyHasKepala = localAnggota.any {
                                    it.keluargaId == selectedKeluargaId &&
                                            it.statusKeluarga.equals("Kepala Keluarga", ignoreCase = true)
                                } || apiKeluargaOptions.any {
                                    it.id == selectedKeluargaId && !it.kepalaKeluarga.isNullOrBlank()
                                }

                                if (alreadyHasKepala) {
                                    put("status_keluarga", "Keluarga ini sudah memiliki Kepala Keluarga.")
                                }
                            }
                        }
                        if (errors.isNotEmpty()) {
                            fieldErrors = errors
                            val firstErrorKey = errors.keys.first()
                            coroutineScope.launch {
                                when (firstErrorKey) {
                                    "keluarga_id" -> keluargaRequester.bringIntoView()
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
                        val finalKeluargaId = selectedKeluargaId ?: return@PrimaryButton
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
                            jaminanKesehatan = jaminanKesehatan,
                            statusKeluarga = statusKeluarga,
                            statusSipil = statusSipil,
                            statusWarga = "aktif",
                            keterangan = keterangan,
                            usia = calculatedUsia,
                            kategoriUsia = calculatedKategori,
                            tempatLahir = tempatLahir,
                            golonganDarah = golonganDarah.ifBlank { null },
                            suku = suku.ifBlank { null },
                            kewarganegaraan = kewarganegaraan.ifBlank { "WNI" }
                        )
                        Toast.makeText(context, "Anggota berhasil disimpan!", Toast.LENGTH_SHORT).show()
                        onBackClick()
                    }
                )
            }

            //item { Spacer(modifier = Modifier.height(80.dp)) }
        }
    }
}

@Composable
fun RadioButtonItem(text: String, isSelected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
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
        Text(
            text = text,
            fontFamily = Inter,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            color = Color(0xFF272727)
        )
    }
}

private fun AnggotaEntity.belongsToKeluarga(keluarga: KeluargaEntity): Boolean {
    val possibleIds = setOfNotNull(keluarga.serverId, keluarga.localId)
    return keluargaId in possibleIds
}
