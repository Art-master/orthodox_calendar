package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.Navigation
import com.artmaster.android.orthodoxcalendar.ui.theme.SelectedItemColor
import com.artmaster.android.orthodoxcalendar.ui.theme.TopBarColor
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModel
import kotlinx.coroutines.launch

enum class Item {
    CALENDAR, RESET, SETTINGS, INFO
}

enum class CalendarType {
    TILE, LIST
}

val APP_BAR_HEIGHT = 49.dp

@Preview
@Composable
fun AppBarPreview() {
    val navController = rememberNavController()
    val year = remember { mutableStateOf(2022) }
    AppBar(
        year = year,
        initCalendarType = CalendarType.TILE,
        navController = navController,
        forceVisibility = true,
        onTimeReset = {},
        onYearChange = {}
    )
}

@Composable
fun AppBarWrapper(
    viewModel: CalendarViewModel = CalendarViewModel(),
    navController: NavHostController,
    snackState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()
    val timeResetMsg = stringResource(id = R.string.time_has_reset_msg)

    val onYearChange = remember {
        { year: Int ->
            viewModel.setYear(year)
        }
    }

    val onTimeReset = remember {
        {
            viewModel.resetTime()
            coroutineScope.launch {
                snackState.showSnackbar(timeResetMsg)
            }
            Unit
        }
    }

    val initCalendarType =
        if (viewModel.firstLoadingTileCalendar()) CalendarType.TILE else CalendarType.LIST

    AppBar(
        year = viewModel.getYear(),
        initCalendarType = initCalendarType,
        onYearChange = onYearChange,
        onTimeReset = onTimeReset,
        navController = navController
    )
}


@Composable
fun AppBar(
    year: MutableState<Int>,
    initCalendarType: CalendarType,
    onYearChange: (year: Int) -> Unit,
    onTimeReset: () -> Unit,
    navController: NavHostController,
    forceVisibility: Boolean = false
) {

    var currentItemSelected by rememberSaveable {
        mutableStateOf(Item.CALENDAR)
    }

    var currentCalendarType by rememberSaveable {
        mutableStateOf(initCalendarType)
    }

    val visibility = navController.run {
        val route = currentDestination?.route ?: ""
        route != "${Navigation.HOLIDAY_PAGE.route}/{id}" &&
                route != "${Navigation.USERS_HOLIDAY_EDITOR.route}/{id}" &&
                route != Navigation.INIT_PAGE.route
    }

    val e = navController.currentBackStackEntryFlow.collectAsState(null)

    if (forceVisibility || (e.value != null && visibility))
        Surface(
            modifier = Modifier
                .padding(bottom = 1.dp)
                /*                .background(brush =
                    verticalGradient(
                        0.0f to Color.Red,
                        0.5f to Color.Green,
                        1.0f to Color.Yellow,
                    ))*/
                .drawWithContent {
                    clipRect(
                        left = 0f,
                        top = 0f,
                        right = size.width,
                        bottom = size.height + 5
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
            color = TopBarColor,
            elevation = 3.dp
        ) {
            DropDownYearMenu(height = APP_BAR_HEIGHT, currentYear = year.value) {
                onYearChange(it)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                val icon = getCalendarIcon(currentCalendarType)
                MenuItem(
                    iconId = icon,
                    item = Item.CALENDAR,
                    selectedItem = currentItemSelected
                ) {
                    val newRoute = when (navController.currentDestination?.route ?: "") {
                        Navigation.TILE_CALENDAR.route -> Navigation.LIST_CALENDAR
                        Navigation.LIST_CALENDAR.route -> Navigation.TILE_CALENDAR
                        else -> {
                            if (currentCalendarType == CalendarType.TILE)
                                Navigation.TILE_CALENDAR
                            else Navigation.LIST_CALENDAR
                        }
                    }
                    navController.navigate(newRoute.route)

                    currentCalendarType =
                        if (newRoute.route == Navigation.TILE_CALENDAR.route) {
                            CalendarType.TILE
                        } else {
                            CalendarType.LIST
                        }
                    currentItemSelected = Item.CALENDAR

                }
                MenuItem(
                    iconId = R.drawable.icon_reset_date,
                    item = Item.RESET,
                    selectedItem = currentItemSelected,
                    onClick = onTimeReset
                )
                MenuItem(
                    iconId = R.drawable.icon_settings,
                    item = Item.SETTINGS,
                    selectedItem = currentItemSelected
                ) {
                    currentItemSelected = Item.SETTINGS
                    navController.navigate(Navigation.SETTINGS.route)
                }
                MenuItem(
                    iconId = R.drawable.icon_info,
                    item = Item.INFO,
                    selectedItem = currentItemSelected
                ) {
                    currentItemSelected = Item.INFO
                    navController.navigate(Navigation.APP_INFO.route)
                }
            }
        }
}

fun getCalendarIcon(currentCalendarType: CalendarType) =
    if (currentCalendarType == CalendarType.TILE) {
        R.drawable.icon_list
    } else R.drawable.icon_tile


@Composable
fun MenuItem(iconId: Int, item: Item, selectedItem: Item, onClick: (() -> Unit)? = null) {
    val background = if (item == selectedItem) SelectedItemColor else Color.Transparent
    val onClickRemembered by rememberUpdatedState { onClick?.invoke() ?: Unit }
    IconButton(
        modifier = Modifier
            .height(APP_BAR_HEIGHT)
            .background(background),
        onClick = onClickRemembered
    ) {
        Icon(
            modifier = Modifier.clickable(onClick = onClickRemembered),
            painter = painterResource(iconId),
            contentDescription = item.name
        )
    }
}