package com.desacibiruwetan.posyandu.ui.screen.profile

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.R
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.viewmodel.AuthViewmodel

@Composable
fun ProfilScreen(
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    authViewModel: AuthViewmodel
) {
    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE) }
    val token = remember { sharedPreferences.getString("TOKEN", "") ?: "" }

    val getMeState by authViewModel.getMeState.collectAsState()

    var usernameDisplay by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noTelepon by remember { mutableStateOf("") }
    var passwordBaru by remember { mutableStateOf("") }
    var passwordLama by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (token.isNotEmpty()) {
            authViewModel.getMe("Bearer $token")
        }
    }

    LaunchedEffect(getMeState) {
        if (getMeState is UiState.Success) {
            val user = (getMeState as UiState.Success).data.data
            if (user != null) {
                email = user.email
                usernameDisplay = if (email.contains("@")) email.substringBefore("@") else email
                noTelepon = user.phoneNumber
            }
        }
    }

    Scaffold(
        containerColor = BgMint,
        bottomBar = {
            AppNavBar(selectedIndex = 3, onItemSelected = onNavItemSelected)
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = paddingValues.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .background(PrimaryGreen)
                    .padding(start = 12.dp, end = 12.dp, bottom = 16.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Profile Mini",
                            tint = PrimaryGreen,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = if (usernameDisplay.isNotEmpty()) usernameDisplay else "Kader",
                        fontFamily = Inter,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(32.dp))

                Box(
                    modifier = Modifier
                        .size(145.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img),
                        contentDescription = "Foto Profil",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (usernameDisplay.isNotEmpty()) usernameDisplay else "Memuat...",
                    fontFamily = Inter,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF272727)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                FormSectionCard(title = "Identitas Utama") {
                    AppTextField(
                        label = "Username (Email)",
                        value = email,
                        readOnly = true,
                        placeholder = "Masukkan email",
                        onValueChange = { }
                    )

                    AppTextField(
                        label = "Nomor Telepon",
                        value = noTelepon,
                        placeholder = "Masukkan nomor telepon",
                        keyboardType = KeyboardType.Phone,
                        onValueChange = { noTelepon = it }
                    )

                    AppTextField(
                        label = "Ganti Password",
                        value = passwordBaru,
                        placeholder = "Masukkan password baru",
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = { passwordBaru = it }
                    )

                    AppTextField(
                        label = "Password Lama",
                        value = passwordLama,
                        placeholder = "Masukkan password lama",
                        visualTransformation = PasswordVisualTransformation(),
                        onValueChange = { passwordLama = it }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(
                    text = "Update Data",
                    icon = Icons.Default.AddCircleOutline,
                    onClick = { /* TODO: update data */ }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onLogoutClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9F413D)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "Keluar",
                        fontFamily = Inter,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.height(80.dp))
            }
        }
    }
}
