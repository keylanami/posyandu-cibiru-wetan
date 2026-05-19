package com.desacibiruwetan.posyandu.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AuthHeader
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.components.input.AppPasswordField
import com.desacibiruwetan.posyandu.ui.components.input.AppTextField
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.TextDark

@Composable
fun RegisterScreen(onNavigateToPersonalization: () -> Unit, onNavigateToLogin: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var errors by remember { mutableStateOf(mapOf<String, String?>()) }

    fun validate() {
        val newErrors = mutableMapOf<String, String?>()
        if (name.isBlank()) newErrors["name"] = "Nama wajib diisi"
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) newErrors["email"] =
            "Email tidak valid"
        if (phone.length < 10) newErrors["phone"] = "Nomor telepon minimal 10 digit"
        if (password.length < 6) newErrors["password"] = "Password minimal 6 karakter"
        if (confirmPassword != password) newErrors["confirm"] = "Password tidak cocok"

        errors = newErrors
        if (newErrors.isEmpty()) onNavigateToPersonalization()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .padding(vertical = 24.dp)
                .width(368.dp)
                .shadow(16.dp, spotColor = Color(0x40DFDFDF), shape = RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                .padding(24.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AuthHeader()
                Spacer(modifier = Modifier.height(24.dp))

                AppTextField(
                    label = "Nama",
                    value = name,
                    onValueChange = { name = it },
                    error = errors["name"]
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
                    onValueChange = { phone = it },
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

                Spacer(modifier = Modifier.height(24.dp))

                PrimaryButton(text = "Daftar", onClick = { validate() })

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = TextDark)) { append("Sudah terdaftar sebagai kader? ") }
                        withStyle(
                            SpanStyle(
                                color = PrimaryGreen, fontWeight = FontWeight.Bold
                            )
                        ) { append("Masuk") }
                    },
                    modifier = Modifier.clickable { onNavigateToLogin() },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }
    }
}