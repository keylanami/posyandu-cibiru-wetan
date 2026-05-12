package com.desacibiruwetan.posyandu.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Personalization : Screen("personalization")
    object Dashboard : Screen("dashboard")
    object Warga : Screen("warga")
}