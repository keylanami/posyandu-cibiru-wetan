package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotBencanaAlamScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {
    ProgramMetricFormScreen(
        title = "Bencana Alam",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("bencana_alam", "Bencana Alam"),
            ProgramMetricField("kerusakan_ekosistem_eksploitasi", "Kerusakan Ekosistem Eksploitasi"),
            ProgramMetricField("kerusakan_ekosistem_bencana", "Kerusakan Ekosistem Bencana"),
            ProgramMetricField("abrasi", "Abrasi"),
            ProgramMetricField("alih_fungsi_lahan", "Alih Fungsi Lahan"),
            ProgramMetricField("restorasi_lahan", "Restorasi Lahan"),
            ProgramMetricField("kerusakan_fasilitas", "Kerusakan Fasilitas")
        )
    )
}
