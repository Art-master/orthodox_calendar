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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.init.components.AppBar
import com.artmaster.android.orthodoxcalendar.ui.init.components.AppStartTextAnimation
import com.artmaster.android.orthodoxcalendar.ui.init.model.LoadDataViewModel
import com.artmaster.android.orthodoxcalendar.ui.list_calendar.components.HolidayPagerListLayout
import com.artmaster.android.orthodoxcalendar.ui.review.components.HolidayPage
import com.artmaster.android.orthodoxcalendar.ui.settings.SettingsLayoutWrapper
import com.artmaster.android.orthodoxcalendar.ui.settings.SettingsViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.NoRippleTheme
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.HolidayTileLayout

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
            val currentHoliday = calendarViewModel.getCurrentHoliday()

            val onDayClick = remember {
                { day: Day -> calendarViewModel.setDayOfMonth(day = day.dayOfMonth) }
            }

            val onHolidayClick = remember {
                { holiday: Holiday ->
                    calendarViewModel.loadHolidayAdditionalInfo(holiday)
                    navController.navigate(Route.HOLIDAY_PAGE.name)
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
                                HolidayTileLayout(calendarViewModel, onDayClick, onHolidayClick)
                            }
                        }
                        composable(Route.LIST_CALENDAR.name) {
                            Column {
                                AppBar(calendarViewModel, navController)
                                HolidayPagerListLayout(
                                    calendarViewModel,
                                    onDayClick,
                                    onHolidayClick
                                )
                            }
                        }
                        composable(Route.HOLIDAY_PAGE.name) {
                            Column {
                                AppBar(calendarViewModel, navController)
                                HolidayPage(calendarViewModel, currentHoliday)
                            }
                        }
                        composable(Route.SETTINGS.name) {
                            Column {
                                AppBar(calendarViewModel, navController)
                                SettingsLayoutWrapper(settingsViewModel)
                            }
                        }

                    }
                }
            }
        }
    }

    private fun initNotifications() = AlarmBuilder.build(applicationContext)
}
