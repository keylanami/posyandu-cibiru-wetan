package com.desacibiruwetan.posyandu.ui.components.dialog

import androidx.compose.foundation.background
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.desacibiruwetan.posyandu.data.model.DummyDetailWarga
import com.desacibiruwetan.posyandu.data.model.MockData
import com.desacibiruwetan.posyandu.ui.components.feedback.EmptyState
import com.desacibiruwetan.posyandu.ui.components.input.AppSearchBar
import com.desacibiruwetan.posyandu.ui.components.items.WargaItemCard

@Composable
fun SearchWargaDialog(
    onDismiss: () -> Unit, onWargaSelected: (DummyDetailWarga) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val filteredWarga = remember(searchQuery) {
        MockData.listWarga.filter {
            it.name.contains(searchQuery, ignoreCase = true) || it.nik.contains(searchQuery)
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight(0.8f)
                .clip(RoundedCornerShape(15.dp))
                .background(MaterialTheme.colorScheme.surface)
                .padding(24.dp)
        ) {
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
                            name = warga.name, nik = warga.nik, rtRw = warga.rtRw, onClick = {
                                onWargaSelected(warga)
                                onDismiss()
                            })
                    }
                }
            }
        }
    }
}