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
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PregnantWoman
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
                Text(
                    text = warga.nama,
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    color = Color(0xFF272727)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "NIK : ${warga.nik}",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    color = Color(0xFFA1A1A1)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    val genderColor =
                        if (warga.jenisKelamin == "Laki-laki") Color(0xFF2C74B3) else Color(
                            0xFFE94560
                        )
                    BadgeInfo(text = warga.jenisKelamin, bgColor = genderColor)
                    BadgeInfo(text = "${warga.usia} Tahun", bgColor = PrimaryGreen)
                }

                Spacer(modifier = Modifier.height(32.dp))

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        "Status Kesehatan",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp,
                        color = Color(0xFF272727)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        StatusChip(
                            text = "KB Aktif",
                            icon = Icons.Default.FamilyRestroom,
                            containerColor = Color(0xFFDF8B89),
                            borderColor = Color(0xFF9A3F3C),
                            contentColor = Color(0xFF9A3F3C)
                        )
                        StatusChip(
                            text = "Balita",
                            icon = Icons.Default.ChildCare,
                            containerColor = Color(0xFFC7FFEC),
                            borderColor = PrimaryGreen,
                            contentColor = PrimaryGreen
                        )
                        StatusChip(
                            text = "Bumil",
                            icon = Icons.Default.PregnantWoman,
                            containerColor = Color(0xFFF9F9F9),
                            borderColor = Color(0xFFACACAC),
                            contentColor = Color(0xFFACACAC)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                InfoKependudukanCard(warga = warga)

                Spacer(modifier = Modifier.height(32.dp))

                PrimaryButton(
                    text = "Lengkapi Data Tambahan",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO */ })
                Spacer(modifier = Modifier.height(16.dp))
                PrimaryButton(
                    text = "Lihat Riwayat Kunjungan",
                    icon = Icons.Default.History,
                    containerColor = Color(0xFFD5D5D5),
                    contentColor = Color(0xFF272727),
                    onClick = { /* TODO */ })

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