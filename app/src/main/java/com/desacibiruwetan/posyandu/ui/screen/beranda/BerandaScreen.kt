package com.desacibiruwetan.posyandu.ui.screen.beranda

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.navigation.Screen
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.button.LargeActionCard
import com.desacibiruwetan.posyandu.ui.components.button.SmallActionCard
import com.desacibiruwetan.posyandu.ui.components.dialog.PilotSelectionDialog
import com.desacibiruwetan.posyandu.ui.components.items.RecentHistoryItem
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen

@Composable
fun DashboardScreen(
    onNavigateToCariWarga: () -> Unit,
    onNavigateToCatatKejadian: () -> Unit,
    onNavigateToUpdateKb: () -> Unit,
    onNavigateToUpdateWusPus: () -> Unit,
    onNavigateToUpdateBalita: () -> Unit,
    onNavigateToAdministrasiRt: () -> Unit,
    onNavigateToBumil: () -> Unit,
    onNavigateToRumahKeluarga: () -> Unit,
    onNavigateToPilot: (String) -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    var showPilotDialog by remember { mutableStateOf(false) }

    if (showPilotDialog) {
        PilotSelectionDialog(
            onDismiss = { showPilotDialog = false },
            onOptionSelected = { option ->
                println("Pilot terpilih: $option")
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
                if (route.isNotEmpty()) {
                    onNavigateToPilot(route)
                }
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
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(179.dp)
                        .background(
                            color = PrimaryGreen,
                            shape = RoundedCornerShape(bottomStart = 64.5.dp, bottomEnd = 64.5.dp)
                        )
                )

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 24.dp, end = 24.dp, top = 40.dp, bottom = 24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(Color.White),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = PrimaryGreen,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = "Joan",
                            fontFamily = Inter,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .shadow(
                                16.dp,
                                spotColor = Color(0x40DFDFDF),
                                shape = RoundedCornerShape(15.dp)
                            )
                            .background(Color.White, RoundedCornerShape(15.dp))
                            .border(1.dp, Color(0xFFE9E9E9), RoundedCornerShape(15.dp))
                            .padding(24.dp)
                    ) {
                        Column {
                            Text(
                                text = "Cari & Lihat Data",
                                fontFamily = Inter,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = Color(0xFF272727)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                LargeActionCard(
                                    title = "Cari Warga",
                                    icon = Icons.Default.People,
                                    iconBgColor = Color(0xFFC7FFEC),
                                    onClick = onNavigateToCariWarga,
                                    modifier = Modifier.weight(1f)
                                )
                                LargeActionCard(
                                    title = "Rumah & Keluarga",
                                    icon = Icons.Default.Home,
                                    iconBgColor = Color(0xFFC7FFEC),
                                    onClick = onNavigateToRumahKeluarga,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Text(
                text = "Perbarui Data",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF272727)
            )
            Spacer(modifier = Modifier.height(12.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SmallActionCard(
                    "Catat Kejadian",
                    Icons.Default.Edit,
                    Color(0xFFFFFFC7),
                    onClick = onNavigateToCatatKejadian,
                    modifier = Modifier.weight(1f)
                )
                SmallActionCard(
                    "Wus/Pus",
                    Icons.Default.Favorite,
                    Color(0xFFD6E4FF),
                    onClick = onNavigateToUpdateWusPus,
                    modifier = Modifier.weight(1f)
                )
                SmallActionCard(
                    "Bumil",
                    Icons.Default.Face,
                    Color(0xFFFFD6E4),
                    onClick = onNavigateToBumil,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SmallActionCard(
                    "Balita",
                    Icons.Default.ChildCare,
                    Color(0xFFC7FFEC),
                    onClick = onNavigateToUpdateBalita,
                    modifier = Modifier.weight(1f)
                )
                SmallActionCard(
                    "KB",
                    Icons.Default.People,
                    Color(0xFFFFFFC7),
                    onClick = onNavigateToUpdateKb,
                    modifier = Modifier.weight(1f)
                )
                SmallActionCard(
                    "Administrasi RT",
                    Icons.Default.Settings,
                    Color(0xFFD6E4FF),
                    onClick = onNavigateToAdministrasiRt,
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SmallActionCard(
                    "Pilot",
                    Icons.Default.Face,
                    Color(0xFFC7FFEC),
                    onClick = { showPilotDialog = true },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.weight(2f))
            }

            Spacer(modifier = Modifier.height(32.dp))





            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(123.dp)
                    .background(
                        Color(0xFF1B9E75),
                        RoundedCornerShape(15.dp)
                    )
                    .padding(24.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Column {
                    Text(
                        text = "Data akurat, warga sejahtera",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Ayo jaga dan tingkatkan kesejahteraan warga bersama-sama",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Normal,
                        fontSize = 12.sp,
                        color = Color.White
                    )
                }
            }





            Spacer(modifier = Modifier.height(32.dp))





            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF4FAF8), RoundedCornerShape(15.dp))
                    .border(1.dp, Color(0xFFE9E9E9), RoundedCornerShape(15.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        text = "Riwayat Terbaru",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = Color(0xFF272727)
                    )
                    Spacer(modifier = Modifier.height(16.dp))

                    RecentHistoryItem(
                        title = "Catat kejadian",
                        subtitle = "Kelahiran - Sumarsih",
                        date = "10 Mei 2026",
                        icon = Icons.AutoMirrored.Filled.Assignment,
                        iconBgColor = Color(0xFFFFFFC7)
                    )
                    RecentHistoryItem(
                        title = "Catat kejadian",
                        subtitle = "Wafat - Mulyodawg",
                        date = "11 Mei 2026",
                        icon = Icons.AutoMirrored.Filled.Assignment,
                        iconBgColor = Color(0xFFFFFFC7)
                    )
                }
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
