package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotKesehatanPusScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    ProgramMetricFormScreen(
        title = "Kesehatan PUS",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("ibu_melahirkan_bayi_sehat", "Ibu Melahirkan Bayi Sehat"),
            ProgramMetricField("kb_wanita", "KB Wanita"),
            ProgramMetricField("kb_pria", "KB Pria"),
            ProgramMetricField("pus_masalah_reproduksi", "PUS Masalah Reproduksi"),
            ProgramMetricField("menikah_usia_dibawah_20", "Menikah Usia Dibawah 20"),
            ProgramMetricField("wus_kehamilan_berisiko", "WUS Kehamilan Berisiko"),
            ProgramMetricField("ims_pada_pus", "IMS Pada PUS")
        )
    )
}
