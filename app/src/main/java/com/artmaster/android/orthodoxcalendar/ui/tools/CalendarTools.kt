package com.artmaster.android.orthodoxcalendar.ui.tools

import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateDp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.DrawerValue
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Text
import androidx.compose.material.rememberDrawerState
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.theme.Background
import com.artmaster.android.orthodoxcalendar.ui.theme.DefaultTextColor
import com.artmaster.android.orthodoxcalendar.ui.theme.FiltersContentColor
import com.artmaster.android.orthodoxcalendar.ui.theme.FloatingButtonColorLight
import com.artmaster.android.orthodoxcalendar.ui.theme.ShadowColor
import com.artmaster.android.orthodoxcalendar.ui.tools.MultitoolState.COLLAPSED
import com.artmaster.android.orthodoxcalendar.ui.tools.MultitoolState.EXPANDED
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModelFake
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.ICalendarViewModel
import kotlinx.coroutines.launch

enum class Tabs {
    FILTERS, NEW_EVENT
}

private const val BUTTON_SIZE = 45f
private const val MAIN_BUTTON_ALPHA = 0.7f
private const val ICON_APPEARANCE_THRESHOLD = 0.6f
private const val ANIMATION_DURATION_MS = 500

@Preview(showBackground = true, device = Devices.PIXEL_3)
@Composable
fun ToolsPreview() {
    CalendarToolsDrawer(viewModel = CalendarViewModelFake()) {

    }
}

@Composable
fun CalendarToolsDrawer(
    viewModel: ICalendarViewModel,
    onToolClick: (MultiFabItem) -> Unit = {},
    content: @Composable () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    var isToolsPanelVisible by remember { mutableStateOf(true) }
    var multiToolState by remember { mutableStateOf(COLLAPSED) }

    val onToolItemClick = remember {
        { item: MultiFabItem ->
            if (multiToolState == EXPANDED) {
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
                multiToolState = COLLAPSED
                onToolClick(item)
            }
        }
    }

    LaunchedEffect(key1 = drawerState.currentValue) {
        if (drawerState.isClosed) isToolsPanelVisible = true
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            ModalDrawer(
                drawerState = drawerState,
                drawerBackgroundColor = Background,
                drawerContentColor = Background,
                drawerContent = {
                    FiltersLayoutWrapper(viewModel = viewModel)
                }
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
                    content()
                }
            }
        }
        Tools(
            parent = this,
            isVisible = isToolsPanelVisible,
            multiToolState = multiToolState,
            stateChanged = { multiToolState = it },
            onItemClicked = onToolItemClick
        )
    }
}

@Composable
fun Tools(
    parent: BoxScope,
    isVisible: Boolean = true,
    multiToolState: MultitoolState = COLLAPSED,
    stateChanged: (fabstate: MultitoolState) -> Unit = {},
    onItemClicked: (item: MultiFabItem) -> Unit = {}
) {

    MultiFloatingActionButton(
        parent = parent,
        fabIcon = painterResource(R.drawable.ic_baseline_arrow_drop_up_24),
        toState = multiToolState,
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
        stateChanged = stateChanged,
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
    toState: MultitoolState,
    showLabels: Boolean = true,
    stateChanged: (fabstate: MultitoolState) -> Unit,
    onFabItemClicked: (item: MultiFabItem) -> Unit,
    parent: BoxScope,
    visible: Boolean = true
) {
    parent.run {
        val transition: Transition<MultitoolState> =
            updateTransition(targetState = toState, label = "transition")

        val scale: Float by transition.animateFloat(
            label = "scale",
            transitionSpec = { tween(durationMillis = ANIMATION_DURATION_MS) }
        ) { state ->
            if (state == EXPANDED) (BUTTON_SIZE * 3) else 0f
        }

        val alpha: Float by transition.animateFloat(
            label = "alpha",
            transitionSpec = { tween(durationMillis = ANIMATION_DURATION_MS) }
        ) { state ->
            if (state == EXPANDED) 1f else 0f
        }
        val shadow: Dp by transition.animateDp(
            label = "shadow",
            transitionSpec = { tween(durationMillis = ANIMATION_DURATION_MS) }
        ) { state ->
            if (state == EXPANDED) 2.dp else 0.dp
        }
        val rotation: Float by transition.animateFloat(label = "rotation") { state ->
            if (state == EXPANDED) 180f else 0f
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
                Spacer(modifier = Modifier.height(16.dp))
            }
            FloatingActionButton(
                modifier = Modifier
                    .alpha(if (alpha < MAIN_BUTTON_ALPHA) MAIN_BUTTON_ALPHA else alpha)
                    .size(BUTTON_SIZE.dp)
                    .padding(bottom = 8.dp, end = 8.dp),
                backgroundColor = FloatingButtonColorLight,
                contentColor = FiltersContentColor,
                elevation = FloatingActionButtonDefaults.elevation(8.dp),
                onClick = {
                    stateChanged(
                        if (transition.currentState == EXPANDED) {
                            COLLAPSED
                        } else EXPANDED
                    )
                }) {
                Icon(
                    painter = fabIcon,
                    contentDescription = "",
                    modifier = Modifier
                        .rotate(rotation)
                        .scale(2f)
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
                fontSize = 25.sp,
                fontFamily = FontFamily(Font(R.font.cyrillic_old)),
                color = DefaultTextColor,
                modifier = Modifier
                    .alpha(
                        animateFloatAsState(
                            targetValue = alpha,
                            label = "floating btn alpha"
                        ).value
                    )
                    .shadow(
                        animateDpAsState(
                            targetValue = shadow,
                            label = "floating btn shadow"
                        ).value
                    )
                    .background(color = FloatingButtonColorLight)
                    .padding(start = 6.dp, end = 6.dp, top = 6.dp, bottom = 6.dp)
                    .clickable(onClick = { onFabItemClicked(item) })
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
            val calculatedBtnSize = (size.height / 1.5).toFloat()
            val radius = if (scale >= calculatedBtnSize) calculatedBtnSize else scale
            drawCircle(
                Color(ShadowColor.value),
                center = Offset(this.center.x + 2f, this.center.y + 7f),
                radius = radius,
                alpha = alpha
            )
            drawCircle(
                color = FloatingButtonColorLight,
                radius = radius,
                alpha = alpha
            )
            drawImage(
                item.icon,
                topLeft = Offset(
                    (this.center.x) - (item.icon.width / 2),
                    (this.center.y) - (item.icon.width / 2)
                ),
                alpha = if (alpha < ICON_APPEARANCE_THRESHOLD) 0f else alpha
            )
        }
    }
}