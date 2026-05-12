package com.desacibiruwetan.posyandu.ui.screen.auth

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.BorderGray
import com.desacibiruwetan.posyandu.ui.theme.Inter
import com.desacibiruwetan.posyandu.ui.theme.PosyanduCibiruTheme
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceLightGray
import com.desacibiruwetan.posyandu.ui.theme.TextDark
import com.desacibiruwetan.posyandu.ui.theme.TextPlaceholder

@Composable
fun LoginScreenWrapper(onNavigateToRegister: () -> Unit) {
    var isDarkTheme by remember { mutableStateOf(false) }

    AnimatedContent(
        targetState = isDarkTheme,
        transitionSpec = {
            (fadeIn(animationSpec = tween(600)) + scaleIn(
                animationSpec = tween(600),
                transformOrigin = TransformOrigin(1f, 0f)
            )).togetherWith(
                fadeOut(animationSpec = tween(600)) + scaleOut(
                    animationSpec = tween(600),
                    transformOrigin = TransformOrigin(1f, 0f)
                )
            )
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
    // States for Inputs
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    // States for Interactivity
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isEmailFocused by remember { mutableStateOf(false) }
    var isPasswordFocused by remember { mutableStateOf(false) }

    // States for Sanity/Validation Errors
    var emailError by remember { mutableStateOf<String?>(null) }
    var passwordError by remember { mutableStateOf<String?>(null) }

    // Validation Logic
    fun validateLogin() {
        var isValid = true

        // Email Sanity Check
        if (emailText.isBlank()) {
            emailError = "Email tidak boleh kosong"
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailText).matches()) {
            emailError = "Format email tidak valid"
            isValid = false
        } else {
            emailError = null
        }

        // Password Sanity Check
        if (passwordText.isBlank()) {
            passwordError = "Password tidak boleh kosong"
            isValid = false
        } else if (passwordText.length < 6) {
            passwordError = "Password minimal 6 karakter"
            isValid = false
        } else {
            passwordError = null
        }

        if (isValid) {
            // TODO: Action ketika lolos validasi (misal: panggil ViewModel / Navigasi)
            println("Login Sukses: $emailText")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
    ) {
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

        // Main Content (Card)
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(368.dp)
                .shadow(
                    elevation = 16.6.dp,
                    spotColor = Color(0x40DFDFDF),
                    ambientColor = Color(0x40DFDFDF),
                    shape = RoundedCornerShape(size = 15.dp)
                )
                .background(color = MaterialTheme.colorScheme.surface, shape = RoundedCornerShape(size = 15.dp))
                .padding(top = 40.dp, bottom = 32.dp, start = 23.dp, end = 23.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Logo
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Logo",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .width(59.dp)
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = "Selamat Datang",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle
                Text(
                    text = "Portal kesehatan Desa Cibiru Wetan",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email Input
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Email",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val emailBorderColor = if (emailError != null) MaterialTheme.colorScheme.error else if (isEmailFocused) MaterialTheme.colorScheme.primary else BorderGray

                    BasicTextField(
                        value = emailText,
                        onValueChange = {
                            emailText = it
                            emailError = null
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.onFocusChanged { isEmailFocused = it.isFocused },
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .background(color = if (isDarkTheme) Color(0xFF303030) else SurfaceLightGray, shape = RoundedCornerShape(size = 5.dp))
                                    .border(width = (if (isEmailFocused) 1.5.dp else 1.dp), color = emailBorderColor, shape = RoundedCornerShape(size = 5.dp))
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (emailText.isEmpty()) {
                                    Text(
                                        text = "Masukkan Email",
                                        style = MaterialTheme.typography.labelMedium
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                    if (emailError != null) {
                        Text(
                            text = emailError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password Input
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Password",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val passwordBorderColor = if (passwordError != null) MaterialTheme.colorScheme.error else if (isPasswordFocused) MaterialTheme.colorScheme.primary else BorderGray

                    BasicTextField(
                        value = passwordText,
                        onValueChange = {
                            passwordText = it
                            passwordError = null
                        },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
                        modifier = Modifier.onFocusChanged { isPasswordFocused = it.isFocused },
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .background(color = if (isDarkTheme) Color(0xFF303030) else SurfaceLightGray, shape = RoundedCornerShape(size = 5.dp))
                                    .border(width = (if (isPasswordFocused) 1.5.dp else 1.dp), color = passwordBorderColor, shape = RoundedCornerShape(size = 5.dp))
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    if (passwordText.isEmpty()) {
                                        Text(
                                            text = "Masukkan Password",
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                    innerTextField()
                                }
                                // Toggle Password Icon
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = "Toggle Password",
                                    tint = TextPlaceholder,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .clickable { isPasswordVisible = !isPasswordVisible }
                                )
                            }
                        }
                    )
                    // Error Message
                    if (passwordError != null) {
                        Text(
                            text = passwordError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(size = 5.dp))
                        .clickable { validateLogin() },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Masuk",
                        style = MaterialTheme.typography.labelSmall
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Register Link
                val registerText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onSurface, fontSize = 12.sp, fontWeight = FontWeight.Medium, fontFamily = Inter)) {
                        append("Belum terdaftar sebagai kader? ")
                    }
                    withStyle(style = SpanStyle(color = PrimaryGreen, fontSize = 12.sp, fontWeight = FontWeight.Bold, fontFamily = Inter)) {
                        append("Daftar")
                    }
                }
                Text(
                    text = registerText,
                    modifier = Modifier.clickable { onNavigateToRegister() }
                )
            }
        }

        // Footer Copyright
        Text(
            text = "© 2026 Pemerintah Desa Cibiru Wetan",
            style = MaterialTheme.typography.labelMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}