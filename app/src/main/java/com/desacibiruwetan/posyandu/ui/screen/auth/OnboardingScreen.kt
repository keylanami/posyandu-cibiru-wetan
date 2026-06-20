package com.desacibiruwetan.posyandu.ui.screen.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.button.PrimaryButton
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.DeepGreen
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import com.desacibiruwetan.posyandu.ui.theme.SurfaceWhite
import com.desacibiruwetan.posyandu.ui.theme.TextMuted

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonalizationScreen(onComplete: () -> Unit) {
    val rwList = (1..19).map { "RW %02d".format(it) }
    val rtList = (1..4).map { "RT %02d".format(it) }

    var selectedRW by remember { mutableStateOf("") }
    var selectedRT by remember { mutableStateOf("") }

    var rwExpanded by remember { mutableStateOf(false) }
    var rtExpanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BgMint)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DeepGreen, RoundedCornerShape(28.dp))
                    .padding(24.dp)
            ) {
                Column {
                    Text(
                        "Personalisasi Wilayah",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = SurfaceWhite
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Pilih RT/RW tugas untuk menyesuaikan data dashboard dan pencarian warga.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = SurfaceWhite.copy(alpha = 0.78f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SurfaceWhite, RoundedCornerShape(22.dp))
                    .border(1.dp, BorderLight, RoundedCornerShape(22.dp))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
            Text(
                "Wilayah Kerja",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                "Data yang tampil akan diprioritaskan sesuai wilayah ini.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(24.dp))

            ExposedDropdownMenuBox(
                expanded = rwExpanded, onExpandedChange = { rwExpanded = !rwExpanded }) {
                OutlinedTextField(
                    value = selectedRW,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Pilih RW") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = rwExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite
                    )
                )
                ExposedDropdownMenu(
                    expanded = rwExpanded, onDismissRequest = { rwExpanded = false }) {
                    rwList.forEach { rw ->
                        DropdownMenuItem(
                            text = { Text(rw) },
                            onClick = { selectedRW = rw; selectedRT = ""; rwExpanded = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            ExposedDropdownMenuBox(
                expanded = rtExpanded,
                onExpandedChange = { if (selectedRW.isNotEmpty()) rtExpanded = !rtExpanded }) {
                OutlinedTextField(
                    value = selectedRT,
                    onValueChange = {},
                    readOnly = true,
                    label = {
                        Text("Pilih RT")
                    },
                    placeholder = {
                        if (selectedRW.isEmpty()) Text("Pilih RW terlebih dahulu")
                    },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = rtExpanded) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    enabled = selectedRW.isNotEmpty(),
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryGreen,
                        unfocusedBorderColor = BorderLight,
                        focusedContainerColor = SurfaceWhite,
                        unfocusedContainerColor = SurfaceWhite,
                        disabledBorderColor = BorderLight,
                        disabledContainerColor = BgMint
                    )
                )
                ExposedDropdownMenu(
                    expanded = rtExpanded, onDismissRequest = { rtExpanded = false }) {
                    rtList.forEach { rt ->
                        DropdownMenuItem(
                            text = { Text(rt) },
                            onClick = { selectedRT = rt; rtExpanded = false })
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            PrimaryButton(
                text = "Selesai & Masuk Dashboard",
                onClick = onComplete,
                enabled = selectedRW.isNotEmpty() && selectedRT.isNotEmpty()
            )
            }
        }
    }
}
