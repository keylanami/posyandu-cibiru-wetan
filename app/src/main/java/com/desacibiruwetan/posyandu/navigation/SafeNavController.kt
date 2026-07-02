package com.desacibiruwetan.posyandu.navigation

import androidx.navigation.NavController
import androidx.navigation.NavOptionsBuilder

/**
 * A wrapper for NavController to prevent rapid multiple clicks (navigation race conditions).
 * It ignores navigation requests that happen within a short [threshold] period.
 */
class SafeNavController(private val navController: NavController) {
    private var lastNavTime = 0L
    private val threshold = 500L

    fun navigate(route: String, builder: NavOptionsBuilder.() -> Unit = {}) {
        val currentTime = System.currentTimeMillis()
        if ((currentTime - lastNavTime) > threshold) {
            lastNavTime = currentTime
            navController.navigate(route, builder)
        }
    }

    fun popBackStack(): Boolean {
        val currentTime = System.currentTimeMillis()
        // Only pop if there is something to pop to prevent white screen
        if (navController.previousBackStackEntry == null) return false
        
        if ((currentTime - lastNavTime) > threshold) {
            lastNavTime = currentTime
            return navController.popBackStack()
        }
        return false
    }
}
