package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotKesBuNakScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {
    ProgramMetricFormScreen(
        title = "KIA",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("ibu_hamil_rutin_periksa", "Ibu Hamil Rutin Periksa"),
            ProgramMetricField("persalinan_tenaga_kesehatan", "Persalinan Tenaga Kesehatan"),
            ProgramMetricField("kematian_ibu_nifas", "Kematian Ibu Nifas"),
            ProgramMetricField("kanker_serviks", "Kanker Serviks"),
            ProgramMetricField("imunisasi_bayi_balita", "Imunisasi Bayi Balita"),
            ProgramMetricField("bayi_balita_sakit_terdata", "Bayi Balita Sakit Terdata"),
            ProgramMetricField("kematian_bayi_balita", "Kematian Bayi Balita")
        )
    )
}
