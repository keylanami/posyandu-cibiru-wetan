package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotSiagaKebakaraanScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {
    ProgramMetricFormScreen(
        title = "Siaga Kebakaran",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("kebakaran_rumah_tangga", "Kebakaran Rumah Tangga"),
            ProgramMetricField("kebakaran_non_rumah_tangga", "Kebakaran Non Rumah Tangga"),
            ProgramMetricField("rumah_punya_apar_atau_air", "Rumah Punya APAR atau Air"),
            ProgramMetricField("rumah_semi_permanen_kayu", "Rumah Semi Permanen Kayu"),
            ProgramMetricField("rumah_punya_p3k", "Rumah Punya P3K"),
            ProgramMetricField("kecelakaan_rumah_tangga", "Kecelakaan Rumah Tangga"),
            ProgramMetricField("instalasi_hydrant", "Instalasi Hydrant")
        )
    )
}
