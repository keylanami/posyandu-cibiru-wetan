package com.desacibiruwetan.posyandu.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.*

@Composable
fun RegisterScreen(onNavigateToPersonalization: () -> Unit, onNavigateToLogin: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var isPasswordVisible by remember { mutableStateOf(false) }
    var isConfirmVisible by remember { mutableStateOf(false) }

    // Validation Errors
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

    Box(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background)) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(368.dp)
                .shadow(
                    16.dp,
                    spotColor = Color(0x40DFDFDF),
                    ambientColor = Color(0x40DFDFDF),
                    shape = RoundedCornerShape(15.dp)
                )
                .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(15.dp))
                .padding(24.dp)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    Icons.Default.Favorite,
                    "Logo",
                    tint = PrimaryGreen,
                    modifier = Modifier.size(60.dp)
                )
                Text("Selamat Datang", style = MaterialTheme.typography.titleLarge)
                Text(
                    "Portal kesehatan Desa Cibiru Wetan",
                    style = MaterialTheme.typography.bodyLarge
                )

                Spacer(modifier = Modifier.height(24.dp))

                InputField("Nama", name, { name = it }, errors["name"])
                InputField("Email", email, { email = it }, errors["email"], KeyboardType.Email)
                InputField(
                    "Nomor Telepon",
                    phone,
                    { phone = it },
                    errors["phone"],
                    KeyboardType.Phone
                )

                // Password Field
                PasswordField(
                    "Password",
                    password,
                    { password = it },
                    isPasswordVisible,
                    { isPasswordVisible = !it },
                    errors["password"]
                )
                PasswordField(
                    "Konfirmasi Password",
                    confirmPassword,
                    { confirmPassword = it },
                    isConfirmVisible,
                    { isConfirmVisible = !it },
                    errors["confirm"]
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { validate() },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen),
                    shape = RoundedCornerShape(5.dp)
                ) {
                    Text("Daftar", style = MaterialTheme.typography.labelSmall)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(color = TextDark)) { append("Sudah terdaftar sebagai kader? ") }
                        withStyle(
                            SpanStyle(
                                color = PrimaryGreen,
                                fontWeight = FontWeight.Bold
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

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(SurfaceLightGray, RoundedCornerShape(5.dp))
                        .border(
                            1.dp,
                            if (error != null) Color.Red else BorderGray,
                            RoundedCornerShape(5.dp)
                        )
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) Text(
                        "Masukkan $label",
                        style = MaterialTheme.typography.labelMedium
                    )
                    innerTextField()
                }
            }
        )
        if (error != null) Text(error, color = Color.Red, fontSize = 10.sp)
    }
}

@Composable
fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isVisible: Boolean,
    onToggle: (Boolean) -> Unit,
    error: String?
) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            visualTransformation = if (isVisible) VisualTransformation.None else PasswordVisualTransformation(),
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(SurfaceLightGray, RoundedCornerShape(5.dp))
                        .border(
                            1.dp,
                            if (error != null) Color.Red else BorderGray,
                            RoundedCornerShape(5.dp)
                        )
                        .padding(horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(modifier = Modifier.weight(1f)) {
                        if (value.isEmpty()) Text(
                            "Konfirmasi $label",
                            style = MaterialTheme.typography.labelMedium
                        )
                        innerTextField()
                    }
                    IconButton(onClick = { onToggle(isVisible) }) {
                        Icon(
                            if (isVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                            null,
                            tint = TextPlaceholder,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        )
        if (error != null) Text(error, color = Color.Red, fontSize = 10.sp)
    }
}