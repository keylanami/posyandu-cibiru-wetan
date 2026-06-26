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
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.desacibiruwetan.posyandu.ui.theme.BorderGray
import com.desacibiruwetan.posyandu.ui.theme.FreshTeal
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceLightGray
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextDark
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.ui.theme.TextPlaceholder

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDropdownField(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    options: List<String>,
    onValueChange: (String) -> Unit,
    placeholder: String = "Pilih $label",
    error: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val borderColor = when {
        error != null -> MaterialTheme.colorScheme.error
        expanded -> PrimaryGreen
        else -> BorderGray
    }

    Column(modifier = modifier
        .fillMaxWidth()
        .padding(bottom = 12.dp)) {
        Text(text = label, style = MaterialTheme.typography.labelLarge, color = TextMuted)
        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            Box(
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(color = SurfaceLightGray, shape = RoundedCornerShape(14.dp))
                    .border(width = if (expanded) 2.dp else 1.dp, color = borderColor, shape = RoundedCornerShape(14.dp))
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (value.isEmpty()) {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.labelMedium.copy(color = TextPlaceholder)
                    )
                } else {
                    Text(text = value, style = MaterialTheme.typography.bodyMedium)
                }

                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded,
                    modifier = Modifier.align(Alignment.CenterEnd)
                )
            }

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier
                    .background(SurfaceWhite, RoundedCornerShape(16.dp))
                    .border(1.dp, BorderGray.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
            ) {
                options.forEach { selectionOption ->
                    val selected = selectionOption == value
                    DropdownMenuItem(
                        modifier = Modifier.background(if (selected) FreshTeal else SurfaceWhite),
                        text = {
                            Text(
                                selectionOption,
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (selected) PrimaryGreen else TextDark,
                                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        onClick = {
                            onValueChange(selectionOption)
                            expanded = false
                        }
                    )
                }
            }
        }
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
