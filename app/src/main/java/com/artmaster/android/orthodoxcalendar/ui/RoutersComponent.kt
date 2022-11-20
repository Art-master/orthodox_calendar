package com.artmaster.android.orthodoxcalendar.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.app_info_page.AppInfoLayout
import com.artmaster.android.orthodoxcalendar.ui.filters.CalendarToolsDrawer
import com.artmaster.android.orthodoxcalendar.ui.filters.MultiFabItem
import com.artmaster.android.orthodoxcalendar.ui.filters.Tabs
import com.artmaster.android.orthodoxcalendar.ui.holiday_page.components.HolidayInfoPager
import com.artmaster.android.orthodoxcalendar.ui.init_page.components.AppStartTextAnimation
import com.artmaster.android.orthodoxcalendar.ui.init_page.model.LoadDataViewModel
import com.artmaster.android.orthodoxcalendar.ui.list_calendar_page.HolidayPagerListLayout
import com.artmaster.android.orthodoxcalendar.ui.settings_page.SettingsLayoutWrapper
import com.artmaster.android.orthodoxcalendar.ui.settings_page.SettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.HolidayTileLayout
import com.artmaster.android.orthodoxcalendar.ui.user_holiday_page.UserHolidayLayout

@Composable
fun AppNavigationComponent(
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
        startDestination = Navigation.INIT_PAGE.route
    ) {
        composable(Navigation.INIT_PAGE.route) {
            AppStartTextAnimation(initViewModel.animationTime.toInt()) {
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
            CalendarToolsDrawer(
                viewModel = calendarViewModel,
                onToolClick = onToolItemClick
            ) {
                HolidayTileLayout(
                    calendarViewModel,
                    onDayClick,
                    onHolidayClick
                )
            }
        }
        composable(Navigation.LIST_CALENDAR.route) {
            CalendarToolsDrawer(
                viewModel = calendarViewModel,
                onToolClick = onToolItemClick
            ) {
                HolidayPagerListLayout(
                    calendarViewModel,
                    onDayClick,
                    onHolidayClick
                )

            }
        }
        composable("${Navigation.HOLIDAY_PAGE.route}/{id}") { backStackEntry ->
            val onEditClick = remember {
                { holiday: Holiday ->
                    navController.navigate(route = "${Navigation.USERS_HOLIDAY_EDITOR.route}/${holiday.id}")
                }
            }
            val onDeleteClick = remember {
                { holiday: Holiday ->
                    calendarViewModel.deleteHoliday(holiday.id)
                    navigateToCalendar(navController)
                }
            }

            val id = backStackEntry.arguments!!.getString("id")!!
            HolidayInfoPager(
                viewModel = calendarViewModel,
                holidayId = id.toLong(),
                onEditClick = onEditClick,
                onDeleteClick = onDeleteClick
            )
        }
        composable(route = "${Navigation.USERS_HOLIDAY_EDITOR.route}/{id}") { backStackEntry ->
            val id = backStackEntry.arguments!!.getString("id")!!
            val holiday = if (id != "0") {
                calendarViewModel.getHolidayById(id.toLong())
            } else null

            UserHolidayLayout(holiday) { data ->
                if (holiday == null) {
                    calendarViewModel.insertHoliday(data) {
                        calendarViewModel.clearCaches()
                        calendarViewModel.loadAllHolidaysOfCurrentYear()
                        navigateToHolidayPage(navController, it.id)
                    }
                } else {
                    calendarViewModel.updateHoliday(data) {
                        calendarViewModel.clearCaches()
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

private fun navigateToCalendar(navController: NavHostController) {
    val currentRoute = navController.currentDestination?.route ?: ""
    val targetRoute = if (currentRoute == Navigation.TILE_CALENDAR.route) {
        Navigation.LIST_CALENDAR.route
    } else Navigation.TILE_CALENDAR.route

    navController.navigate(targetRoute)
}