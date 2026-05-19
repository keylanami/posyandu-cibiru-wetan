package com.desacibiruwetan.posyandu.ui.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite

@Composable
fun AppTopBar(
    title: String,
    onBackClick: () -> Unit
) {

    val topbarList = listOf<String>(
        "Kembali", "Detail Warga"
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = PrimaryGreen)
            .statusBarsPadding()
            .height(64.dp)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = topbarList[0],
            tint = SurfaceWhite,
            modifier = Modifier
                .size(28.dp)
                .clickable { onBackClick() }
        )

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = title,
            fontFamily = Inter,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = SurfaceWhite
        )
    }
}