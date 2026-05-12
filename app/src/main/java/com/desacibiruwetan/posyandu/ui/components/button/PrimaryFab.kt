package com.desacibiruwetan.posyandu.ui.components.button

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.ErrorDark
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite

@Composable
fun PrimaryFab(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color = ErrorDark
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = containerColor,
        contentColor = SurfaceWhite,
        shape = RoundedCornerShape(10.dp),
        icon = { Icon(imageVector = icon, contentDescription = text) },
        text = {
            Text(
                text = text,
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp
            )
        }
    )
}