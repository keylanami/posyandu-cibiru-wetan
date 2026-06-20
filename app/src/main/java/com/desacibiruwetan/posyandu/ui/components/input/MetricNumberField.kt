package com.desacibiruwetan.posyandu.ui.components.input

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType

@Composable
fun MetricNumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    AppTextField(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = { input ->
            if (input.all { it.isDigit() }) onValueChange(input)
        },
        placeholder = "0",
        keyboardType = KeyboardType.Number
    )
}
