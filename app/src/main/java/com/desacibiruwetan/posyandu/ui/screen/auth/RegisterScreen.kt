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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.desacibiruwetan.posyandu.data.model.RegisterRequest
import com.desacibiruwetan.posyandu.data.network.UiState
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppPasswordField
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.components.layout.responsiveScreenPadding
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.viewmodel.AuthViewmodel

@Composable
fun RegisterScreenWrapper(
    onNavigateToLogin: () -> Unit,
    viewmodel: AuthViewmodel
) {
    val context = LocalContext.current
    val registerState by viewmodel.registerState.collectAsState()
    var serverError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(registerState) {
        when (registerState) {
            is UiState.Success -> {
                Toast.makeText(context, "Pendaftaran Berhasil! Silakan Login", Toast.LENGTH_SHORT).show()
                viewmodel.resetRegisterState()
                onNavigateToLogin()
            }

            is UiState.Error -> {
                serverError = (registerState as UiState.Error).message
                viewmodel.resetRegisterState()
            }

            else -> Unit
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        RegisterScreen(
            isLoading = registerState is UiState.Loading,
            serverError = serverError,
            onNavigateToLogin = onNavigateToLogin,
            onRegisterSubmit = { request ->
                serverError = null
                viewmodel.register(request)
            }
        )

        if (registerState is UiState.Loading) {
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
fun RegisterScreen(
    isLoading: Boolean,
    serverError: String?,
    onNavigateToLogin: () -> Unit,
    onRegisterSubmit: (RegisterRequest) -> Unit
) {
    var nik by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var errors by remember { mutableStateOf(mapOf<String, String?>()) }

    fun validate() {
        val newErrors = mutableMapOf<String, String?>()
        if (nik.length < 16) newErrors["nik"] = "NIK harus 16 digit"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) newErrors["email"] = "Email tidak valid"
        if (phone.length < 10) newErrors["phone"] = "Nomor telepon minimal 10 digit"
        if (password.length < 8) newErrors["password"] = "Password minimal 8 karakter"
        if (confirmPassword != password) newErrors["confirm"] = "Password tidak cocok"

        errors = newErrors
        if (newErrors.isEmpty() && !isLoading) {
            onRegisterSubmit(
                RegisterRequest(
                    nik = nik,
                    email = email,
                    phoneNumber = phone,
                    password = password,
                    passwordConfirmation = confirmPassword
                )
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = 230.dp)
                .background(DeepGreen)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .responsiveScreenPadding()
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            Text(
                text = "Daftar Kader",
                style = MaterialTheme.typography.headlineMedium,
                color = SurfaceWhite
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Buat akun untuk mengakses pendataan Posyandu Cibiru Wetan.",
                style = MaterialTheme.typography.bodyMedium,
                color = SurfaceWhite.copy(alpha = 0.82f)
            )

            Spacer(modifier = Modifier.height(28.dp))

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
                        text = "Identitas akun kader",
                        style = MaterialTheme.typography.titleSmall,
                        color = PrimaryGreen
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Text(
                    text = "Lengkapi data awal",
                    style = MaterialTheme.typography.titleLarge
                )
                Text(
                    text = "Pastikan NIK dan nomor telepon aktif.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = TextMuted
                )

                Spacer(modifier = Modifier.height(22.dp))

                AppTextField(
                    label = "NIK",
                    value = nik,
                    onValueChange = { nik = it.filter(Char::isDigit).take(16) },
                    error = errors["nik"],
                    maxLength = 16,
                    counterLabel = "NIK",
                    keyboardType = KeyboardType.Number
                )

                AppTextField(
                    label = "Email",
                    value = email,
                    onValueChange = { email = it },
                    error = errors["email"],
                    keyboardType = KeyboardType.Email
                )

                AppTextField(
                    label = "Nomor Telepon",
                    value = phone,
                    onValueChange = { if (it.all { char -> char.isDigit() }) phone = it },
                    error = errors["phone"],
                    keyboardType = KeyboardType.Phone
                )

                AppPasswordField(
                    label = "Password",
                    value = password,
                    onValueChange = { password = it },
                    error = errors["password"]
                )

                AppPasswordField(
                    label = "Konfirmasi Password",
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = "Konfirmasi Password",
                    error = errors["confirm"]
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
                    text = if (isLoading) "Memproses..." else "Daftar",
                    onClick = { validate() }
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = MaterialTheme.colorScheme.onSurface)) {
                            append("Sudah terdaftar sebagai kader? ")
                        }
                        withStyle(SpanStyle(color = PrimaryGreen, fontWeight = FontWeight.Bold)) {
                            append("Masuk")
                        }
                    },
                    modifier = Modifier.clickable(enabled = !isLoading) { onNavigateToLogin() },
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}
