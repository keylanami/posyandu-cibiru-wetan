package com.desacibiruwetan.posyandu.ui.components.items

import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite

@Composable
fun InfoKependudukanCard(warga: AnggotaEntity) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(
                elevation = 16.dp,
                spotColor = Color(0x40CBCBCB),
                ambientColor = Color(0x40CBCBCB),
                shape = RoundedCornerShape(15.dp)
            )
            .background(color = SurfaceWhite, shape = RoundedCornerShape(15.dp))
            .padding(24.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Informasi Kependudukan",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    color = PrimaryGreen
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFDCDCDC)))
            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.weight(1f)) {
                    InfoItem(label = "Tanggal Lahir", value = warga.tanggalLahir)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Status Keluarga", value = warga.statusKeluarga)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Pekerjaan", value = warga.pekerjaan ?: "-")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "No BPJS", value = warga.noBpjs ?: "-")
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    InfoItem(label = "Status Sipil", value = warga.statusSipil)
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Status Warga", value = warga.statusWarga ?: "-")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "Pendidikan", value = warga.pendidikanTerakhir ?: "-")
                    Spacer(modifier = Modifier.height(16.dp))
                    InfoItem(label = "ID Keluarga", value = warga.keluargaId.toString())
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            InfoItem(label = "Keterangan", value = warga.keterangan ?: "-")
        }
    }
}