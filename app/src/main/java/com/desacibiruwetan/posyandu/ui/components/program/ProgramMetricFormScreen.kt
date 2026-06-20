package com.desacibiruwetan.posyandu.ui.components.program

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.feedback.AppSnackbarHost
import com.desacibiruwetan.posyandu.ui.components.input.MetricNumberField
import com.desacibiruwetan.posyandu.ui.components.items.FormActionBar
import com.desacibiruwetan.posyandu.ui.components.items.FormSectionCard
import com.desacibiruwetan.posyandu.ui.theme.BgMint
import kotlinx.coroutines.launch

data class ProgramMetricField(
    val key: String,
    val label: String
)

@Composable
fun ProgramMetricFormScreen(
    title: String,
    fields: List<ProgramMetricField>,
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    onSubmit: (Map<String, Int?>) -> Unit = {}
) {
    val values = remember(fields) {
        mutableStateMapOf<String, String>().apply {
            fields.forEach { put(it.key, "") }
        }
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    androidx.compose.material3.Scaffold(
        topBar = { AppTopBar(title = title, onBackClick = onBackClick) },
        bottomBar = { AppNavBar(selectedIndex = 1, onItemSelected = onNavItemSelected) },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        containerColor = BgMint
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }
            item {
                FormSectionCard(title = "Indikator $title") {
                    fields.forEach { field ->
                        MetricNumberField(
                            label = field.label,
                            value = values[field.key].orEmpty(),
                            onValueChange = { values[field.key] = it }
                        )
                    }
                }
            }
            item {
                FormActionBar(
                    text = "Simpan Data $title",
                    icon = Icons.Default.Save,
                    onClick = {
                        onSubmit(values.mapValues { it.value.toIntOrNull() })
                        scope.launch {
                            snackbarHostState.showSnackbar("Data $title siap disimpan saat endpoint diaktifkan")
                        }
                    }
                )
            }
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}
