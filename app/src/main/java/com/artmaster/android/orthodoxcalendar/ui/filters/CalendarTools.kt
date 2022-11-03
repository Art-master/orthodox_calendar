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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.*


@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun ToolsPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Tools(parent = this)
    }
}

@Composable
fun Tools(parent: BoxScope) {
    parent.run {
        var state by remember { mutableStateOf(MultiFabState.EXPANDED) }

        MultiFloatingActionButton(
            parent = parent,
            fabIcon = painterResource(R.drawable.ic_baseline_arrow_drop_up_24),
            toState = state,
            items = listOf(
                MultiFabItem(
                    identifier = "1",
                    icon = ImageBitmap.imageResource(R.drawable.ic_add_button),
                    label = "Добавить праздник"
                ),
                MultiFabItem(
                    identifier = "2",
                    icon = ImageBitmap.imageResource(R.drawable.ic_filter),
                    label = "Фильтры"
                )
            ),
            onFabItemClicked = {

            },
            stateChanged = { state = it }
        )
    }
}

class MultiFabItem(
    val identifier: String,
    val icon: ImageBitmap,
    val label: String
)

@Composable
fun MultiFloatingActionButton(
    fabIcon: Painter,
    items: List<MultiFabItem>,
    toState: MultiFabState,
    showLabels: Boolean = true,
    stateChanged: (fabstate: MultiFabState) -> Unit,
    onFabItemClicked: (item: MultiFabItem) -> Unit,
    parent: BoxScope
) {
    parent.run {
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
            if (state == MultiFabState.EXPANDED) 180f else 0f
        }
        Column(
            modifier = Modifier.align(Alignment.BottomEnd),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Arrangement.Bottom
        ) {
            items.forEach { item ->
                MiniFabItem(item, alpha, shadow, scale, showLabels, onFabItemClicked)
                Spacer(modifier = Modifier.height(20.dp))
            }
            FloatingActionButton(
                modifier = Modifier
                    .alpha(0.5f)
                    .size(45.dp)
                    .padding(bottom = 8.dp, end = 8.dp),
                backgroundColor = FloatingButtonColor,
                contentColor = FiltersContentColor,
                onClick = {
                    stateChanged(
                        if (transition.currentState == MultiFabState.EXPANDED) {
                            MultiFabState.COLLAPSED
                        } else MultiFabState.EXPANDED
                    )
                }) {
                Icon(
                    painter = fabIcon,
                    contentDescription = "",
                    modifier = Modifier.rotate(rotation)
                )
            }
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
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(end = 12.dp)
    ) {
        if (showLabel) {
            Text(
                item.label,
                fontSize = 16.sp,
                fontFamily = FontFamily(Font(R.font.cyrillic_old)),
                color = DefaultTextColor,
                modifier = Modifier
                    .alpha(animateFloatAsState(targetValue = alpha).value)
                    .shadow(animateDpAsState(targetValue = shadow).value)
                    .background(color = FiltersLabelColor)
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
                        color = FloatingButtonColorLight
                    ),
                    interactionSource = interactionSource
                )
                .clickable(onClick = { onFabItemClicked(item) })
        ) {
            drawCircle(
                Color(ShadowColor.value),
                center = Offset(this.center.x + 2f, this.center.y + 7f),
                radius = scale
            )
            drawCircle(color = FloatingButtonColorLight, scale)
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