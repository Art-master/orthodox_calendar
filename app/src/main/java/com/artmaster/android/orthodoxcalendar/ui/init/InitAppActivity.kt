package com.artmaster.android.orthodoxcalendar.ui.init

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import com.artmaster.android.orthodoxcalendar.ui.init.components.AppBar
import com.artmaster.android.orthodoxcalendar.ui.init.components.AppStartTextAnimation
import com.artmaster.android.orthodoxcalendar.ui.init.model.LoadDataViewModel
import com.artmaster.android.orthodoxcalendar.ui.tile_month.components.HolidayTileLayout

class InitAppActivity : ComponentActivity() {

    private val initViewModel: LoadDataViewModel by viewModels()

    private val viewModel: CalendarViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel.fillDatabaseIfNeed {
            initNotifications()
        }

        setContent {
            AppStartTextAnimation(initViewModel.animationTime.toInt()) {
                nextScreen()
            }
        }
    }

    private fun initNotifications() = AlarmBuilder.build(applicationContext)

    private fun nextScreen() {
        setContent {
            Column {
                AppBar()
                HolidayTileLayout(viewModel)
            }
        }
    }
}
