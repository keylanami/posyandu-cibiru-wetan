package com.desacibiruwetan.posyandu.ui.screen.profile

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveThreeColumn
import com.desacibiruwetan.posyandu.ui.components.layout.ResponsiveTwoColumn
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.ErrorDark
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AuthViewmodel

@Composable
fun ProfilScreen(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    authViewModel: AuthViewmodel
) {
    val context = LocalContext.current
    val token = remember { SessionManager.getRawToken(context) }
    val getMeState by authViewModel.getMeState.collectAsState()

    var rt by remember { mutableStateOf("-") }
    var rw by remember { mutableStateOf("-") }
    var usernameDisplay by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noTelepon by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (token.isNotEmpty()) authViewModel.getMe(token)
    }

    LaunchedEffect(getMeState) {
        if (getMeState is UiState.Success) {
            val user = (getMeState as UiState.Success).data.data
            if (user != null) {
                email = user.email ?: ""
                usernameDisplay = if (email.contains("@")) email.substringBefore("@") else email
                noTelepon = user.phoneNumber ?: ""
                rt = user.rt ?: "-"
                rw = user.rw ?: "-"
            }
        }
    }

    Scaffold(
        containerColor = BgMint,
        bottomBar = { AppNavBar(selectedIndex = 3, onItemSelected = onNavItemSelected) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .responsiveScreenPadding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileHero(
                name = if (usernameDisplay.isNotEmpty()) usernameDisplay else "Kader",
                email = email.ifEmpty { "Memuat akun..." },
                rt = rt,
                rw = rw
            )

            ProfileSection(title = "Kontak kader") {
                AppTextField(
                    label = "Email",
                    value = email,
                    readOnly = true,
                    onValueChange = {}
                )
                AppTextField(
                    label = "Nomor Telepon",
                    value = noTelepon,
                    readOnly = true,
                    placeholder = "Belum ada nomor telepon",
                    onValueChange = {}
                )
                ResponsiveTwoColumn(
                    first = { itemModifier ->
                    Box(modifier = itemModifier) {
                        AppTextField(label = "RT", value = rt, readOnly = true, onValueChange = {})
                    } },
                    second = { itemModifier ->
                    Box(modifier = itemModifier) {
                        AppTextField(label = "RW", value = rw, readOnly = true, onValueChange = {})
                    } }
                )
            }

            Button(
                onClick = onLogoutClick,
                modifier = Modifier.fillMaxWidth().heightIn(min = 52.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ErrorDark),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null, modifier = Modifier.size(19.dp))
                Spacer(modifier = Modifier.size(8.dp))
                Text("Keluar", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(64.dp))
        }
    }
}

@Composable
private fun ProfileHero(name: String, email: String, rt: String, rw: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(DeepGreen, RoundedCornerShape(28.dp))
            .padding(20.dp)
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier.size(54.dp).background(SurfaceWhite.copy(alpha = 0.12f), RoundedCornerShape(18.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Person, contentDescription = null, tint = SurfaceWhite, modifier = Modifier.size(30.dp))
                }
                Column(modifier = Modifier.weight(1f).padding(start = 14.dp)) {
                    Text(name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = SurfaceWhite)
                    Text(email, style = MaterialTheme.typography.bodyMedium, color = SurfaceWhite.copy(alpha = 0.76f))
                }
            }
            ResponsiveThreeColumn(
                first = { AreaPill("RT", rt, it) },
                second = { AreaPill("RW", rw, it) },
                third = { AreaPill("Role", "Kader", it) }
            )
        }
    }
}

@Composable
private fun AreaPill(label: String, value: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.background(SurfaceWhite.copy(alpha = 0.1f), RoundedCornerShape(16.dp)).padding(12.dp)
    ) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = SurfaceWhite.copy(alpha = 0.68f))
        Text(value, style = MaterialTheme.typography.labelLarge, color = SurfaceWhite, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun ProfileSection(title: String, content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SurfaceWhite, RoundedCornerShape(22.dp))
            .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(38.dp).background(FreshTeal, RoundedCornerShape(13.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = PrimaryGreen, modifier = Modifier.size(21.dp))
            }
            Column(modifier = Modifier.padding(start = 10.dp)) {
                Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
                Text("Data akun dan wilayah kerja", style = MaterialTheme.typography.bodySmall, color = TextMuted)
            }
        }
        content()
    }
}
