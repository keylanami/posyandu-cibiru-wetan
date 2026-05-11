package com.desacibiruwetan.posyandu.ui.screen.beranda

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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

// Warna tambahan khusus Dashboard
val InactiveNavColor = Color(0xFFD2D2D2)
val LightMintIconBg = Color(0xFFC7FFEC)

@Composable
fun DashboardScreen() {
    Scaffold(
        topBar = { DashboardTopBar() },
        bottomBar = { DashboardBottomBar() },
        containerColor = BgMint
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            // Banner Selamat Datang
            WelcomeBanner()

            Spacer(modifier = Modifier.height(24.dp))

            // Judul Layanan
            Text(
                text = "Layanan",
                fontFamily = Inter,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = TextDark
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Grid Menu Layanan
            ServiceGrid()
        }
    }
}

@Composable
fun DashboardTopBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color = PrimaryGreen)
            .padding(horizontal = 24.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Placeholder untuk Foto Profil
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

    NavigationBar(
        containerColor = PrimaryGreen,
        contentColor = SurfaceWhite,
        modifier = Modifier.height(84.dp)
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selectedItem == index,
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
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = SurfaceWhite,
                    unselectedIconColor = InactiveNavColor,
                    selectedTextColor = SurfaceWhite,
                    unselectedTextColor = InactiveNavColor,
                    indicatorColor = PrimaryGreen
                )
            )
        }
    }
}

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

@Composable
fun ServiceGrid() {
    val services = listOf(
        ServiceData("Cari Warga", Icons.Default.Search),
        ServiceData("Rumah & Keluarga", Icons.Default.Home),
        ServiceData("Update KB", Icons.Default.People),
        ServiceData("Wus / Pus", Icons.Default.Favorite),
        ServiceData("Catat Kejadian", Icons.Default.Edit),
        ServiceData("Update Bumil", Icons.Default.Face),
        ServiceData("Update Balita", Icons.Default.Face),
        ServiceData("Administrasi RT", Icons.Default.Settings)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(services) { service ->
            ServiceCard(service = service)
        }
    }
}

@Composable
fun ServiceCard(service: ServiceData) {
    Box(
        modifier = Modifier
            .shadow(
                elevation = 16.6.dp,
                spotColor = Color(0x40DFDFDF),
                ambientColor = Color(0x40DFDFDF),
                shape = RoundedCornerShape(10.dp)
            )
            .background(color = PrimaryGreen, shape = RoundedCornerShape(10.dp))
            .height(129.dp)
            .fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(59.dp)
                    .clip(CircleShape)
                    .background(color = LightMintIconBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = service.icon,
                    contentDescription = service.title,
                    tint = PrimaryGreen,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))


            Text(
                text = service.title,
                fontFamily = Poppins,
                fontWeight = FontWeight.Medium,
                fontSize = 12.sp,
                color = SurfaceWhite
            )
        }
    }
}


data class ServiceData(
    val title: String,
    val icon: ImageVector
)