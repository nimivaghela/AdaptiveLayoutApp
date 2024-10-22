package com.example.androidadaptivelayout

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.shrinkOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androidadaptivelayout.ui.theme.AndroidAdaptiveLayoutTheme

class PaneExpansionWithCustomAnimationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AndroidAdaptiveLayoutTheme {
//                AdaptiveDraggablePaneLayoutUsingCustomAnimation() // Section 3a
//                AdaptiveDraggablePaneLayoutExpandShrinking() // Section 3b
                AdaptiveDraggablePaneLayoutFadeInFadeOut() // Section 3c
            }
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdaptiveDraggablePaneLayoutExpandShrinking() {
    // State for the width of the left pane
    var leftPaneWidth by remember { mutableStateOf(360.dp) }
    // States for pane visibility
    var isLeftPaneVisible by remember { mutableStateOf(true) }
    var isRightPaneVisible by remember { mutableStateOf(true) }

    // Get current screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Constants for the anchors
    val startAnchor = 360.dp
    val endAnchor = 360.dp
    val totalWidth = screenWidth // Use dynamic width based on the screen size

    Row(Modifier.fillMaxSize()) {
        // Left Pane with Default Motion (Fade and Slide)
        AnimatedVisibility(
            visible = isLeftPaneVisible,
            enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -it }),
            exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -it })
        ) {
            Box(
                modifier = Modifier
                    .width(leftPaneWidth)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            ) {
                Text("Left Pane", modifier = Modifier.padding(16.dp))
            }
        }

        // Divider
        Box(modifier = Modifier
            .width(8.dp)
            .fillMaxHeight()
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    // Calculate new width of the left pane
                    leftPaneWidth = (leftPaneWidth + dragAmount.x.toDp()).coerceIn(
                        startAnchor, totalWidth - endAnchor
                    )
                }
            })

        // Right Pane with Expanding/Shrinking Animation
        AnimatedVisibility(
            visible = isRightPaneVisible,
            enter = expandIn(animationSpec = tween(300)), // Expanding effect when entering
            exit = shrinkOut(animationSpec = tween(300)) // Shrinking effect when exiting
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Gray)
            ) {
                Text("Right Pane", modifier = Modifier.padding(16.dp))
            }
        }
    }

    // Anchor adjustment for the left pane width
    LaunchedEffect(leftPaneWidth) {
        val parentWidth = totalWidth.value // Total width of the layout

        when {
            leftPaneWidth < startAnchor -> {
                leftPaneWidth = startAnchor
            }

            leftPaneWidth > (parentWidth.dp - endAnchor) -> {
                leftPaneWidth = (parentWidth - endAnchor.value).dp
            }

            leftPaneWidth < (parentWidth * 0.5).dp -> {
                leftPaneWidth = (parentWidth * 0.5).dp
            }
        }
    }

    // Example buttons to toggle visibility of panes (optional)
    Button(onClick = { isLeftPaneVisible = !isLeftPaneVisible }) {
        Text("Toggle Left Pane")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = { isRightPaneVisible = !isRightPaneVisible }) {
        Text("Toggle Right Pane")
    }
}


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdaptiveDraggablePaneLayoutUsingCustomAnimation() {
    // State for the width of the left pane
    var leftPaneWidth by remember { mutableStateOf(360.dp) }
    // States for pane visibility
    var isLeftPaneVisible by remember { mutableStateOf(true) }
    var isRightPaneVisible by remember { mutableStateOf(true) }

    // Get current screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Constants for the anchors
    val startAnchor = 360.dp
    val endAnchor = 360.dp
    val totalWidth = screenWidth // Use dynamic width based on the screen size

    Row(Modifier.fillMaxSize()) {
        // Left Pane with Customized Animation
        AnimatedVisibility(
            visible = isLeftPaneVisible, enter = if (screenWidth > 600.dp) { // Large screen
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -it }) + expandIn()
            } else { // Small screen
                fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f) // Scale effect for small screens
            }, exit = if (screenWidth > 600.dp) {
                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -it }) + shrinkOut()
            } else {
                fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f) // Scale effect for small screens
            }
        ) {
            Box(
                modifier = Modifier
                    .width(leftPaneWidth)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            ) {
                Text("Left Pane", modifier = Modifier.padding(16.dp))
            }
        }

        // Divider
        Box(modifier = Modifier
            .width(8.dp)
            .fillMaxHeight()
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    // Calculate new width of the left pane
                    leftPaneWidth = (leftPaneWidth + dragAmount.x.toDp()).coerceIn(
                        startAnchor, totalWidth - endAnchor
                    )
                }
            })

        // Right Pane with Customized Animation
        AnimatedVisibility(
            visible = isRightPaneVisible, enter = if (screenWidth > 600.dp) { // Large screen
                fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { it }) + expandIn()
            } else { // Small screen
                fadeIn(animationSpec = tween(300)) + scaleIn(initialScale = 0.8f) // Scale effect for small screens
            }, exit = if (screenWidth > 600.dp) {
                fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { it }) + shrinkOut()
            } else {
                fadeOut(animationSpec = tween(300)) + scaleOut(targetScale = 0.8f) // Scale effect for small screens
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Gray)
            ) {
                Text("Right Pane", modifier = Modifier.padding(16.dp))
            }
        }
    }

    // Anchor adjustment for the left pane width
    LaunchedEffect(leftPaneWidth) {
        val parentWidth = totalWidth.value // Total width of the layout

        when {
            leftPaneWidth < startAnchor -> {
                leftPaneWidth = startAnchor
            }

            leftPaneWidth > (parentWidth.dp - endAnchor) -> {
                leftPaneWidth = (parentWidth - endAnchor.value).dp
            }

            leftPaneWidth < (parentWidth * 0.5).dp -> {
                leftPaneWidth = (parentWidth * 0.5).dp
            }
        }
    }

    // Example buttons to toggle visibility of panes (optional)
    Button(onClick = { isLeftPaneVisible = !isLeftPaneVisible }) {
        Text("Toggle Left Pane")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = { isRightPaneVisible = !isRightPaneVisible }) {
        Text("Toggle Right Pane")
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdaptiveDraggablePaneLayoutFadeInFadeOut() {
    // State for the width of the left pane
    var leftPaneWidth by remember { mutableStateOf(360.dp) }
    // States for pane visibility
    var isLeftPaneVisible by remember { mutableStateOf(true) }
    var isRightPaneVisible by remember { mutableStateOf(true) }

    // Get current screen width
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    // Constants for the anchors
    val startAnchor = 360.dp
    val endAnchor = 360.dp
    val totalWidth = screenWidth // Use dynamic width based on the screen size

    Row(Modifier.fillMaxSize()) {
        // Left Pane with Default Motion (Fade and Slide)
        AnimatedVisibility(
            visible = isLeftPaneVisible,
            enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -it }),
            exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -it })
        ) {
            Box(
                modifier = Modifier
                    .width(leftPaneWidth)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            ) {
                Text("Left Pane", modifier = Modifier.padding(16.dp))
            }
        }

        // Divider
        Box(modifier = Modifier
            .width(8.dp)
            .fillMaxHeight()
            .background(Color.DarkGray)
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    change.consume()

                    // Calculate new width of the left pane
                    leftPaneWidth = (leftPaneWidth + dragAmount.x.toDp()).coerceIn(
                        startAnchor, totalWidth - endAnchor
                    )
                }
            })

        // Right Pane with Fading Animation
        AnimatedVisibility(
            visible = isRightPaneVisible,
            enter = fadeIn(animationSpec = tween(300)), // Fade-in effect when entering
            exit = fadeOut(animationSpec = tween(300)) // Fade-out effect when exiting
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Gray)
            ) {
                Text("Right Pane", modifier = Modifier.padding(16.dp))
            }
        }
    }

    // Anchor adjustment for the left pane width
    LaunchedEffect(leftPaneWidth) {
        val parentWidth = totalWidth.value // Total width of the layout

        when {
            leftPaneWidth < startAnchor -> {
                leftPaneWidth = startAnchor
            }

            leftPaneWidth > (parentWidth.dp - endAnchor) -> {
                leftPaneWidth = (parentWidth - endAnchor.value).dp
            }

            leftPaneWidth < (parentWidth * 0.5).dp -> {
                leftPaneWidth = (parentWidth * 0.5).dp
            }
        }
    }

    // Example buttons to toggle visibility of panes (optional)
    Button(onClick = { isLeftPaneVisible = !isLeftPaneVisible }) {
        Text("Toggle Left Pane")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = { isRightPaneVisible = !isRightPaneVisible }) {
        Text("Toggle Right Pane")
    }
}

/*@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AdaptiveDraggablePaneLayout() {
    // State for the width of the left pane
    var leftPaneWidth by remember { mutableStateOf(360.dp) }
    // States for pane visibility
    var isLeftPaneVisible by remember { mutableStateOf(true) }
    var isRightPaneVisible by remember { mutableStateOf(true) }

    // Constants for the anchors
    val startAnchor = 360.dp
    val endAnchor = 360.dp
    val totalWidth =
        LocalConfiguration.current.screenWidthDp.dp // Dynamic width based on the screen size

    Row(Modifier.fillMaxSize()) {
        // Left Pane with Animation
        AnimatedVisibility(
            visible = isLeftPaneVisible,
            enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { -it }) + expandIn(),
            exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { -it }) + shrinkOut()
        ) {
            Box(
                modifier = Modifier
                    .width(leftPaneWidth)
                    .fillMaxHeight()
                    .background(Color.LightGray)
            ) {
                Text("Left Pane", modifier = Modifier.padding(16.dp))
            }
        }

        // Divider
        Box(
            modifier = Modifier
                .width(8.dp)
                .fillMaxHeight()
                .background(Color.DarkGray)
                .pointerInput(Unit) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()

                        // Calculate new width of the left pane
                        leftPaneWidth = (leftPaneWidth + dragAmount.x.toDp()).coerceIn(
                            startAnchor,
                            totalWidth - endAnchor
                        )
                    }
                }
        )

        // Right Pane with Animation
        AnimatedVisibility(
            visible = isRightPaneVisible,
            enter = fadeIn(animationSpec = tween(300)) + slideInHorizontally(initialOffsetX = { it }) + expandIn(),
            exit = fadeOut(animationSpec = tween(300)) + slideOutHorizontally(targetOffsetX = { it }) + shrinkOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .background(Color.Gray)
            ) {
                Text("Right Pane", modifier = Modifier.padding(16.dp))
            }
        }
    }

    // Anchor adjustment for the left pane width
    LaunchedEffect(leftPaneWidth) {
        val parentWidth = totalWidth.value // Total width of the layout

        when {
            leftPaneWidth < startAnchor -> {
                leftPaneWidth = startAnchor
            }

            leftPaneWidth > (parentWidth.dp - endAnchor) -> {
                leftPaneWidth = (parentWidth - endAnchor.value).dp
            }

            leftPaneWidth < (parentWidth * 0.5).dp -> {
                leftPaneWidth = (parentWidth * 0.5).dp
            }
        }
    }

    // Example buttons to toggle visibility of panes (optional)
    Button(onClick = { isLeftPaneVisible = !isLeftPaneVisible }) {
        Text("Toggle Left Pane")
    }
    Spacer(modifier = Modifier.height(8.dp))
    Button(onClick = { isRightPaneVisible = !isRightPaneVisible }) {
        Text("Toggle Right Pane")
    }
}*/

// Extension function to convert Float to Dp
fun Float.toDp() = this.dp
