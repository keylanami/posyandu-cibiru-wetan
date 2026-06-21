package com.desacibiruwetan.posyandu.ui.screen.warga

import androidx.compose.runtime.Composable
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricField
import com.desacibiruwetan.posyandu.ui.components.program.ProgramMetricFormScreen

@Composable
fun PilotKeuanganSehatScreen(
    onBackClick: () -> Unit,
    onNavItemSelected: (Int) -> Unit,
    userName: String
) {
    ProgramMetricFormScreen(
        title = "Keuangan Sehat",
        onBackClick = onBackClick,
        onNavItemSelected = onNavItemSelected,
        fields = listOf(
            ProgramMetricField("keluarga_asuransi_kesehatan", "Keluarga Asuransi Kesehatan"),
            ProgramMetricField("kepala_keluarga_pengangguran", "Kepala Keluarga Pengangguran"),
            ProgramMetricField("kepala_keluarga_tidak_tetap", "Kepala Keluarga Tidak Tetap"),
            ProgramMetricField("kepala_keluarga_penghasilan_tetap", "Kepala Keluarga Penghasilan Tetap"),
            ProgramMetricField("tabulin_ibu_hamil", "Tabulin Ibu Hamil"),
            ProgramMetricField("keluarga_punya_tabungan", "Keluarga Punya Tabungan"),
            ProgramMetricField("keluarga_punya_aset_investasi", "Keluarga Punya Aset Investasi")
        )
    )
}
