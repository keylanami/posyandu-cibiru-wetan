package com.desacibiruwetan.posyandu.ui.screen.beranda

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.PregnantWoman
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.navigation.Screen
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.dialog.PilotSelectionDialog
import com.desacibiruwetan.posyandu.ui.components.items.SyncStatusBadge
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveThreeColumn
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.ActionAmber
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.HistorySlate
import com.desacibiruwetan.posyandu.ui.theme.HealthBlue
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.ProgramPurple
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.viewmodel.DataReadViewModel
import com.desacibiruwetan.posyandu.viewmodel.ReadCollection
import com.desacibiruwetan.posyandu.viewmodel.ReadRecord

@Composable
fun DashboardScreen(
    onNavigateToCariWarga: () -> Unit,
    onNavigateToCatatKejadian: () -> Unit,
    onNavigateToAdministrasiRt: () -> Unit,
    onNavigateToRumahKeluarga: () -> Unit,
    onNavigateToPilot: (String) -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String,
    activeRtRw: String,
    dataReadViewModel: DataReadViewModel,
    updateViewModel: com.desacibiruwetan.posyandu.viewmodel.UpdateViewmodel
) {
    val context = LocalContext.current
    var showPilotDialog by remember { mutableStateOf(false) }
    val updateInfo by updateViewModel.updateInfo.collectAsState()

    LaunchedEffect(Unit) {
        updateViewModel.checkForUpdate()
    }

    if (updateInfo != null) {
        com.desacibiruwetan.posyandu.ui.components.dialog.UpdateDialog(
            updateInfo = updateInfo!!,
            onDismiss = { if (!updateInfo!!.forceUpdate) updateViewModel.clearUpdateInfo() },
            onUpdateClick = {
                context.findActivity()?.let { activity ->
                    updateViewModel.startPlayUpdate(activity)
                }
                if (!updateInfo!!.forceUpdate) updateViewModel.clearUpdateInfo()
            }
        )
    }
    val isOnline = rememberOnlineStatus()
    val readState by dataReadViewModel.readState.collectAsState()
    val recentActivities = ((readState as? UiState.Success<List<ReadCollection>>)
        ?.data
        ?.firstOrNull { it.key == "logs" }
        ?.records)
        .orEmpty()
        .take(2)

    if (showPilotDialog) {
        PilotSelectionDialog(
            onDismiss = { showPilotDialog = false },
            onOptionSelected = { option ->
                showPilotDialog = false
                val route = when (option) {
                    "Peduli Stunting" -> Screen.PilotStunting.route
                    "PHBS" -> Screen.PilotPhbs.route
                    "Kesehatan Ibu & Anak" -> Screen.PilotKia.route
                    "Siaga Kebakaran" -> Screen.PilotKebakaran.route
                    "Bencana Alam" -> Screen.PilotBencana.route
                    "Peduli Lingkungan" -> Screen.PilotLingkungan.route
                    "Keluarga Sehat Berkualitas" -> Screen.PilotKeluargaSehat.route
                    "Keuangan Sehat" -> Screen.PilotKeuangan.route
                    "Kesehatan PUS" -> Screen.PilotKesehatanPus.route
                    else -> ""
                }
                if (route.isNotEmpty()) onNavigateToPilot(route)
            }
        )
    }

    Scaffold(
        containerColor = BgMint,
        bottomBar = { AppNavBar(selectedIndex = 0, onItemSelected = onNavItemSelected) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            WorkHeader(userName = userName, activeRtRw = activeRtRw, isOnline = isOnline)

            Column(
                modifier = Modifier.responsiveScreenPadding(vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                PrimaryWorkflow(
                    onSearch = onNavigateToCariWarga,
                    onAdd = onNavigateToRumahKeluarga,
                    onEvent = onNavigateToCatatKejadian
                )

                SectionHeader("Wilayah & program", "Administrasi wilayah dan indikator program")
                ResponsiveTwoColumn(
                    first = { itemModifier -> CompactWorkCard("Administrasi", Icons.Default.Groups, ActionAmber, onNavigateToAdministrasiRt, itemModifier) },
                    second = { itemModifier -> CompactWorkCard("Program", Icons.Default.HealthAndSafety, ProgramPurple, { showPilotDialog = true }, itemModifier) }
                )

                SectionHeader("Aktivitas terakhir", "Ringkas untuk membantu kader melanjutkan pekerjaan")
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (recentActivities.isEmpty()) {
                        EmptyActivityCard()
                    } else {
                        recentActivities.forEach { item ->
                            ActivityRow(item)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(54.dp))
            }
        }
    }
}

@Composable
private fun WorkHeader(userName: String, activeRtRw: String, isOnline: Boolean) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                DeepGreen,
                RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
            )
            .responsiveScreenPadding(vertical = 20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "GKSTTB Cibiru Wetan",
                        style = MaterialTheme.typography.labelLarge,
                        color = SurfaceWhite.copy(alpha = 0.78f)
                    )
                    Text(
                        text = "Selamat bertugas, $userName",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = SurfaceWhite
                    )
                }
                SyncStatusBadge(isOnline = isOnline)
            }
            ResponsiveTwoColumn(
                first = { itemModifier -> HeaderPill("RT/RW aktif", activeRtRw, itemModifier) },
                second = { itemModifier -> HeaderPill("Mode kerja", "Input cepat", itemModifier) }
            )
        }
    }
}

@Composable
private fun HeaderPill(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(Color.White.copy(alpha = 0.1f), RoundedCornerShape(18.dp))
            .padding(horizontal = 14.dp, vertical = 12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = SurfaceWhite.copy(alpha = 0.68f))
        Text(value, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold, color = SurfaceWhite)
    }
}

@Composable
private fun MetricTile(title: String, value: String, icon: ImageVector, color: Color, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(SurfaceWhite, RoundedCornerShape(20.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(20.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.size(34.dp).background(color.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(19.dp))
        }
        Text(title, style = MaterialTheme.typography.labelMedium, color = TextMuted)
        Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun PrimaryWorkflow(onSearch: () -> Unit, onAdd: () -> Unit, onEvent: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(24.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(24.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Navigasi Utama", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
        ResponsiveThreeColumn(
            first = { itemModifier -> WorkflowButton("Pusat", "data", Icons.Default.Search, PrimaryGreen, onSearch, itemModifier) },
                second = { itemModifier -> WorkflowButton("Manajemen", "Rumah dan KK", Icons.Default.Add, HealthBlue, onAdd, itemModifier) },
            third = { itemModifier -> WorkflowButton("Catat", "kejadian", Icons.Default.EditNote, ActionAmber, onEvent, itemModifier) }
        )
    }
}

@Composable
private fun WorkflowButton(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color.copy(alpha = 0.14f), RoundedCornerShape(18.dp))
            .border(1.dp, color.copy(alpha = 0.22f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(24.dp))
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.labelSmall, color = TextMuted)
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
    }
}

@Composable
private fun WorkRow(title: String, subtitle: String, icon: ImageVector, color: Color, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(18.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(44.dp).background(color.copy(alpha = 0.12f), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(23.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = TextMuted)
        }
        Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null, tint = TextMuted)
    }
}

@Composable
private fun CompactWorkCard(title: String, icon: ImageVector, color: Color, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(color.copy(alpha = 0.10f), RoundedCornerShape(18.dp))
            .border(1.dp, color.copy(alpha = 0.20f), RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(icon, contentDescription = null, tint = color)
        Text(title, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ActivityRow(item: ReadRecord) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(18.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(38.dp).background(HistorySlate.copy(alpha = 0.12f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.AutoMirrored.Filled.Assignment, contentDescription = null, tint = HistorySlate)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.labelMedium,
                color = TextMuted,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
        Text(
            text = item.meta,
            style = MaterialTheme.typography.labelSmall,
            color = TextMuted,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EmptyActivityCard() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(18.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(18.dp))
            .padding(14.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text("Belum ada aktivitas", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
        Text("Riwayat terbaru akan tampil di sini setelah data tersinkron.", style = MaterialTheme.typography.bodySmall, color = TextMuted)
    }
}

@Composable
private fun rememberOnlineStatus(): Boolean {
    val context = LocalContext.current
    val connectivityManager = remember(context) {
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    var isOnline by remember {
        mutableStateOf(connectivityManager.isConnectedToInternet())
    }
    val mainHandler = remember { Handler(Looper.getMainLooper()) }

    DisposableEffect(connectivityManager) {
        fun updateOnline(value: Boolean) {
            mainHandler.post { isOnline = value }
        }

        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                updateOnline(connectivityManager.isConnectedToInternet())
            }

            override fun onLost(network: Network) {
                updateOnline(connectivityManager.isConnectedToInternet())
            }

            override fun onCapabilitiesChanged(network: Network, networkCapabilities: NetworkCapabilities) {
                updateOnline(
                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
                )
            }
        }

        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .build()
        connectivityManager.registerNetworkCallback(request, callback)
        onDispose {
            runCatching { connectivityManager.unregisterNetworkCallback(callback) }
        }
    }

    return isOnline
}

private fun ConnectivityManager.isConnectedToInternet(): Boolean {
    val network = activeNetwork ?: return false
    val capabilities = getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}
