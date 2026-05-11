package com.desacibiruwetan.posyandu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.desacibiruwetan.posyandu.ui.screen.auth.LoginScreenWrapper
import com.desacibiruwetan.posyandu.ui.theme.PosyanduCibiruTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PosyanduCibiruTheme {
                LoginScreenWrapper()
            }
        }
    }
}
