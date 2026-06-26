package com.desacibiruwetan.posyandu.ui.components.items

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.theme.SyncOffline
import com.desacibiruwetan.posyandu.ui.theme.SyncOfflineContainer
import com.desacibiruwetan.posyandu.ui.theme.SyncOnline
import com.desacibiruwetan.posyandu.ui.theme.SyncOnlineContainer

@Composable
fun SyncStatusBadge(
    isOnline: Boolean,
    modifier: Modifier = Modifier
) {
    val color = if (isOnline) SyncOnline else SyncOffline
    val container = if (isOnline) SyncOnlineContainer else SyncOfflineContainer
    val text = if (isOnline) "Online" else "Offline"
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(100.dp))
            .background(container)
            .border(1.dp, color.copy(alpha = 0.36f), RoundedCornerShape(100.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}
