package com.artmaster.android.orthodoxcalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.app_info_page.AppInfoLayout
import com.artmaster.android.orthodoxcalendar.ui.holiday_page.HolidayInfoPager
import com.artmaster.android.orthodoxcalendar.ui.init_page.components.AppStartTextAnimation
import com.artmaster.android.orthodoxcalendar.ui.init_page.model.LoadDataViewModel
import com.artmaster.android.orthodoxcalendar.ui.list_calendar_page.HolidayPagerListLayout
import com.artmaster.android.orthodoxcalendar.ui.settings_page.SettingsLayoutWrapper
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.HolidayTileLayout
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.components.Spinner
import com.artmaster.android.orthodoxcalendar.ui.tools.CalendarToolsDrawer
import com.artmaster.android.orthodoxcalendar.ui.tools.MultiFabItem
import com.artmaster.android.orthodoxcalendar.ui.tools.Tabs
import com.artmaster.android.orthodoxcalendar.ui.user_holiday_page.UserHolidayLayout
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.SettingsViewModel

@Composable
fun AppNavigationComponent(
    startRoute: String = Navigation.INIT_PAGE.route,
    calendarViewModel: CalendarViewModel,
    initViewModel: LoadDataViewModel,
    settingsViewModel: SettingsViewModel,
    navController: NavHostController
) {
    val onDayClick = remember {
        { day: Day -> calendarViewModel.setDayOfMonth(day = day.dayOfMonth) }
    }

    val onHolidayClick = remember {
        { holiday: Holiday ->
            navController.navigate("${Navigation.HOLIDAY_PAGE.route}/${holiday.id}")
        }
    }

    val onEditClick = remember {
        { holiday: Holiday ->
            navController.navigate(route = "${Navigation.USERS_HOLIDAY_EDITOR.route}/${holiday.id}")
        }
    }
    val onDeleteClick = remember {
        { holiday: Holiday ->
            calendarViewModel.deleteHoliday(holiday.id)
            navigateToCalendar(navController, settingsViewModel)
        }
    }

    val onToolItemClick = remember {
        { item: MultiFabItem ->
            when (item.identifier) {
                Tabs.NEW_EVENT.name -> {
                    navController.navigate(route = "${Navigation.USERS_HOLIDAY_EDITOR.route}/0")
                }

                Tabs.FILTERS.name -> {} //No actions
                else -> throw IllegalStateException("Wrong item name")
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = startRoute
    ) {
        composable(Navigation.INIT_PAGE.route) {
            AppStartTextAnimation(duration = initViewModel.animationTime.toInt(), resIndex = null) {

                val route = if (calendarViewModel.firstLoadingTileCalendar())
                    Navigation.TILE_CALENDAR
                else Navigation.LIST_CALENDAR

                navController.navigate(route.route) {
                    popUpTo(Navigation.INIT_PAGE.route) {
                        inclusive = true
                    }
                }
            }
        }
        composable(Navigation.TILE_CALENDAR.route) {
            if (initViewModel.isDatabasePrepared) {
                CalendarToolsDrawer(
                    viewModel = calendarViewModel,
                    settingsViewModel = settingsViewModel,
                    onToolClick = onToolItemClick
                ) {
                    HolidayTileLayout(
                        calendarViewModel,
                        settingsViewModel,
                        onDayClick,
                        onHolidayClick
                    )
                }
            } else Spinner()
        }
        composable(Navigation.LIST_CALENDAR.route) {
            if (initViewModel.isDatabasePrepared) {
                CalendarToolsDrawer(
                    viewModel = calendarViewModel,
                    settingsViewModel = settingsViewModel,
                    onToolClick = onToolItemClick
                ) {
                    HolidayPagerListLayout(
                        calendarViewModel,
                        settingsViewModel,
                        onDayClick,
                        onEditClick,
                        onDeleteClick,
                        onHolidayClick
                    )

                }
            } else Spinner()
        }
        composable("${Navigation.HOLIDAY_PAGE.route}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments!!.getString("id")!!
            HolidayInfoPager(
                viewModel = calendarViewModel,
                holidayId = id.toLong(),
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
        composable(route = "${Navigation.USERS_HOLIDAY_EDITOR.route}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments!!.getString("id")!!.toLong()
            val isNewHoliday = id == 0L
            UserHolidayLayout(
                holidayId = if (isNewHoliday) null else id,
                viewModel = calendarViewModel
            ) { data ->
                if (isNewHoliday) {
                    calendarViewModel.insertHoliday(data) {
                        calendarViewModel.loadAllHolidaysOfCurrentYear()
                        navigateToHolidayPage(navController, it.id)
                    }
                } else {
                    calendarViewModel.updateHoliday(data) {
                        calendarViewModel.loadAllHolidaysOfCurrentYear()
                        navigateToHolidayPage(navController, data.id)
                    }
                }
            }
        }
        composable(Navigation.SETTINGS.route) {
            SettingsLayoutWrapper(settingsViewModel)
        }
        composable(Navigation.APP_INFO.route) {
            AppInfoLayout()
        }
    }
}

private fun navigateToHolidayPage(navController: NavHostController, id: Long) {
    navController.navigate("${Navigation.HOLIDAY_PAGE.route}/${id}") {
        popUpTo("${Navigation.USERS_HOLIDAY_EDITOR.route}/{id}") {
            inclusive = true
        }
    }
}

fun navigateToCalendar(navController: NavHostController, model: SettingsViewModel) {
    if (model.getSetting(Settings.Name.FIRST_LOADING_TILE_CALENDAR).value.toBoolean()) {
        navController.navigate(Navigation.TILE_CALENDAR.route)
    } else navController.navigate(Navigation.LIST_CALENDAR.route)
}