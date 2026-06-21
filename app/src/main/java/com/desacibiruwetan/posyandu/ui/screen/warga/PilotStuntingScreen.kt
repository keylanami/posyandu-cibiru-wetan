package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotStuntingScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {
    ProgramMetricFormScreen(
        title = "Stunting",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("bayi_lahir_prematur", "Bayi Lahir Prematur"),
            ProgramMetricField("bayi_bblr", "Bayi BBLR"),
            ProgramMetricField("balita_kurang_gizi", "Balita Kurang Gizi"),
            ProgramMetricField("balita_stunting", "Balita Stunting"),
            ProgramMetricField("balita_rutin_pemeriksaan_tumbuh_kembang", "Balita Rutin Pemeriksaan Tumbuh Kembang"),
            ProgramMetricField("kehamilan_tidak_direncanakan", "Kelahiran tidak direncanakan"),
            ProgramMetricField("jarak_kehamilan_terlalu_dekat", "Jarak Kehamilan Terlalu Dekat")
        )
    )
}
