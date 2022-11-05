package com.artmaster.android.orthodoxcalendar.ui.filters

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.*
import kotlinx.coroutines.launch


enum class Tabs {
    FILTERS, NEW_EVENT
}

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun ToolsPreview() {
    Box(modifier = Modifier.fillMaxSize()) {
        Tools(parent = this, isVisible = true)
    }
}

@Composable
fun CalendarToolsDrawer(viewModel: CalendarViewModel, content: @Composable () -> Unit) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isToolsPanelVisible by remember { mutableStateOf(true) }

    val onToolItemClick = remember {
        { item: MultiFabItem ->
            when (item.identifier) {
                Tabs.FILTERS.name -> {
                    coroutineScope.launch {
                        drawerState.open()
                    }
                }
                Tabs.NEW_EVENT.name -> {}
                else -> throw IllegalStateException("Wrong item name")
            }
            isToolsPanelVisible = false
        }
    }

    LaunchedEffect(key1 = drawerState.currentValue) {
        if (drawerState.isClosed) isToolsPanelVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            ModalDrawer(
                drawerState = drawerState,
                drawerBackgroundColor = Background,
                drawerContentColor = Background,
                // drawerShape = drawerShape(),
                drawerContent = {
                    FiltersLayoutWrapper(viewModel = viewModel)
                }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    content()
                }
            }
        }
        Tools(parent = this, isVisible = isToolsPanelVisible, onItemClicked = onToolItemClick)
    }
}

fun drawerShape() = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Rectangle(
            Rect(
                size.width - 800,
                size.height - 1200,
                size.width /* width */,
                size.height /* height */
            )
        )
    }
}

@Composable
fun Tools(
    parent: BoxScope,
    isVisible: Boolean = true,
    onItemClicked: (item: MultiFabItem) -> Unit = {}
) {
    var state by remember { mutableStateOf(MultiFabState.COLLAPSED) }

    MultiFloatingActionButton(
        parent = parent,
        fabIcon = painterResource(R.drawable.ic_baseline_arrow_drop_up_24),
        toState = state,
        items = listOf(
            MultiFabItem(
                identifier = Tabs.NEW_EVENT.name,
                icon = ImageBitmap.imageResource(R.drawable.ic_add_button),
                label = stringResource(id = R.string.add_holiday)
            ),
            MultiFabItem(
                identifier = Tabs.FILTERS.name,
                icon = ImageBitmap.imageResource(R.drawable.ic_filter),
                label = stringResource(id = R.string.filters_title)
            )
        ),
        onFabItemClicked = onItemClicked,
        stateChanged = { state = it },
        visible = isVisible
    )
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
    parent: BoxScope,
    visible: Boolean = true
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
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .alpha(if (visible) 1f else 0f),
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