package com.desacibiruwetan.posyandu.ui.screen.beranda

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.Poppins
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextDark

val LightMintIconBg  = Color(0xFFC7FFEC)

// ─── Data ────────────────────────────────────────────────────────────────────

data class ServiceData(
    val title: String,
    val description: String,
    val icon: ImageVector
)

data class ServiceGroup(val groupLabel: String, val items: List<ServiceData>)

// ─── Screen ──────────────────────────────────────────────────────────────────

@Composable
fun DashboardScreen() {
    // Kita hapus inner padding bawah dari Scaffold agar navbar bisa overlay dengan efek transparan
    Scaffold(
        containerColor = BgMint,
        bottomBar = { DashboardBottomBar() }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding()) // Hanya terapkan bottom padding ke lazy column
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
                            ServiceData("Cari Warga", "Temukan data warga berdasarkan nama atau NIK", Icons.Default.Search),
                            ServiceData("Rumah & Keluarga", "Lihat data anggota keluarga per rumah tangga", Icons.Default.Home)
                        )
                    ),
                    ServiceGroup(
                        "Catat & Perbarui",
                        listOf(
                            ServiceData("Catat Kejadian", "Rekam kelahiran, kematian, atau kejadian penting", Icons.Default.Edit),
                            ServiceData("Update KB", "Perbarui status penggunaan KB warga", Icons.Default.People),
                            ServiceData("Wus / Pus", "Data wanita dan pasangan usia subur", Icons.Default.Favorite),
                            ServiceData("Update Bumil", "Catat perkembangan ibu hamil di wilayah Anda", Icons.Default.Face),
                            ServiceData("Update Balita", "Input tumbuh kembang dan gizi balita", Icons.Default.Face)
                        )
                    ),
                    ServiceGroup(
                        "Administrasi",
                        listOf(
                            ServiceData("Administrasi RT", "Kelola surat dan dokumen administrasi RT", Icons.Default.Settings)
                        )
                    )
                )

                items(serviceGroups) { group ->
                    ServiceGroupSection(group = group)
                    Spacer(modifier = Modifier.height(24.dp))
                }

                item { Spacer(modifier = Modifier.height(32.dp)) } // Ruang ekstra agar tidak tertutup gradient bottom bar
            }
        }
    }
}

// ─── Top Bar ─────────────────────────────────────────────────────────────────

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

// ─── Bottom Bar (Gradient Transparan) ────────────────────────────────────────

@Composable
fun DashboardBottomBar() {
    var selectedItem by remember { mutableIntStateOf(0) }

    val items = listOf("Beranda", "Warga", "Riwayat", "Profil")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.People,
        Icons.Default.History,
        Icons.Default.Person
    )

    // Menggunakan warna background system agar adaptif ke Light/Dark mode
    val systemBgColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent, // Atas transparan
                        systemBgColor.copy(alpha = 0.85f), // Tengah semi transparan
                        systemBgColor // Bawah solid mengikuti tema
                    )
                )
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent, // Dibuat transparan agar gradient Box terlihat
            windowInsets = NavigationBarDefaults.windowInsets, // Support edge-to-edge
            modifier = Modifier.padding(top = 16.dp) // Sedikit padding atas agar efek transparan di ujung atas terlihat natural
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedItem == index

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { selectedItem = index },
                    icon = {
                        Icon(
                            imageVector = icons[index],
                            contentDescription = item,
                            modifier = Modifier.size(26.dp)
                        )
                    },
                    label = {
                        Text(
                            text = item,
                            fontFamily = Poppins,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        // Warna diubah menjadi PrimaryGreen agar terlihat di atas background terang/gelap
                        selectedIconColor   = PrimaryGreen,
                        unselectedIconColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        selectedTextColor   = PrimaryGreen,
                        unselectedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                        indicatorColor      = PrimaryGreen.copy(alpha = 0.15f)
                    )
                )
            }
        }
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
fun ServiceRow(service: ServiceData, onClick: () -> Unit = {}) {
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
            .clickable { onClick() }
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