package com.desacibiruwetan.posyandu.ui.screen.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceMuted
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.DataReadViewModel
import com.desacibiruwetan.posyandu.viewmodel.ReadCollection
import com.desacibiruwetan.posyandu.viewmodel.ReadRecord

@Composable
fun RiwayatScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String,
    dataReadViewModel: DataReadViewModel
) {
    val context = LocalContext.current
    val token = SessionManager.getAuthorizationHeader(context)
    val readState by dataReadViewModel.readState.collectAsState()

    LaunchedEffect(token) {
        if (token.isNotBlank()) dataReadViewModel.refresh(token)
    }

    val logs = when (readState) {
        is UiState.Success -> (readState as UiState.Success<List<ReadCollection>>).data.firstOrNull { it.key == "logs" }
        else -> null
    }

    Scaffold(
        topBar = { AppTopBar(title = "Riwayat", onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 2, onItemSelected = onNavItemSelected) },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Spacer(modifier = Modifier.height(6.dp))
            ActivityHeader(
                userName = userName,
                total = logs?.count ?: 0,
                onRefresh = { if (token.isNotBlank()) dataReadViewModel.refresh(token) }
            )

            when (readState) {
                UiState.Idle, UiState.Loading -> {
                    LoadingCard()
                }

                is UiState.Error -> {
                    InfoCard("Riwayat belum bisa dimuat", (readState as UiState.Error).message)
                }

                is UiState.Success -> {
                    val records = logs?.records.orEmpty()
                    if (records.isEmpty()) {
                        EmptyState(icon = Icons.Default.History, message = "Belum ada riwayat aktivitas")
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(records) { item ->
                                ActivityCard(item)
                            }
                            item { Spacer(modifier = Modifier.height(82.dp)) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityHeader(userName: String, total: Int, onRefresh: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(SurfaceWhite.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = SurfaceWhite)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text("Log aktivitas", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SurfaceWhite)
                Text("$userName - $total catatan", style = MaterialTheme.typography.bodySmall, color = SurfaceWhite.copy(alpha = 0.76f))
            }
            IconButton(onClick = onRefresh) {
                Icon(Icons.Default.Refresh, contentDescription = "Sinkronisasi", tint = SurfaceWhite)
            }
        }
    }
}

@Composable
private fun ActivityCard(item: ReadRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(20.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(FreshTeal, RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.History, contentDescription = null, tint = PrimaryGreen)
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(item.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(item.subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Text(item.meta, style = MaterialTheme.typography.labelMedium, color = TextMuted)
            if (item.details.isNotEmpty()) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp)
                        .background(SurfaceMuted, RoundedCornerShape(14.dp))
                        .padding(10.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    item.details.take(7).forEach { (label, value) ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelSmall,
                                color = TextMuted,
                                modifier = Modifier.weight(0.42f)
                            )
                            Text(
                                text = value,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.weight(0.58f)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingCard() {
    InfoCard("Memuat riwayat", "Mengambil catatan aktivitas terbaru saat jaringan tersedia.")
}

@Composable
private fun InfoCard(title: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(20.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
    }
}
