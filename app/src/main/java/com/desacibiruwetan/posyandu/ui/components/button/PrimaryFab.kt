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
import com.desacibiruwetan.posyandu.ui.theme.ActionAmber
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.Inter

@Composable
fun PrimaryFab(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    containerColor: Color = ActionAmber
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        containerColor = containerColor,
        contentColor = Color.White,
        shape = RoundedCornerShape(16.dp),
        icon = { Icon(imageVector = icon, contentDescription = text) },
        text = {
            Text(
                text = text,
                fontFamily = Inter,
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Color.White
            )
        }
    )
}
