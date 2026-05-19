package com.desacibiruwetan.posyandu.ui.screen.beranda

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar // <-- Import Reusable Component
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.Poppins
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextDark

val LightMintIconBg = Color(0xFFC7FFEC)

// ─── Data ────────────────────────────────────────────────────────────────────

data class ServiceData(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit = {}
)

data class ServiceGroup(val groupLabel: String, val items: List<ServiceData>)

// ─── Screen ──────────────────────────────────────────────────────────────────

@Composable
fun DashboardScreen(
    onNavigateToCariWarga: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    onNavigateToCatatKejadian: () -> Unit,
    onNavigateToUpdateKb: () -> Unit,
    onNavigateToUpdateWusPus: () -> Unit,
    onNavigateToUpdateBalita: () -> Unit,
) {
    Scaffold(
        containerColor = BgMint,
        bottomBar = {
            AppNavBar(
                selectedIndex = 0,
                onItemSelected = onNavItemSelected
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
        ) {
            DashboardTopBar()

            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                item { Spacer(modifier = Modifier.height(20.dp)) }
                item { WelcomeBanner() }
                item { Spacer(modifier = Modifier.height(28.dp)) }
                item {
                    Text(
                        text = "Layanan",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = TextDark
                    )
                }
                item { Spacer(modifier = Modifier.height(16.dp)) }

                val serviceGroups = listOf(
                    ServiceGroup(
                        "Cari & Lihat Data",
                        listOf(
                            ServiceData(
                                title = "Cari Warga",
                                description = "Temukan data warga berdasarkan nama atau NIK",
                                icon = Icons.Default.Search,
                                onClick = onNavigateToCariWarga
                            ),
                            ServiceData(
                                title = "Rumah & Keluarga",
                                description = "Lihat data anggota keluarga per rumah tangga",
                                icon = Icons.Default.Home
                            )
                        )
                    ),
                    ServiceGroup(
                        "Catat & Perbarui",
                        listOf(
                            ServiceData(
                                "Catat Kejadian",
                                "Rekam kelahiran, kematian, atau kejadian penting",
                                Icons.Default.Edit,
                                onNavigateToCatatKejadian
                            ),
                            ServiceData(
                                "Update KB",
                                "Perbarui status penggunaan KB warga",
                                Icons.Default.People,
                                onNavigateToUpdateKb
                            ),
                            ServiceData(
                                "Wus / Pus",
                                "Data wanita dan pasangan usia subur",
                                Icons.Default.Favorite,
                                onNavigateToUpdateWusPus
                            ),
                            ServiceData(
                                "Update Bumil",
                                "Catat perkembangan ibu hamil di wilayah Anda",
                                Icons.Default.Face,
                            ),
                            ServiceData(
                                "Update Balita",
                                "Input tumbuh kembang dan gizi balita",
                                Icons.Default.Face,
                                onNavigateToUpdateBalita
                            )
                        )
                    ),
                    ServiceGroup(
                        "Administrasi",
                        listOf(
                            ServiceData(
                                "Administrasi RT",
                                "Kelola surat dan dokumen administrasi RT",
                                Icons.Default.Settings
                            )
                        )
                    )
                )

                items(serviceGroups) { group ->
                    ServiceGroupSection(group = group)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item { Spacer(modifier = Modifier.height(32.dp)) }
            }
        }
    }
}

// ─── Top Bar Khusus Dashboard ────────────────────────────────────────────────

@Composable
fun DashboardTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(color = PrimaryGreen)
            .statusBarsPadding()
            .padding(horizontal = 24.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(SurfaceWhite),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Profile Picture",
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
            color = SurfaceWhite
        )
    }
}

// ─── Welcome Banner ───────────────────────────────────────────────────────────

@Composable
fun WelcomeBanner() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(173.dp)
            .background(color = Color(0xFF16805E), shape = RoundedCornerShape(15.dp))
            .padding(24.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Column {
            Text(
                text = "Selamat Datang, Joan!",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = SurfaceWhite
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Ayo jaga dan tingkatkan kesejahteraan warga bersama-sama",
                fontFamily = Inter,
                fontWeight = FontWeight.Medium,
                fontSize = 15.sp,
                color = SurfaceWhite
            )
        }
    }
}

// ─── Service Group Section ────────────────────────────────────────────────────

@Composable
fun ServiceGroupSection(group: ServiceGroup) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = group.groupLabel.uppercase(),
            fontFamily = Poppins,
            fontWeight = FontWeight.Bold,
            fontSize = 11.sp,
            color = TextDark.copy(alpha = 0.45f),
            letterSpacing = 1.2.sp
        )
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            group.items.forEach { service ->
                ServiceRow(service = service)
            }
        }
    }
}

// ─── Service Row ─────────────────────────────────────────────────────────────

@Composable
fun ServiceRow(service: ServiceData) {
    Row(
        modifier = Modifier
            .shadow(
                elevation = 6.dp,
                spotColor = Color(0x30DFDFDF),
                ambientColor = Color(0x30DFDFDF),
                shape = RoundedCornerShape(12.dp)
            )
            .background(color = PrimaryGreen, shape = RoundedCornerShape(12.dp))
            .fillMaxWidth()
            .clickable { service.onClick() }
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(color = LightMintIconBg),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = service.icon,
                contentDescription = service.title,
                tint = PrimaryGreen,
                modifier = Modifier.size(24.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = service.title,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp,
                color = SurfaceWhite
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = service.description,
                fontFamily = Poppins,
                fontWeight = FontWeight.Normal,
                fontSize = 11.sp,
                color = SurfaceWhite.copy(alpha = 0.75f),
                lineHeight = 15.sp
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForwardIos,
            contentDescription = null,
            tint = SurfaceWhite.copy(alpha = 0.4f),
            modifier = Modifier.size(14.dp)
        )
    }
}