package com.desacibiruwetan.posyandu.ui.components.dialog

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.desacibiruwetan.posyandu.data.local.entity.AnggotaEntity
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.components.input.AppSearchBar
import com.desacibiruwetan.posyandu.ui.components.items.WargaItemCard
import com.desacibiruwetan.posyandu.ui.theme.BorderLight
import com.desacibiruwetan.posyandu.ui.theme.TextMuted
import com.desacibiruwetan.posyandu.viewmodel.AnggotaViewmodel

@Composable
fun SearchWargaDialog(
    onDismiss: () -> Unit,
    onWargaSelected: (AnggotaEntity) -> Unit,
    anggotaViewModel: AnggotaViewmodel,
    filterByKategori: String?= null
) {
    var searchQuery by remember { mutableStateOf("") }

    val context = LocalContext.current
    val sharedPreferences = remember { context.getSharedPreferences("posyandu_prefs", Context.MODE_PRIVATE) }
    val userRt = sharedPreferences.getString("USER_RT", "00") ?: "00"
    val userRw = sharedPreferences.getString("USER_RW", "00") ?: "00"
    val displayRtRw = "RT $userRt / RW $userRw"

    val listWargaAsli by anggotaViewModel.listAnggotaLocal.collectAsState()

    val filteredWarga = remember(searchQuery, listWargaAsli, filterByKategori) {
        listWargaAsli.filter { warga ->
            val matchSearch = warga.nama.contains(searchQuery, ignoreCase = true) || warga.nik.contains(searchQuery)
            val matchKategori = filterByKategori == null || warga.kategoriUsia.equals(filterByKategori, ignoreCase = true)

            matchSearch && matchKategori
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .fillMaxHeight(0.82f)
                .clip(RoundedCornerShape(28.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, BorderLight, RoundedCornerShape(28.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "Pilih Warga",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = displayRtRw,
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppSearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                placeholder = "Cari nama atau NIK warga..."
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (filteredWarga.isEmpty()) {
                EmptyState(
                    icon = Icons.Default.Face, message = "Data warga tidak ditemukan"
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredWarga) { warga ->
                        WargaItemCard(
                            name = warga.nama,
                            nik = warga.nik,
                            rtRw = displayRtRw,
                            onClick = {
                                onWargaSelected(warga)
                                onDismiss()
                            }
                        )
                    }
                }
            }
        }
    }
}
