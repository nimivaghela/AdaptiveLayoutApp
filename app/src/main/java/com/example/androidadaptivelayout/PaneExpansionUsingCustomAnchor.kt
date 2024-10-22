package com.example.androidadaptivelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import com.example.androidadaptivelayout.ui.theme.AndroidAdaptiveLayoutTheme

// Section 1 - Draggable Pane Layout
// Section 2 - Predictive Back Support

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAdaptiveLayoutTheme {
                AdaptiveDraggableLayout()
            }
        }
    }
}


@Composable
fun AdaptiveDraggableLayout() {
    // State for the drag amount
    var dragOffset by remember { mutableFloatStateOf(0f) }
    val minDragLimit = 360.dp // 360.dp from the start
    val maxDragLimit = with(LocalDensity.current) { 360.dp } // 360.dp from the end
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Pane sizes and draggable behavior
    val firstPaneWidth = remember(screenWidth, dragOffset) {
        max(minDragLimit, min(dragOffset.dp, screenWidth - maxDragLimit))
    }
    val secondPaneWidth = remember(screenWidth, firstPaneWidth) {
        screenWidth - firstPaneWidth
    }

    Row(modifier = Modifier.fillMaxSize()) {
        // First pane
        Box(
            modifier = Modifier
                .width(with(LocalDensity.current) { firstPaneWidth })
                .fillMaxHeight()
                .background(Color.Blue),
        ) {
            Text(text = "First Pane", modifier = Modifier.align(Alignment.Center))
        }

        // Drag handle
        Box(
            modifier = Modifier
                .width(10.dp)
                .fillMaxHeight()
                .draggable(
                    state = rememberDraggableState { delta ->
                        dragOffset += delta
                        dragOffset = (max(
                            minDragLimit,
                            min(dragOffset.dp, screenWidth - maxDragLimit)
                        )).value

                    },
                    orientation = Orientation.Horizontal
                )
                .background(Color.Gray),
        )

        // Second pane
        Box(
            modifier = Modifier
                .width(with(LocalDensity.current) { secondPaneWidth })
                .fillMaxHeight()
                .background(Color.Green),
        ) {
            Text(text = "Second Pane", modifier = Modifier.align(Alignment.Center))
        }
    }
}



