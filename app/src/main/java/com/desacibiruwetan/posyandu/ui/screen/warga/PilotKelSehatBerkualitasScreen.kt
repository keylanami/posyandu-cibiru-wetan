package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotKelSehatBerkualitasScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {
    ProgramMetricFormScreen(
        title = "Keluarga Sehat",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("keluarga_dengan_2_anak", "Keluarga Dengan 2 Anak"),
            ProgramMetricField("berobat_faskes", "Berobat Faskes"),
            ProgramMetricField("penyakit_menular", "Penyakit Menular"),
            ProgramMetricField("penyakit_tidak_menular", "Penyakit Tidak Menular"),
            ProgramMetricField("bayi_lahir_sehat", "Bayi Lahir Sehat"),
            ProgramMetricField("bayi_lahir_cukup_bulan", "Bayi Lahir Cukup Bulan"),
            ProgramMetricField("gangguan_jiwa_keluarga", "Gangguan Jiwa Keluarga")
        )
    )
}
