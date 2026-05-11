package com.desacibiruwetan.posyandu.ui.screen.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.R
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mykader.R // Sesuaikan dengan package project

@Composable
fun LoginScreen() {
    var emailText by remember { mutableStateOf("") }
    var passwordText by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color(0xFFEDFFF9))
    ) {
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
                .background(color = Color(0xFFFFFFFF), shape = RoundedCornerShape(size = 15.dp))
                .padding(top = 40.dp, bottom = 32.dp, start = 23.dp, end = 23.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Logo
                Image(
                    painter = painterResource(id = R.drawable.image3), // Ganti dengan ID Logo yang sesuai
                    contentDescription = "Logo",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .width(59.dp)
                        .height(60.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Title
                Text(
                    text = "Selamat Datang",
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontFamily = FontFamily(Font(R.font.poppins)),
                        fontWeight = FontWeight(700),
                        color = Color(0xFF272727),
                    )
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Subtitle
                Text(
                    text = "Portal kesehatan Desa Cibiru Wetan",
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.inter)),
                        fontWeight = FontWeight(500),
                        color = Color(0xFF272727),
                    )
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email Input
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Email",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF272727),
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = emailText,
                        onValueChange = { emailText = it },
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF272727)
                        ),
                        decorationBox = { innerTextField ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .background(color = Color(0xFFFBFBFB), shape = RoundedCornerShape(size = 5.dp))
                                    .border(width = 1.dp, color = Color(0xFFE9E9E9), shape = RoundedCornerShape(size = 5.dp))
                                    .padding(horizontal = 12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                if (emailText.isEmpty()) {
                                    Text(
                                        text = "Masukkan Email",
                                        style = TextStyle(
                                            fontSize = 12.sp,
                                            fontFamily = FontFamily(Font(R.font.inter)),
                                            fontWeight = FontWeight(500),
                                            color = Color(0xFFC9C9C9),
                                        )
                                    )
                                }
                                innerTextField()
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Password Input
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Password",
                        style = TextStyle(
                            fontSize = 13.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF272727),
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    BasicTextField(
                        value = passwordText,
                        onValueChange = { passwordText = it },
                        textStyle = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(500),
                            color = Color(0xFF272727)
                        ),
                        decorationBox = { innerTextField ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(38.dp)
                                    .background(color = Color(0xFFFBFBFB), shape = RoundedCornerShape(size = 5.dp))
                                    .border(width = 1.dp, color = Color(0xFFE9E9E9), shape = RoundedCornerShape(size = 5.dp))
                                    .padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Box(modifier = Modifier.weight(1f)) {
                                    if (passwordText.isEmpty()) {
                                        Text(
                                            text = "Masukkan Password",
                                            style = TextStyle(
                                                fontSize = 12.sp,
                                                fontFamily = FontFamily(Font(R.font.inter)),
                                                fontWeight = FontWeight(500),
                                                color = Color(0xFFC9C9C9),
                                            )
                                        )
                                    }
                                    innerTextField()
                                }
                                Image(
                                    painter = painterResource(id = R.drawable.eye), // Ganti dengan ID Eye Icon
                                    contentDescription = "Toggle Password",
                                    contentScale = ContentScale.None,
                                    modifier = Modifier.size(20.dp).clickable { /* Action Toggle Password */ }
                                )
                            }
                        }
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Login Button
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(color = Color(0xFF1B9E75), shape = RoundedCornerShape(size = 5.dp))
                        .clickable { /* Action Login */ },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Masuk",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontFamily = FontFamily(Font(R.font.inter)),
                            fontWeight = FontWeight(600),
                            color = Color(0xFFFFFFFF),
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Register Link
                val registerText = buildAnnotatedString {
                    withStyle(style = SpanStyle(color = Color(0xFF272727), fontSize = 12.sp, fontWeight = FontWeight(500), fontFamily = FontFamily(Font(R.font.inter)))) {
                        append("Belum terdaftar sebagai kader? ")
                    }
                    withStyle(style = SpanStyle(color = Color(0xFF1B9E75), fontSize = 12.sp, fontWeight = FontWeight(700), fontFamily = FontFamily(Font(R.font.inter)))) {
                        append("Daftar")
                    }
                }
                Text(
                    text = registerText,
                    modifier = Modifier.clickable { /* Action Navigate to Register */ }
                )
            }
        }

        // Footer Copyright
        Text(
            text = "© 2026 Pemerintah Desa Cibiru Wetan",
            style = TextStyle(
                fontSize = 12.sp,
                fontFamily = FontFamily(Font(R.font.inter)),
                fontWeight = FontWeight(500),
                color = Color(0xFF272727),
            ),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        )
    }
}