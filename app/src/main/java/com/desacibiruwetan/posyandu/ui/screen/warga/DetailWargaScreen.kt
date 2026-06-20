package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.items.InfoKependudukanCard
import com.desacibiruwetan.posyandu.ui.components.items.StatusChip
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun DetailWargaScreen(
    onBackClick: () -> Unit,
    onCatatKejadianClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    nikWarga: String? = null,
    anggotaViewModel: AnggotaViewmodel,
) {
    val listWargaAsli by anggotaViewModel.listAnggotaLocal.collectAsState()
    val warga = listWargaAsli.find { it.nik == nikWarga }

    Scaffold(
        topBar = { AppTopBar(title = "Detail Warga", onBackClick = onBackClick) },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(32.dp))

            if (warga != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SurfaceWhite, RoundedCornerShape(10.dp))
                        .padding(18.dp)
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = warga.nama,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF272727)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "NIK: ${warga.nik}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color(0xFF6D6D6D)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            val genderColor =
                                if (warga.jenisKelamin == "Laki-laki") Color(0xFF2C74B3) else Color(0xFFE94560)
                            BadgeInfo(text = warga.jenisKelamin, bgColor = genderColor)
                            BadgeInfo(text = "${warga.usia ?: "-"} Tahun", bgColor = PrimaryGreen)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Status Kesehatan",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Color(0xFF272727)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatusChip(
                            text = "${warga.kategoriUsia}",
                            icon = Icons.Default.ChildCare,
                            containerColor = Color(0xFFC7FFEC),
                            borderColor = PrimaryGreen,
                            contentColor = PrimaryGreen
                        )
                        StatusChip(
                            text = "Keluarga ${warga.keluargaId}",
                            icon = Icons.Default.Home,
                            containerColor = Color(0xFFF4FAF8),
                            borderColor = Color(0xFFE0ECE8),
                            contentColor = Color(0xFF44645B)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                InfoKependudukanCard(warga = warga)

                Spacer(modifier = Modifier.height(32.dp))

                PrimaryButton(
                    text = "Lengkapi Data Tambahan",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { onCatatKejadianClick(warga.nik) })

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = "Edit data",
                    icon = Icons.Default.Edit,
                    onClick = { onEditClick(warga.nik) }
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(
                    text = "Riwayat Kunjungan Belum Tersedia",
                    icon = Icons.Default.History,
                    containerColor = Color(0xFFD5D5D5),
                    contentColor = Color(0xFF272727),
                    enabled = false,
                    onClick = {}
                )

            } else {
                Text("Data warga tidak ditemukan.", color = Color.Gray)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun BadgeInfo(text: String, bgColor: Color) {
    Box(
        modifier = Modifier
            .background(color = bgColor, shape = RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = Inter,
            fontWeight = FontWeight.Medium,
            fontSize = 10.sp,
            color = SurfaceWhite
        )
    }
}
