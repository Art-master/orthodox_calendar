package com.artmaster.android.orthodoxcalendar.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.components.HolidayPagerListLayout
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.init.components.AppBar
import com.artmaster.android.orthodoxcalendar.ui.init.components.AppStartTextAnimation
import com.artmaster.android.orthodoxcalendar.ui.init.model.LoadDataViewModel
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
                                HolidayTileLayout(viewModel)
                            }
                        }
                        composable(Route.LIST_CALENDAR.name) {
                            Column {
                                AppBar(viewModel, navController)
                                HolidayPagerListLayout(viewModel)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun initNotifications() = AlarmBuilder.build(applicationContext)
}
