package com.desacibiruwetan.posyandu.ui.components.input

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen

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
    var isFocused by remember { mutableStateOf(value = false) }
    
    val containerColor by animateColorAsState(
        targetValue = when {
            readOnly -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
            isFocused -> MaterialTheme.colorScheme.surface
            else -> MaterialTheme.colorScheme.surfaceContainerLow
        },
        label = "containerColor",
    )
    
    val borderColor by animateColorAsState(
        targetValue = when {
            error != null -> MaterialTheme.colorScheme.error
            isFocused -> MaterialTheme.colorScheme.primary
            else -> MaterialTheme.colorScheme.outlineVariant
        },
        label = "borderColor",
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (error != null) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(start = 4.dp),
        )
        Spacer(modifier = Modifier.height(4.dp))

        BasicTextField(
            value = value,
            onValueChange = { newValue ->
                onValueChange(maxLength?.let { newValue.take(it) } ?: newValue)
            },
            readOnly = readOnly,
            singleLine = singleLine,
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType,
                capitalization = capitalization,
            ),
            visualTransformation = visualTransformation,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = if (readOnly) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
            ),
            modifier = modifier.onFocusChanged { isFocused = it.isFocused },
            decorationBox = { innerTextField ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = if (singleLine) 56.dp else 120.dp)
                        .background(color = containerColor, shape = RoundedCornerShape(18.dp))
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(18.dp),
                        )
                        .padding(horizontal = 16.dp, vertical = if (singleLine) 0.dp else 14.dp),
                    verticalAlignment = if (singleLine) Alignment.CenterVertically else Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = if (singleLine) Alignment.CenterStart else Alignment.TopStart,
                    ) {
                        if (value.isEmpty()) {
                            Text(
                                text = placeholder,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            )
                        }
                        innerTextField()
                    }
                    trailingContent?.invoke()
                }
            },
        )
        
        // Supporting row for error and counter
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp, start = 4.dp, end = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(modifier = Modifier.weight(1f)) {
                error?.let {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(14.dp),
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    }
                }
            }
            
            if (counterLabel != null && maxLength != null) {
                val remaining = (maxLength - value.length).coerceAtLeast(0)
                val isComplete = value.length >= maxLength
                val counterText = if (isComplete) "$counterLabel lengkap" else "$counterLabel $remaining lagi"
                
                Text(
                    text = counterText,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isComplete && error == null) PrimaryGreen else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            } else if (maxLength != null) {
                Text(
                    text = "${value.length} / $maxLength",
                    style = MaterialTheme.typography.labelSmall,
                    color = if (value.length >= maxLength) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
