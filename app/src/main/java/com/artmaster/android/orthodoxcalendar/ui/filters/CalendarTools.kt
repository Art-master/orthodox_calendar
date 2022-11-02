package com.artmaster.android.orthodoxcalendar.ui.filters

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.FiltersContentColor
import com.artmaster.android.orthodoxcalendar.ui.theme.FloatingButtonColor


@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun ToolsPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Tools(parent = this)
    }
}

@Composable
fun Tools(parent: BoxScope) {
    var toState by remember { mutableStateOf(MultiFabState.COLLAPSED) }

    parent.run {
        FloatingActionButton(
            modifier = Modifier
                .alpha(0.5f)
                .size(50.dp)
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            onClick = { /*TODO*/ },
            backgroundColor = FloatingButtonColor,
            contentColor = FiltersContentColor
        ) {
            Icon(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(R.drawable.ic_baseline_arrow_drop_up_24),
                contentDescription = ""
            )
        }
    }
}

class MultiFabItem(
    val identifier: String,
    val icon: ImageBitmap,
    val label: String
)

@Composable
fun MultiFloatingActionButton(
    fabIcon: ImageBitmap,
    items: List<MultiFabItem>,
    toState: MultiFabState,
    showLabels: Boolean = true,
    stateChanged: (fabstate: MultiFabState) -> Unit,
    onFabItemClicked: (item: MultiFabItem) -> Unit
) {
    val transition: Transition<MultiFabState> =
        updateTransition(targetState = toState, label = "transition")

    val scale: Float by transition.animateFloat(label = "scale") { state ->
        if (state == MultiFabState.EXPANDED) 56f else 0f
    }

    val alpha: Float by transition.animateFloat(
        label = "alpha",
        transitionSpec = {
            tween(durationMillis = 50)
        }
    ) { state ->
        if (state == MultiFabState.EXPANDED) 1f else 0f
    }
    val shadow: Dp by transition.animateDp(
        label = "shadow",
        transitionSpec = {
            tween(durationMillis = 50)
        }
    ) { state ->
        if (state == MultiFabState.EXPANDED) 2.dp else 0.dp
    }
    val rotation: Float by transition.animateFloat(label = "rotation") { state ->
        if (state == MultiFabState.EXPANDED) 45f else 0f
    }
    Column(horizontalAlignment = Alignment.End) {
        items.forEach { item ->
            MiniFabItem(item, alpha, shadow, scale, showLabels, onFabItemClicked)
            Spacer(modifier = Modifier.height(20.dp))
        }
        FloatingActionButton(onClick = {
            stateChanged(
                if (transition.currentState == MultiFabState.EXPANDED) {
                    MultiFabState.COLLAPSED
                } else MultiFabState.EXPANDED
            )
        }) {
            Icon(
                bitmap = fabIcon,
                contentDescription = "",
                modifier = Modifier.rotate(rotation)
            )
        }
    }
}

@Composable
private fun MiniFabItem(
    item: MultiFabItem,
    alpha: Float,
    shadow: Dp,
    scale: Float,
    showLabel: Boolean,
    onFabItemClicked: (item: MultiFabItem) -> Unit
) {
    val fabColor = MaterialTheme.colors.secondary
    val ctx = LocalContext.current
    val shadowColor = ContextCompat.getColor(ctx, R.color.colorBlack)

    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        if (showLabel) {
            Text(
                item.label,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .alpha(animateFloatAsState(targetValue = alpha).value)
                    .shadow(animateDpAsState(targetValue = shadow).value)
                    .background(color = MaterialTheme.colors.surface)
                    .padding(start = 6.dp, end = 6.dp, top = 4.dp, bottom = 4.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
        }
        Canvas(
            modifier = Modifier
                .size(32.dp)
                .indication(
                    indication = rememberRipple(
                        bounded = false,
                        radius = 20.dp,
                        color = MaterialTheme.colors.onSecondary
                    ),
                    interactionSource = interactionSource
                )
                .clickable(onClick = { onFabItemClicked(item) })
        ) {
            drawCircle(
                Color(shadowColor),
                center = Offset(this.center.x + 2f, this.center.y + 7f),
                radius = scale
            )
            drawCircle(color = fabColor, scale)
            drawImage(
                item.icon,
                topLeft = Offset(
                    (this.center.x) - (item.icon.width / 2),
                    (this.center.y) - (item.icon.width / 2)
                ),
                alpha = alpha
            )
        }
    }
}