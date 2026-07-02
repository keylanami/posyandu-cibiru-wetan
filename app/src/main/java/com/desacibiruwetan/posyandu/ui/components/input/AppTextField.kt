package com.desacibiruwetan.posyandu.ui.components.input

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.BorderGray
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.SurfaceLightGray
import com.desacibiruwetan.posyandu.ui.theme.TextMuted

@Composable
fun AppTextField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Masukkan $label",
    error: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    capitalization: KeyboardCapitalization = KeyboardCapitalization.None,
    readOnly: Boolean = false,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    maxLength: Int? = null,
    counterLabel: String? = null,
    trailingContent: (@Composable () -> Unit)? = null,
) {
    var isFocused by remember { mutableStateOf(false) }
    val borderColor =
        if (error != null) MaterialTheme.colorScheme.error else if (isFocused) MaterialTheme.colorScheme.primary else BorderGray
    val bgColor = SurfaceLightGray

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = TextMuted
        )
        Spacer(modifier = Modifier.height(8.dp))

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(maxLength?.let { newValue.take(it) } ?: newValue)
            },
            readOnly = readOnly,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = capitalization
            ),
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = if (readOnly) TextMuted else MaterialTheme.colorScheme.onSurface),
            modifier = Modifier.onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = if (singleLine) 52.dp else 132.dp)
                        .background(color = bgColor, shape = RoundedCornerShape(14.dp))
                        .border(
                            width = if (isFocused && !readOnly) 2.dp else 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(14.dp)
                        )
                        .padding(horizontal = 16.dp, vertical = if (singleLine) 0.dp else 14.dp),
                    verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top
                ) {
                    Box(modifier = Modifier.weight(1f), contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart) {
                        if (value.isEmpty()) {
                            Text(text = placeholder, style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.36f)))
                        }
                        innerTextField()
                    }
                    if (counterLabel != null && maxLength != null) {
                        Spacer(modifier = Modifier.width(10.dp))
                        RemainingCounter(
                            label = counterLabel,
                            remaining = (maxLength - value.length).coerceAtLeast(0),
                            isComplete = value.length >= maxLength,
                            isError = error != null
                        )
                    }
                    trailingContent?.invoke()
                }
            }
        )
        if (error != null) {
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}

@Composable
private fun RemainingCounter(
    label: String,
    remaining: Int,
    isComplete: Boolean,
    isError: Boolean
) {
    val color = when {
        isError -> MaterialTheme.colorScheme.error
        isComplete -> PrimaryGreen
        else -> TextMuted
    }
    val text = if (isComplete) "$label lengkap" else "$label $remaining lagi"

    Text(
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = color,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .background(SurfaceWhite.copy(alpha = 0.72f), RoundedCornerShape(999.dp))
            .padding(horizontal = 9.dp, vertical = 5.dp)
    )
}
