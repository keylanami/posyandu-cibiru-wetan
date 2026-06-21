package com.desacibiruwetan.posyandu.ui.components.input

import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.desacibiruwetan.posyandu.utils.dateFieldToUtcMillis
import com.desacibiruwetan.posyandu.utils.formatDateForDisplay
import com.desacibiruwetan.posyandu.utils.utcMillisToFormDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDateField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String = "Pilih tanggal",
    error: String? = null
) {
    var openPicker by remember { mutableStateOf(false) }
    val selectedMillis = remember(value) { dateFieldToUtcMillis(value) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = selectedMillis)
    val displayValue = formatDateForDisplay(value)

    AppTextField(
        modifier = modifier.clickable { openPicker = true },
        label = label,
        value = displayValue,
        onValueChange = {},
        placeholder = placeholder,
        error = error,
        readOnly = true,
        trailingContent = {
            IconButton(onClick = { openPicker = true }) {
                Icon(Icons.Default.CalendarMonth, contentDescription = "Pilih tanggal")
            }
        }
    )

    if (openPicker) {
        DatePickerDialog(
            onDismissRequest = { openPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { onValueChange(utcMillisToFormDate(it)) }
                        openPicker = false
                    }
                ) {
                    Text("Pilih")
                }
            },
            dismissButton = {
                TextButton(onClick = { openPicker = false }) {
                    Text("Batal")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
