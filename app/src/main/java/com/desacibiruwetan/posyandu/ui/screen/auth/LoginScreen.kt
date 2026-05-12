package com.desacibiruwetan.posyandu.ui.screen.auth

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.components.bar.AuthHeader
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppPasswordField
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PosyanduCibiruTheme
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen

@Composable
fun LoginScreenWrapper(onNavigateToRegister: () -> Unit) {
    var isDarkTheme by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = isDarkTheme,
        transitionSpec = {
            (fadeIn(tween(600)) + scaleIn(
                tween(600),
                transformOrigin = TransformOrigin(1f, 0f)
            )) togetherWith
                    (fadeOut(tween(600)) + scaleOut(
                        tween(600),
                        transformOrigin = TransformOrigin(1f, 0f)
                    ))
        },
        label = "ThemeWaveTransition"
    ) { targetTheme ->
        PosyanduCibiruTheme(darkTheme = targetTheme) {
            LoginScreen(
                isDarkTheme = targetTheme,
                onThemeToggle = { isDarkTheme = !isDarkTheme },
                onNavigateToRegister = onNavigateToRegister
            )
        }
    }
}

@Composable
fun LoginScreen(isDarkTheme: Boolean, onThemeToggle: () -> Unit, onNavigateToRegister: () -> Unit) {
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    fun validateLogin() {
        var isValid = true
        if (emailText.isBlank()) {
            emailError = "Email tidak boleh kosong"; isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailError = "Format email tidak valid"; isValid = false
        } else emailError = null

        if (passwordText.isBlank()) {
            passwordError = "Password tidak boleh kosong"; isValid = false
        } else if (passwordText.length < 6) {
            passwordError = "Password minimal 6 karakter"; isValid = false
        } else passwordError = null

        if (isValid) println("Login Sukses: $emailText")
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        IconButton(
            onClick = onThemeToggle,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                contentDescription = "Toggle Theme",
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(368.dp)
                .shadow(16.6.dp, shape = RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                .padding(top = 40.dp, bottom = 32.dp, start = 23.dp, end = 23.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                AuthHeader()
                Spacer(modifier = Modifier.height(32.dp))

                AppTextField(
                    label = "Email",
                    value = emailText,
                    onValueChange = { emailText = it; emailError = null },
                    error = emailError,
                    keyboardType = KeyboardType.Email,
                    isDarkTheme = isDarkTheme
                )

                AppPasswordField(
                    label = "Password",
                    value = passwordText,
                    onValueChange = { passwordText = it; passwordError = null },
                    error = passwordError,
                    isDarkTheme = isDarkTheme
                )

                Spacer(modifier = Modifier.height(16.dp))

                PrimaryButton(text = "Masuk", onClick = { validateLogin() })

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            SpanStyle(
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                fontFamily = Inter
                            )
                        ) { append("Belum terdaftar sebagai kader? ") }
                        withStyle(
                            SpanStyle(
                                color = PrimaryGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                fontFamily = Inter
                            )
                        ) { append("Daftar") }
                    },
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }

        Text(
            text = "© 2026 Pemerintah Desa Cibiru Wetan",
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}