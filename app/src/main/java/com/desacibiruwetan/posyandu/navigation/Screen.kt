package com.desacibiruwetan.posyandu.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Personalization : Screen("personalization")
    object Dashboard : Screen("dashboard")
    object Warga : Screen("warga")
    object Profil : Screen("profil")
    object Riwayat : Screen("riwayat")

    object TambahWarga : Screen("tambah_warga")
    object DetailWarga : Screen("detail_warga")
    object UpdateBalita : Screen("update_balita")
    object UpdateKb : Screen("update_kb")
    object UpdateWusPus : Screen("update_wuspus")
    object CatatKejadian : Screen("catat_kejadian")


    object AdministrasiRt : Screen("administrasi_rt")
    object RumahKeluarga : Screen("rumah_keluarga")
    object UpdateBumil : Screen("update_bumil")

    // Routes with arguments
    fun routeWithId(route: String, id: Int) = "$route/$id"

    object PilotStunting : Screen("pilot_stunting")
    object PilotPhbs : Screen("pilot_phbs")
    object PilotKia : Screen("pilot_kia")
    object PilotKebakaran : Screen("pilot_kebakaran")
    object PilotBencana : Screen("pilot_bencana")
    object PilotLingkungan : Screen("pilot_lingkungan")
    object PilotKeluargaSehat : Screen("pilot_keluarga_sehat")
    object PilotKeuangan : Screen("pilot_keuangan")
    object PilotKesehatanPus : Screen("pilot_kesehatan_pus")
}