package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotPeduliLingkunganScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit
) {
    ProgramMetricFormScreen(
        title = "Peduli Lingkungan",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("keluarga_punya_bak_sampah", "Keluarga Punya Bak Sampah"),
            ProgramMetricField("anggota_bank_sampah", "Anggota Bank Sampah"),
            ProgramMetricField("keluarga_pakai_spal", "Keluarga Pakai SPAL"),
            ProgramMetricField("kasus_banjir", "Kasus Banjir"),
            ProgramMetricField("bak_sampah_desa", "Bak Sampah Desa"),
            ProgramMetricField("rumah_ventilasi_baik", "Rumah Ventilasi Baik"),
            ProgramMetricField("klb", "KLB")
        )
    )
}
