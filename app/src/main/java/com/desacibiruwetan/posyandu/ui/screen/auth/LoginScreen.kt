package com.desacibiruwetan.posyandu.ui.screen.auth

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppPasswordField
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.utils.SessionManager
import com.desacibiruwetan.posyandu.viewmodel.AuthViewmodel

@Composable
fun LoginScreenWrapper(
    onNavigateToRegister: () -> Unit,
    onNavigateToDashboard: () -> Unit,
    viewmodel: AuthViewmodel
) {
    val context = LocalContext.current
    val loginState by viewmodel.loginState.collectAsState()
    var serverError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(loginState) {
        when (loginState) {
            is UiState.Success -> {
                val loginData = (loginState as UiState.Success).data.data
                if (loginData != null) {
                    SessionManager.saveSession(context, loginData.token, loginData.user)
                }
                Toast.makeText(context, "Login Berhasil!", Toast.LENGTH_SHORT).show()
                viewmodel.resetLoginState()
                onNavigateToDashboard()
            }

            is UiState.Error -> {
                serverError = (loginState as UiState.Error).message
                viewmodel.resetLoginState()
            }

            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LoginScreen(
            isLoading = loginState is UiState.Loading,
            serverError = serverError,
            onNavigateToRegister = onNavigateToRegister,
            onLoginSubmit = { email, password ->
                serverError = null
                viewmodel.login(email, password, "Android Device")
            }
        )

        if (loginState is UiState.Loading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.30f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = SurfaceWhite)
            }
        }
    }
}

@Composable
fun LoginScreen(
    isLoading: Boolean,
    serverError: String?,
    onNavigateToRegister: () -> Unit,
    onLoginSubmit: (String, String) -> Unit
) {
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validateLogin() {
        var isValid = true
        if (emailText.isBlank()) {
            emailError = "Email tidak boleh kosong"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailError = "Format email tidak valid"
            isValid = false
        } else {
            emailError = null
        }

        if (passwordText.isBlank()) {
            passwordError = "Password tidak boleh kosong"
            isValid = false
        } else if (passwordText.length < 6) {
            passwordError = "Password minimal 6 karakter"
            isValid = false
        } else {
            passwordError = null
        }

        if (isValid && !isLoading) onLoginSubmit(emailText, passwordText)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 278.dp)
                .background(DeepGreen)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .responsiveScreenPadding()
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "GKSTTB Cibiru Wetan",
                style = MaterialTheme.typography.headlineMedium,
                color = SurfaceWhite
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Kelola data warga, kesehatan keluarga, dan pelaporan kader dalam satu alur kerja yang rapi.",
                style = MaterialTheme.typography.bodyMedium,
                color = SurfaceWhite.copy(alpha = 0.82f)
            )

            Spacer(modifier = Modifier.height(34.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(24.dp))
                    .padding(22.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(FreshTeal, RoundedCornerShape(18.dp))
                        .padding(horizontal = 14.dp, vertical = 10.dp)
                ) {
                    Text(
                        text = "Masuk sebagai kader",
                        style = MaterialTheme.typography.titleSmall,
                        color = PrimaryGreen
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Selamat datang kembali",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Gunakan akun kader yang sudah terdaftar.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(22.dp))

                AppTextField(
                    label = "Email",
                    value = emailText,
                    onValueChange = { emailText = it; emailError = null },
                    error = emailError,
                    keyboardType = KeyboardType.Email
                )

                AppPasswordField(
                    label = "Password",
                    value = passwordText,
                    onValueChange = { passwordText = it; passwordError = null },
                    error = passwordError
                )

                serverError?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(bottom = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                PrimaryButton(
                    text = if (isLoading) "Memeriksa..." else "Masuk",
                    onClick = { validateLogin() }
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontWeight = FontWeight.Medium,
                                fontFamily = Inter
                            )
                        ) { append("Belum terdaftar sebagai kader? ") }
                        withStyle(
                            SpanStyle(
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Inter
                            )
                        ) { append("Daftar") }
                    },
                    modifier = Modifier.clickable(enabled = !isLoading) { onNavigateToRegister() }
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "(c) 2026 Pemerintah Desa Cibiru Wetan",
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.62f)
                ),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 24.dp)
            )
        }
    }
}
