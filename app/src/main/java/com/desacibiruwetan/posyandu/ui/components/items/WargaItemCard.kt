package com.desacibiruwetan.posyandu.ui.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.AgeElderly
import com.desacibiruwetan.posyandu.ui.theme.AgeElderlyContainer
import com.desacibiruwetan.posyandu.ui.theme.AgeProductive
import com.desacibiruwetan.posyandu.ui.theme.AgeProductiveContainer
import com.desacibiruwetan.posyandu.ui.theme.AgeToddler
import com.desacibiruwetan.posyandu.ui.theme.AgeToddlerContainer
import com.desacibiruwetan.posyandu.ui.theme.ResidentFemale
import com.desacibiruwetan.posyandu.ui.theme.ResidentFemaleContainer
import com.desacibiruwetan.posyandu.ui.theme.ResidentMale
import com.desacibiruwetan.posyandu.ui.theme.ResidentMaleContainer
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite

@Composable
fun WargaItemCard(
    name: String,
    nik: String,
    rtRw: String,
    jenisKelamin: String? = null,
    kategoriUsia: String? = null,
    onClick: () -> Unit
) {
    val residentColor = residentAccent(jenisKelamin)
    val residentContainer = residentContainer(jenisKelamin)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = residentContainer, shape = RoundedCornerShape(16.dp))
            .border(1.dp, residentColor.copy(alpha = 0.24f), RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = Color(0xFF272727)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "NIK : $nik",
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = Color(0xFF6D7C78)
            )
            Spacer(modifier = Modifier.height(8.dp))

            kategoriUsia?.takeIf { it.isNotBlank() }?.let { label ->
                ResponsiveTwoColumn(
                    horizontalSpacing = 8.dp,
                    verticalSpacing = 8.dp,
                    first = { WargaBadge(text = rtRw, color = residentColor, container = SurfaceWhite.copy(alpha = 0.72f), modifier = it) },
                    second = {
                    val ageColor = ageAccent(label)
                    WargaBadge(text = label, color = ageColor, container = ageContainer(label), modifier = it)
                    }
                )
            } ?: WargaBadge(text = rtRw, color = residentColor, container = SurfaceWhite.copy(alpha = 0.72f))
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Detail",
            tint = residentColor,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
private fun WargaBadge(text: String, color: Color, container: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color = container, shape = RoundedCornerShape(100.dp))
            .border(1.dp, color.copy(alpha = 0.24f), RoundedCornerShape(100.dp))
            .padding(horizontal = 12.dp, vertical = 6.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            fontFamily = Inter,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.sp,
            color = color
        )
    }
}

private fun residentAccent(jenisKelamin: String?): Color =
    if (jenisKelamin.equals("Perempuan", ignoreCase = true)) ResidentFemale else ResidentMale

private fun residentContainer(jenisKelamin: String?): Color =
    if (jenisKelamin.equals("Perempuan", ignoreCase = true)) ResidentFemaleContainer else ResidentMaleContainer

private fun ageAccent(label: String): Color =
    when (label.lowercase()) {
        "balita" -> AgeToddler
        "lansia" -> AgeElderly
        "produktif" -> AgeProductive
        else -> Color(0xFF64748B)
    }

private fun ageContainer(label: String): Color =
    when (label.lowercase()) {
        "balita" -> AgeToddlerContainer
        "lansia" -> AgeElderlyContainer
        "produktif" -> AgeProductiveContainer
        else -> SurfaceWhite.copy(alpha = 0.72f)
    }
