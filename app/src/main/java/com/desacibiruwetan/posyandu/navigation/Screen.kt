package com.desacibiruwetan.posyandu.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Personalization : Screen("personalization")
    object Dashboard : Screen("dashboard")
    object Warga : Screen("warga")

    object Riwayat : Screen("riwayat")

    object TambahWarga : Screen("tambah_warga")
    object DetailWarga : Screen("detail_warga")
    object UpdateBalita : Screen("update_balita")
    object UpdateKb : Screen("update_kb")
    object UpdateWusPus : Screen("update_wuspus")
    object CatatKejadian : Screen("catat_kejadian")
}