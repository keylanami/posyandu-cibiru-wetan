package com.desacibiruwetan.posyandu.ui.screen.riwayat

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.AddTask
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.theme.ActionAmber
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.HealthBlue
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted

private data class ActivityItem(
    val type: String,
    val title: String,
    val description: String,
    val location: String,
    val time: String,
    val icon: ImageVector,
    val color: Color
)

@Composable
fun RiwayatScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {
    var selectedFilter by remember { mutableStateOf("Semua") }
    val filters = listOf("Semua", "Warga", "Kesehatan", "Kejadian")
    val activities = remember {
        listOf(
            ActivityItem(
                type = "Kesehatan",
                title = "Data KB diperbarui",
                description = "Status metode kontrasepsi IUD pada kunjungan rutin",
                location = "RT04 / RW02",
                time = "08:45",
                icon = Icons.Default.HealthAndSafety,
                color = PrimaryGreen
            ),
            ActivityItem(
                type = "Warga",
                title = "Pendaftaran warga baru",
                description = "Data anggota berhasil ditambahkan ke database kader",
                location = "RT04 / RW02",
                time = "Kemarin",
                icon = Icons.Default.Groups,
                color = HealthBlue
            ),
            ActivityItem(
                type = "Kejadian",
                title = "Catatan kejadian keluarga",
                description = "Perubahan status keluarga dicatat untuk tindak lanjut RT",
                location = "RT04 / RW02",
                time = "28 Februari",
                icon = Icons.Default.AddTask,
                color = ActionAmber
            )
        )
    }
    val filteredActivities = remember(selectedFilter, activities) {
        if (selectedFilter == "Semua") activities else activities.filter { it.type == selectedFilter }
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
            ActivityHeader(userName = userName, total = activities.size)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(22.dp))
                    .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
                    .padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.FilterList, contentDescription = null, tint = PrimaryGreen)
                    Text(
                        text = "Filter aktivitas",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEach { filter ->
                        FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = FreshTeal,
                                selectedLabelColor = PrimaryGreen
                            )
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(filteredActivities) { item ->
                    ActivityCard(item)
                }
                item { Spacer(modifier = Modifier.height(82.dp)) }
            }
        }
    }
}

@Composable
private fun ActivityHeader(userName: String, total: Int) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(24.dp))
            .padding(18.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(48.dp).background(SurfaceWhite.copy(alpha = 0.12f), RoundedCornerShape(16.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = SurfaceWhite)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                Text("Aktivitas kader", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = SurfaceWhite)
                Text("$userName - $total catatan terbaru", style = MaterialTheme.typography.bodySmall, color = SurfaceWhite.copy(alpha = 0.76f))
            }
        }
    }
}

@Composable
private fun ActivityCard(item: ActivityItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(20.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier.size(44.dp).background(item.color.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(item.icon, contentDescription = null, tint = item.color)
        }
        Column(modifier = Modifier.weight(1f).padding(start = 12.dp), verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(item.type, style = MaterialTheme.typography.labelMedium, color = item.color, fontWeight = FontWeight.Bold)
                Text(" - ${item.time}", style = MaterialTheme.typography.labelMedium, color = TextMuted)
            }
            Text(item.title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(item.description, style = MaterialTheme.typography.bodySmall, color = TextMuted)
            Text(item.location, style = MaterialTheme.typography.labelMedium, color = TextMuted)
        }
    }
}
