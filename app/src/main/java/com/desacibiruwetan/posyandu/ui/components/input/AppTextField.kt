package com.desacibiruwetan.posyandu.ui.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.BorderGray
import com.desacibiruwetan.posyandu.ui.theme.SurfaceLightGray

@Composable
fun AppTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Masukkan $label",
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    isDarkTheme: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor =
        if (error != null) MaterialTheme.colorScheme.error else if (isFocused) MaterialTheme.colorScheme.primary else BorderGray
    val bgColor = if (isDarkTheme) Color(0xFF303030) else SurfaceLightGray

    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 8.dp)) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(38.dp)
                        .background(color = bgColor, shape = RoundedCornerShape(5.dp))
                        .border(
                            width = if (isFocused) 1.5.dp else 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(5.dp)
                        )
                        .padding(horizontal = 12.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(text = placeholder, style = MaterialTheme.typography.labelMedium)
                    }
                    innerTextField()
                }
            }
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }
    }
}