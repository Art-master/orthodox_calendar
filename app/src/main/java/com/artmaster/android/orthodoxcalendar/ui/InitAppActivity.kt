package com.artmaster.android.orthodoxcalendar.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.SnackbarData
import androidx.compose.material.SnackbarHost
import androidx.compose.material.SnackbarHostState
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.util.Consumer
import androidx.navigation.compose.rememberNavController
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Action
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.common.AppBarWrapper
import com.artmaster.android.orthodoxcalendar.ui.common.StyledSnackBar
import com.artmaster.android.orthodoxcalendar.ui.init_page.model.LoadDataViewModel
import com.artmaster.android.orthodoxcalendar.ui.theme.NoRippleTheme
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.viewmodel.SettingsViewModel

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
            val startRoute by remember { mutableStateOf(Navigation.INIT_PAGE.route) }
            val navController = rememberNavController()
            val snackState = remember { SnackbarHostState() }


            DisposableEffect(Unit) {
                val listener = Consumer<Intent> {
                    intent = it
                    if (it.action == Action.OPEN_HOLIDAY_PAGE.value) {
                        val id = it.getLongExtra(Constants.ExtraData.HOLIDAY_ID.value, 0)
                        navController.navigate("${Navigation.HOLIDAY_PAGE.route}/${id}")
                    }
                }

                val id = intent?.getLongExtra(Constants.ExtraData.HOLIDAY_ID.value, 0) ?: 0
                if (id > 0) navController.navigate("${Navigation.HOLIDAY_PAGE.route}/${id}")

                addOnNewIntentListener(listener)
                onDispose { removeOnNewIntentListener(listener) }
            }

            MaterialTheme {
                CompositionLocalProvider(LocalRippleTheme provides NoRippleTheme) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Column {
                            AppBarWrapper(
                                viewModel = calendarViewModel,
                                navController = navController,
                                snackState = snackState
                            )
                            AppNavigationComponent(
                                startRoute = startRoute,
                                calendarViewModel = calendarViewModel,
                                initViewModel = initViewModel,
                                settingsViewModel = settingsViewModel,
                                navController = navController
                            )
                        }

                        SnackbarHost(
                            modifier = Modifier
                                .align(Alignment.BottomStart)
                                .padding(bottom = 90.dp, start = 15.dp, end = 15.dp),
                            hostState = snackState
                        ) { data: SnackbarData ->
                            StyledSnackBar(message = data.message)
                        }
                    }
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    private fun initNotifications() = AlarmBuilder.build(applicationContext)
}
