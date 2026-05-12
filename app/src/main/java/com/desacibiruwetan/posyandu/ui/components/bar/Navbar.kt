package com.desacibiruwetan.posyandu.ui.components.bar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarDefaults
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.Poppins
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen

@Composable
fun AppNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    val items = listOf("Beranda", "Warga", "Riwayat", "Profil")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.People,
        Icons.Default.History,
        Icons.Default.Person
    )

    val systemBgColor = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        systemBgColor.copy(alpha = 0.85f),
                        systemBgColor
                    )
                )
            )
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            windowInsets = NavigationBarDefaults.windowInsets,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = selectedIndex == index

                NavigationBarItem(
                    selected = isSelected,
                    onClick = { onItemSelected(index) },
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