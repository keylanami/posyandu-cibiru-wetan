package com.desacibiruwetan.posyandu.ui.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.BorderGray
import com.desacibiruwetan.posyandu.ui.theme.HealthBlue
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite

@Composable
fun InfoKependudukanCard(warga: AnggotaEntity, noKk: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = SurfaceWhite, shape = RoundedCornerShape(18.dp))
            .border(1.dp, BorderGray.copy(alpha = 0.65f), RoundedCornerShape(18.dp))
            .padding(20.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = HealthBlue,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Informasi Kependudukan",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = HealthBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(BorderGray))
            Spacer(modifier = Modifier.height(16.dp))

            ResponsiveTwoColumn(
                first = { columnModifier ->
                Column(modifier = columnModifier) {
                    InfoItem(label = "Tempat Lahir", value = warga.tempatLahir ?: "-")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Tanggal Lahir", value = warga.tanggalLahir)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Golongan Darah", value = warga.golonganDarah ?: "-")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Status Keluarga", value = warga.statusKeluarga)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Pekerjaan", value = warga.pekerjaan ?: "-")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Jaminan Kesehatan", value = if (warga.jaminanKesehatan) "Punya" else "Tidak")
                }
                },
                second = { columnModifier ->
                Column(modifier = columnModifier) {
                    InfoItem(label = "Suku", value = warga.suku ?: "-")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Kewarganegaraan", value = warga.kewarganegaraan ?: "WNI")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Status Sipil", value = warga.statusSipil)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Status Warga", value = warga.statusWarga ?: "Aktif")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Pendidikan", value = warga.pendidikanTerakhir ?: "-")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "No. KK", value = noKk)
                }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            InfoItem(label = "Keterangan", value = warga.keterangan ?: "-")
        }
    }
}
