package com.desacibiruwetan.posyandu.ui.components.layout

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val CompactBreakpoint = 360.dp

@Composable
fun screenHorizontalPadding(): Dp =
    if (LocalConfiguration.current.screenWidthDp.dp < CompactBreakpoint) 14.dp else 20.dp

@Composable
fun responsiveContentPadding(vertical: Dp = 16.dp): PaddingValues =
    PaddingValues(horizontal = screenHorizontalPadding(), vertical = vertical)

@Composable
fun Modifier.responsiveScreenPadding(vertical: Dp = 0.dp): Modifier =
    padding(horizontal = screenHorizontalPadding(), vertical = vertical)

@Composable
fun ResponsiveTwoColumn(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 12.dp,
    verticalSpacing: Dp = 12.dp,
    breakpoint: Dp = CompactBreakpoint,
    first: @Composable (Modifier) -> Unit,
    second: @Composable (Modifier) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (maxWidth < breakpoint) {
            Column(verticalArrangement = Arrangement.spacedBy(verticalSpacing)) {
                first(Modifier.fillMaxWidth())
                second(Modifier.fillMaxWidth())
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)) {
                first(Modifier.weight(1f))
                second(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ResponsiveActionRow(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 10.dp,
    verticalSpacing: Dp = 10.dp,
    breakpoint: Dp = CompactBreakpoint,
    first: @Composable (Modifier) -> Unit,
    second: @Composable (Modifier) -> Unit
) {
    ResponsiveTwoColumn(
        modifier = modifier,
        horizontalSpacing = horizontalSpacing,
        verticalSpacing = verticalSpacing,
        breakpoint = breakpoint,
        first = first,
        second = second
    )
}

@Composable
fun ResponsiveThreeColumn(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 10.dp,
    verticalSpacing: Dp = 10.dp,
    breakpoint: Dp = CompactBreakpoint,
    first: @Composable (Modifier) -> Unit,
    second: @Composable (Modifier) -> Unit,
    third: @Composable (Modifier) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (maxWidth < breakpoint) {
            Column(verticalArrangement = Arrangement.spacedBy(verticalSpacing)) {
                first(Modifier.fillMaxWidth())
                second(Modifier.fillMaxWidth())
                third(Modifier.fillMaxWidth())
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)) {
                first(Modifier.weight(1f))
                second(Modifier.weight(1f))
                third(Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun ResponsiveFourColumn(
    modifier: Modifier = Modifier,
    horizontalSpacing: Dp = 10.dp,
    verticalSpacing: Dp = 10.dp,
    breakpoint: Dp = CompactBreakpoint,
    first: @Composable (Modifier) -> Unit,
    second: @Composable (Modifier) -> Unit,
    third: @Composable (Modifier) -> Unit,
    four: @Composable (Modifier) -> Unit
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        if (maxWidth < breakpoint) {
            Column(verticalArrangement = Arrangement.spacedBy(verticalSpacing)) {
                first(Modifier.fillMaxWidth())
                second(Modifier.fillMaxWidth())
                third(Modifier.fillMaxWidth())
                four(Modifier.fillMaxWidth())
            }
        } else {
            Row(horizontalArrangement = Arrangement.spacedBy(horizontalSpacing)) {
                first(Modifier.weight(1f))
                second(Modifier.weight(1f))
                third(Modifier.weight(1f))
                four(Modifier.weight(1f))
            }
        }
    }
}

