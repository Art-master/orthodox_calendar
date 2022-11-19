package com.artmaster.android.orthodoxcalendar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.app_info_page.AppInfoLayout
import com.artmaster.android.orthodoxcalendar.ui.common.AppBar
import com.artmaster.android.orthodoxcalendar.ui.filters.CalendarToolsDrawer
import com.artmaster.android.orthodoxcalendar.ui.filters.MultiFabItem
import com.artmaster.android.orthodoxcalendar.ui.filters.Tabs
import com.artmaster.android.orthodoxcalendar.ui.holiday_page.components.HolidayInfoPager
import com.artmaster.android.orthodoxcalendar.ui.init_page.components.AppStartTextAnimation
import com.artmaster.android.orthodoxcalendar.ui.init_page.model.LoadDataViewModel
import com.artmaster.android.orthodoxcalendar.ui.list_calendar_page.HolidayPagerListLayout
import com.artmaster.android.orthodoxcalendar.ui.settings_page.SettingsLayoutWrapper
import com.artmaster.android.orthodoxcalendar.ui.settings_page.SettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.NoRippleTheme
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar_page.HolidayTileLayout
import com.artmaster.android.orthodoxcalendar.ui.user_holiday_page.UserHolidayLayout

class InitAppActivity : ComponentActivity() {

    private val initViewModel: LoadDataViewModel by viewModels()
    private val calendarViewModel: CalendarViewModel by viewModels()
    private val settingsViewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel.fillDatabaseIfNeed {
            initNotifications()
        }

        setContent {
            val navController = rememberNavController()

            val onDayClick = remember {
                { day: Day -> calendarViewModel.setDayOfMonth(day = day.dayOfMonth) }
            }

            val onHolidayClick = remember {
                { holiday: Holiday ->
                    navController.navigate(Route.HOLIDAY_PAGE.name)
                }
            }

            val onToolItemClick = remember {
                { item: MultiFabItem ->
                    when (item.identifier) {
                        Tabs.NEW_EVENT.name -> {
                            navController.navigate(route = "${Route.USERS_HOLIDAY_EDITOR.name}/0")
                        }
                        Tabs.FILTERS.name -> {} //No actions
                        else -> throw IllegalStateException("Wrong item name")
                    }
                }
            }

            MaterialTheme {
                CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {

                    NavHost(
                        navController = navController,
                        startDestination = Route.INIT_PAGE.name
                    ) {
                        composable(Route.INIT_PAGE.name) {
                            AppStartTextAnimation(initViewModel.animationTime.toInt()) {
                                navController.navigate(Route.TILE_CALENDAR.name)
                            }
                        }
                        composable(Route.TILE_CALENDAR.name) {
                            Column {
                                AppBar(calendarViewModel, navController)
                                CalendarToolsDrawer(
                                    viewModel = calendarViewModel,
                                    onToolClick = onToolItemClick
                                ) {
                                    HolidayTileLayout(calendarViewModel, onDayClick, onHolidayClick)
                                }
                            }
                        }
                        composable(Route.LIST_CALENDAR.name) {
                            Column {
                                AppBar(calendarViewModel, navController)
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
                        }
                        composable("${Route.HOLIDAY_PAGE.name}/{id}") { backStackEntry ->
                            val onEditClick = remember {
                                { holiday: Holiday ->
                                    navController.navigate(route = "${Route.USERS_HOLIDAY_EDITOR.name}/${holiday.id}")
                                }
                            }
                            val onDeleteClick = remember {
                                { holiday: Holiday ->
                                    calendarViewModel.deleteHoliday(holiday.id)
                                    navigateToCalendar(navController)
                                }
                            }

                            val id = backStackEntry.arguments!!.getString("id")!!
                            Column {
                                AppBar(calendarViewModel, navController)
                                HolidayInfoPager(
                                    viewModel = calendarViewModel,
                                    holidayId = id.toLong(),
                                    onEditClick = onEditClick,
                                    onDeleteClick = onDeleteClick
                                )
                            }
                        }
                        composable(route = "${Route.USERS_HOLIDAY_EDITOR.name}/{id}") { backStackEntry ->
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
                        composable(Route.SETTINGS.name) {
                            Column {
                                AppBar(calendarViewModel, navController)
                                SettingsLayoutWrapper(settingsViewModel)
                            }
                        }
                        composable(Route.APP_INFO.name) {
                            Column {
                                AppBar(calendarViewModel, navController)
                                AppInfoLayout()
                            }
                        }

                    }
                }
            }
        }
    }

    private fun navigateToHolidayPage(navController: NavHostController, id: Long) {
        navController.navigate("${Route.HOLIDAY_PAGE.name}/${id}") {
            popUpTo("${Route.USERS_HOLIDAY_EDITOR.name}/{id}") {
                inclusive = true
            }
        }
    }

    private fun navigateToCalendar(navController: NavHostController) {
        val currentRoute = navController.currentDestination?.route ?: ""
        val targetRoute = if (currentRoute == Route.TILE_CALENDAR.name) {
            Route.LIST_CALENDAR.name
        } else Route.TILE_CALENDAR.name

        navController.navigate(targetRoute)
    }

    private fun initNotifications() = AlarmBuilder.build(applicationContext)
}
