package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotPHBSScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    ProgramMetricFormScreen(
        title = "PHBS",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("patuh_protokol_kesehatan", "Patuh Protokol Kesehatan"),
            ProgramMetricField("rumah_jamban_sehat", "Rumah Jamban Sehat"),
            ProgramMetricField("rumah_air_bersih", "Rumah Air Bersih"),
            ProgramMetricField("kasus_diare", "Kasus Diare"),
            ProgramMetricField("rumah_tanpa_asap_rokok", "Rumah Tanpa Asap Rokok"),
            ProgramMetricField("babs", "BABS")
        )
    )
}
