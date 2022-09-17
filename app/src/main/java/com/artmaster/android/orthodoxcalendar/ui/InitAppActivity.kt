package com.artmaster.android.orthodoxcalendar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.artmaster.android.orthodoxcalendar.ui.theme.NoRippleTheme
import com.artmaster.android.orthodoxcalendar.ui.tile_calendar.components.HolidayTileLayout

class InitAppActivity : ComponentActivity() {

    private val initViewModel: LoadDataViewModel by viewModels()

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel.fillDatabaseIfNeed {
            initNotifications()
        }

        setContent {
            val navController = rememberNavController()
            val currentHoliday = rememberSaveable { mutableStateOf<Holiday?>(null) }

            val onDayClick = remember {
                { day: Day -> viewModel.setDayOfMonth(day = day.dayOfMonth) }
            }

            val onHolidayClick = remember {
                { holiday: Holiday -> currentHoliday.value = holiday }
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
                                AppBar(viewModel, navController)
                                HolidayTileLayout(viewModel, onDayClick, onHolidayClick)
                            }
                        }
                        composable(Route.LIST_CALENDAR.name) {
                            Column {
                                AppBar(viewModel, navController)
                                HolidayPagerListLayout(viewModel, onDayClick, onHolidayClick)
                            }
                        }
                        composable(Route.HOLIDAY_PAGE.name) {
                            Column {
                                AppBar(viewModel, navController)
                                HolidayPage(viewModel, currentHoliday)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initNotifications() = AlarmBuilder.build(applicationContext)
}
