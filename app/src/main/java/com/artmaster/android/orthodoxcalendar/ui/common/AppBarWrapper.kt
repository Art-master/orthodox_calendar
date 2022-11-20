package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.Navigation
import com.artmaster.android.orthodoxcalendar.ui.theme.SelectedItemColor
import com.artmaster.android.orthodoxcalendar.ui.theme.TopBarColor

@Preview
@Composable
fun AppBarPreview() {
    val navController = rememberNavController()
    val year = remember { mutableStateOf(2022) }
    AppBar(
        year = year,
        initCalendarType = CalendarType.TILE,
        navController = navController,
        forceVisibility = true
    )
}

enum class Item {
    CALENDAR, RESET, SETTINGS, INFO
}

enum class CalendarType {
    TILE, LIST
}

@Composable
fun AppBarWrapper(
    viewModel: CalendarViewModel = CalendarViewModel(),
    navController: NavHostController
) {

    val onYearChange = remember {
        { year: Int ->
            viewModel.setYear(year)
        }
    }

    val initCalendarType =
        if (viewModel.firstLoadingTileCalendar()) CalendarType.TILE else CalendarType.LIST

    AppBar(
        year = viewModel.getYear(),
        initCalendarType = initCalendarType,
        onYearChange = onYearChange,
        navController = navController
    )
}


@Composable
fun AppBar(
    year: MutableState<Int>,
    initCalendarType: CalendarType,
    onYearChange: (year: Int) -> Unit = {},
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
                .padding(bottom = 5.dp)
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
            DropDownYearMenu(currentYear = year.value) {
                onYearChange(it)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {

                val icon = getCalendarIcon(currentCalendarType)
                MenuItem(iconId = icon, item = Item.CALENDAR, selectedItem = currentItemSelected) {
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

                    currentCalendarType = if (newRoute.route == Navigation.TILE_CALENDAR.route) {
                        CalendarType.TILE
                    } else {
                        CalendarType.LIST
                    }
                    currentItemSelected = Item.CALENDAR

                }
                MenuItem(
                    iconId = R.drawable.icon_reset_date,
                    item = Item.RESET,
                    selectedItem = currentItemSelected
                ) {

                }
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
fun MenuItem(iconId: Int, item: Item, selectedItem: Item, onClick: () -> Unit = {}) {
    val background = if (item == selectedItem) SelectedItemColor else Color.Transparent
    IconButton(
        modifier = Modifier
            .height(49.dp)
            .background(background),
        onClick = onClick
    ) {
        Icon(
            modifier = Modifier.clickable { onClick() },
            painter = painterResource(iconId),
            contentDescription = item.name
        )
    }
}