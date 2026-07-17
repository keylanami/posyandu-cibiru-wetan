package com.desacibiruwetan.posyandu.ui.components.layout

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.desacibiruwetan.posyandu.ui.theme.PrimaryGreen
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

/**
 * An optimized, draggable floating scrollbar for LazyColumn lists.
 * Features a large touch target and pixel-precise dragging.
 */
@Composable
fun DraggableScrollbar(
    listState: LazyListState,
    modifier: Modifier = Modifier,
) {
    val coroutineScope = rememberCoroutineScope()
    var isDragging by remember { mutableStateOf(value = false) }
    val density = LocalDensity.current

    val scrollbarAlpha by animateFloatAsState(
        targetValue = when {
            isDragging -> 1f
            listState.isScrollInProgress -> 0.8f
            else -> 0.35f
        },
        label = "scrollbarAlpha",
    )

    val thumbWidth by animateDpAsState(
        targetValue = if (isDragging) 8.dp else 5.dp,
        label = "thumbWidth",
    )

    BoxWithConstraints(
        modifier = modifier
            .fillMaxHeight()
            .width(32.dp),
    ) {
        val totalItems by remember { derivedStateOf { listState.layoutInfo.totalItemsCount } }
        val visibleItems by remember { derivedStateOf { listState.layoutInfo.visibleItemsInfo.size.coerceAtLeast(1) } }
        val firstVisibleIndex by remember { derivedStateOf { listState.firstVisibleItemIndex } }

        if (totalItems > 0) {
            // Convert maxHeight (Dp) to pixels for precise math
            val trackHeightPx = with(density) { this@BoxWithConstraints.maxHeight.toPx() }
            
            // Calculate thumb height ratio
            val thumbHeightPx = ((visibleItems.toFloat() / totalItems) * trackHeightPx)
                .coerceAtLeast(120f) // Minimum 120px height for visibility

            val maxOffsetPx = trackHeightPx - thumbHeightPx

            // Calculate current scroll progress (0.0 to 1.0)
            val scrollProgress = if (totalItems > visibleItems) {
                firstVisibleIndex.toFloat() / (totalItems - visibleItems)
            } else {
                0f
            }

            // Internal pixel-based offset state
            var thumbOffsetPx by remember { mutableFloatStateOf(value = 0f) }
            
            // Sync with list scroll when NOT dragging
            if (!isDragging) {
                thumbOffsetPx = scrollProgress.coerceIn(0f, 1f) * maxOffsetPx
            }

            // Track Background (Subtle indicator)
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 4.dp)
                    .width(5.dp)
                    .fillMaxHeight()
                    .background(
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.03f),
                        shape = RoundedCornerShape(100),
                    ),
            )

            // Touch Target Container (Invisible 32dp width)
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset { IntOffset(0, thumbOffsetPx.roundToInt()) }
                    .width(32.dp)
                    .height(with(density) { thumbHeightPx.toDp() })
                    .pointerInput(totalItems, visibleItems) {
                        detectVerticalDragGestures(
                            onDragStart = { isDragging = true },
                            onDragEnd = { isDragging = false },
                            onDragCancel = { isDragging = false },
                        ) { change, dragAmount ->
                            change.consume()
                            
                            // Direct pixel manipulation for maximum smoothness
                            thumbOffsetPx = (thumbOffsetPx + dragAmount)
                                .coerceIn(0f, maxOffsetPx)
                            
                            val dragFraction = if (maxOffsetPx > 0) {
                                (thumbOffsetPx / maxOffsetPx).coerceIn(0f, 1f)
                            } else {
                                0f
                            }
                            
                            val targetItemIndex = (dragFraction * (totalItems - visibleItems)).toInt()
                            
                            coroutineScope.launch {
                                listState.scrollToItem(targetItemIndex.coerceIn(0, totalItems - 1))
                            }
                        }
                    },
            ) {
                // Visual Thumb (Thin 5-8dp)
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 4.dp)
                        .width(thumbWidth)
                        .fillMaxHeight()
                        .graphicsLayer { alpha = scrollbarAlpha }
                        .background(
                            color = PrimaryGreen,
                            shape = RoundedCornerShape(100),
                        ),
                )
            }
        }
    }
}
