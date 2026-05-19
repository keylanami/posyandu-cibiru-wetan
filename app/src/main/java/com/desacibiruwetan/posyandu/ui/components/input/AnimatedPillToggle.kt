package com.desacibiruwetan.posyandu.ui.components.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen

@Composable
fun AnimatedPillToggle(
    isYes: Boolean,
    onToggle: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val offset by animateDpAsState(
        targetValue = if (isYes) 0.dp else 55.dp,
        animationSpec = tween(durationMillis = 300),
        label = "ToggleOffset"
    )

    val textColorYes by animateColorAsState(
        if (isYes) Color.White else Color(0xFF272727),
        label = "TextYes"
    )
    val textColorNo by animateColorAsState(
        if (!isYes) Color.White else Color(0xFF272727),
        label = "TextNo"
    )

    Box(
        modifier = modifier
            .width(119.dp)
            .height(30.dp)
            .background(color = Color(0xFFEAFAF5), shape = RoundedCornerShape(100.dp))
            .clickable { onToggle(!isYes) }
            .padding(5.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Box(
            modifier = Modifier
                .offset(x = offset)
                .width(54.dp)
                .fillMaxHeight()
                .background(color = PrimaryGreen, shape = RoundedCornerShape(100.dp))
        )

        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "Ya",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp,
                    color = textColorYes
                )
            }
            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                Text(
                    text = "Tidak",
                    fontFamily = Inter,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 10.sp,
                    color = textColorNo
                )
            }
        }
    }
}