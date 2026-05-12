package com.desacibiruwetan.posyandu.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationScreen(onComplete: () -> Unit) {
    val rwList = (1..19).map { "RW %02d".format(it) }
    val rtList = (1..4).map { "RT %02d".format(it) }

    var selectedRW by remember { mutableStateOf("") }
    var selectedRT by remember { mutableStateOf("") }

    var rwExpanded by remember { mutableStateOf(false) }
    var rtExpanded by remember { mutableStateOf(false) }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(BgMint)) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(368.dp)
                .background(Color.White, RoundedCornerShape(15.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Personalisasi Wilayah", style = MaterialTheme.typography.titleLarge)
            Text(
                "Pilih wilayah tugas Anda sebagai kader",
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(32.dp))

            // RW Dropdown
            ExposedDropdownMenuBox(
                expanded = rwExpanded,
                onExpandedChange = { rwExpanded = !rwExpanded }
            ) {
                OutlinedTextField(
                    value = selectedRW,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pilih RW") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = rwExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
                )
                ExposedDropdownMenu(
                    expanded = rwExpanded,
                    onDismissRequest = { rwExpanded = false }) {
                    rwList.forEach { rw ->
                        DropdownMenuItem(
                            text = { Text(rw) },
                            onClick = {
                                selectedRW = rw
                                selectedRT = ""
                                rwExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // RT Dropdown
            ExposedDropdownMenuBox(
                expanded = rtExpanded,
                onExpandedChange = { if (selectedRW.isNotEmpty()) rtExpanded = !rtExpanded }
            ) {
                OutlinedTextField(
                    value = selectedRT,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pilih RT") },
                    placeholder = { if (selectedRW.isEmpty()) Text("Pilih RW terlebih dahulu") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = rtExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    enabled = selectedRW.isNotEmpty(),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PrimaryGreen)
                )
                ExposedDropdownMenu(
                    expanded = rtExpanded,
                    onDismissRequest = { rtExpanded = false }) {
                    rtList.forEach { rt ->
                        DropdownMenuItem(
                            text = { Text(rt) },
                            onClick = {
                                selectedRT = rt
                                rtExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onComplete,
                enabled = selectedRW.isNotEmpty() && selectedRT.isNotEmpty(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryGreen)
            ) {
                Text("Selesai & Masuk Dashboard")
            }
        }
    }
}