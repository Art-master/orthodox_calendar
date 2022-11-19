package com.artmaster.android.orthodoxcalendar.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.ui.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.Route
import com.artmaster.android.orthodoxcalendar.ui.theme.TopBarColor

@Preview
@Composable
fun AppBarPreview() {
    val navController = rememberNavController()
    AppBarInner(year = 2022, initCalendarType = CalendarType.TILE, navController = navController)
}

enum class Item {
    CALENDAR, RESET, SETTINGS, INFO
}

enum class CalendarType {
    TILE, LIST
}

@Composable
fun AppBar(viewModel: CalendarViewModel = CalendarViewModel(), navController: NavHostController) {

    val onYearChange = remember {
        { year: Int ->
            viewModel.setYear(year)
        }
    }

    val initCalendarType =
        if (viewModel.firstLoadingTileCalendar()) CalendarType.TILE else CalendarType.LIST

    AppBarInner(
        year = viewModel.getYear().value,
        initCalendarType = initCalendarType,
        onYearChange = onYearChange,
        navController = navController
    )
}


@Composable
fun AppBarInner(
    year: Int,
    initCalendarType: CalendarType,
    onYearChange: (year: Int) -> Unit = {},
    navController: NavHostController
) {
    var currentItemSelected by rememberSaveable {
        mutableStateOf(Item.CALENDAR)
    }

    var currentCalendarType by rememberSaveable {
        mutableStateOf(initCalendarType)
    }

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
        DropDownYearMenu(currentYear = year) {
            onYearChange(it)
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {

            val icon = getCalendarIcon(navController)
            MenuItem(iconId = icon, Item.CALENDAR) {
                val route = navController.currentDestination?.route ?: ""
                when (route) {
                    Route.TILE_CALENDAR.name -> navController.navigate(Route.LIST_CALENDAR.name)
                    Route.LIST_CALENDAR.name -> navController.navigate(Route.TILE_CALENDAR.name)
                    else -> {
                        val newRoute =
                            if (currentCalendarType == CalendarType.TILE)
                                Route.LIST_CALENDAR
                            else Route.TILE_CALENDAR

                        navController.navigate(newRoute.name)
                    }
                }
                currentCalendarType = if (route == Route.TILE_CALENDAR.name) {
                    CalendarType.TILE
                } else {
                    CalendarType.LIST
                }

            }
            MenuItem(iconId = R.drawable.icon_reset_date, Item.RESET) {

            }
            MenuItem(iconId = R.drawable.icon_settings, Item.SETTINGS) {
                navController.navigate(Route.SETTINGS.name)
                currentItemSelected = Item.SETTINGS
            }
            MenuItem(iconId = R.drawable.icon_info, Item.INFO) {
                navController.navigate(Route.APP_INFO.name)
            }
        }
    }
}

fun getCalendarIcon(navController: NavHostController) = navController.run {
    val currentRoute = currentDestination?.route ?: ""
    if (currentRoute == Route.TILE_CALENDAR.name) {
        R.drawable.icon_tile
    } else R.drawable.icon_list
}

@Composable
fun MenuItem(iconId: Int, item: Item, onClick: () -> Unit = {}) {
    IconButton(onClick = onClick) {
        Icon(
            painter = painterResource(iconId),
            contentDescription = item.name
        )
    }
}