package com.artmaster.android.orthodoxcalendar.ui.init

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp.MainCalendarActivity
import com.artmaster.android.orthodoxcalendar.ui.init.components.TextAnimation
import dagger.android.AndroidInjection

class InitAppActivity : ComponentActivity() {

    private val viewModel: LoadDataViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_app)

        setContent {
            TextAnimation(viewModel.animationTime)
        }
    }

    fun initNotifications() {
        AlarmBuilder.build(applicationContext)
    }

    fun nextScreen() {
        startActivity(getArgs())
        finish()
    }

    private fun getArgs() = Intent(applicationContext, MainCalendarActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }

    override fun onBackPressed() = OrtUtils.exitProgram(applicationContext)
}
