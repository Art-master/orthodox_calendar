package com.artmaster.android.orthodoxcalendar.ui.init

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp.MainCalendarActivity
import com.artmaster.android.orthodoxcalendar.ui.init.components.AppStartTextAnimation
import com.artmaster.android.orthodoxcalendar.ui.init.model.LoadDataViewModel

class InitAppActivity : ComponentActivity() {

    private val viewModel: LoadDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.fillDatabaseIfNeed {
            initNotifications()
        }

        setContent {
            AppStartTextAnimation(viewModel.animationTime.toInt()) {
                nextScreen()
            }
        }
    }

    private fun initNotifications() {
        AlarmBuilder.build(applicationContext)
    }

    private fun nextScreen() {
        startActivity(getArgs())
        finish()
    }

    private fun getArgs() = Intent(applicationContext, MainCalendarActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }
}
