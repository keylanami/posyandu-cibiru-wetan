package com.desacibiruwetan.posyandu.ui.components.layout

import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.components.bar.AppNavBar
import com.desacibiruwetan.posyandu.ui.components.bar.AppTopBar
import com.desacibiruwetan.posyandu.ui.components.feedback.AppSnackbarHost
import com.desacibiruwetan.posyandu.ui.theme.BgMint

@Composable
fun AppScreenScaffold(
    title: String,
    onBackClick: (() -> Unit)? = null,
    selectedNavIndex: Int? = null,
    onNavItemSelected: ((Int) -> Unit)? = null,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    floatingActionButton: @Composable () -> Unit = {},
    contentPadding: PaddingValues = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
    content: @Composable ColumnScope.(PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            if (onBackClick != null) {
                AppTopBar(title = title, onBackClick = onBackClick)
            }
        },
        bottomBar = {
            if (selectedNavIndex != null && onNavItemSelected != null) {
                AppNavBar(selectedIndex = selectedNavIndex, onItemSelected = onNavItemSelected)
            }
        },
        snackbarHost = { AppSnackbarHost(snackbarHostState) },
        floatingActionButton = floatingActionButton,
        containerColor = BgMint
    ) { scaffoldPadding ->
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(scaffoldPadding)
                .padding(contentPadding)
        ) {
            content(scaffoldPadding)
        }
    }
}
