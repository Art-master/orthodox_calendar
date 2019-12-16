package com.artmaster.android.orthodoxcalendar.notifications

import android.app.Service
import android.content.Intent
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Time
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit

class NotificationsService : Service() {

    private val prefs = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startThread()
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?) = null

    private fun startThread(){
        Single.fromCallable { execute() }
            .subscribeOn(Schedulers.io())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
               onSuccess = { },
               onError = { it.printStackTrace() })
    }

    private fun execute(){
        val time = Time()
        val timeNotification = 1

        val days = repository.getMonthDays(time.month, time.year)
        val calcTime = calculateTime(timeNotification)
        checkDataBySetting(days[calcTime.get(Calendar.DAY_OF_MONTH) - 1])
    }

    private fun checkDataBySetting(day: Day){
        for(holiday in day.holidays){
            buildNotification(holiday.title, "описание")
        }
    }

    private fun calculateTime(timeSetting: Int): Calendar {
        val time = Time()
        return time.calculateDate(time.year, time.month, time.dayOfMonth, Calendar.DAY_OF_YEAR, timeSetting)
    }

    private fun buildNotification(name: String, msg: String){
        Notification(applicationContext)
                .setHolidayName(name)
                .setMsgText(msg)
                .build()
    }
}