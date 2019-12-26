package com.artmaster.android.orthodoxcalendar.notifications

import android.app.Service
import android.content.Intent
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class NotificationsService : Service() {

    private val prefs = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()
    private val allowSound = prefs.get(SOUND_OF_NOTIFICATION).toBoolean()
    private val allowVibration = prefs.get(VIBRATION_OF_NOTIFICATION).toBoolean()
    private val allowAverageHolidays = prefs.get(AVERAGE_HOLIDAYS_NOTIFY_ALLOW).toBoolean()
    private val allowTodayNotification = prefs.get(IS_ENABLE_NOTIFICATION_TODAY).toBoolean()
    private val allowTimeNotification = prefs.get(IS_ENABLE_NOTIFICATION_TIME).toBoolean()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(allowTodayNotification or allowTimeNotification) startThread()

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
        time.calendar.set(Calendar.DAY_OF_MONTH, 26)



        val days = repository.getMonthDays(time.monthWith0, time.year)

        if(allowTimeNotification){
            val timeNotification = prefs.get(TIME_OF_NOTIFICATION)

        }

        if(allowTodayNotification){
            val holidays = days[time.dayOfMonth - 1].holidays
            notificationsToday(holidays)
        }
        //val calcTime = calculateTime(timeNotification)
        //checkDataBySetting(days[time.dayOfMonth - 1])
    }

    private fun notificationsToday(holidays : ArrayList<HolidayEntity>){
        for (holiday in holidays){
            buildNotification(holiday.title, getDescription(holiday), holiday.id.toInt())
        }
    }

    private fun getDescription(holiday: HolidayEntity): String {
        return "сегодня праздник ${holiday.title}"
    }

    private fun checkDataBySetting(day: Day){
        for(holiday in day.holidays){
            buildNotification(holiday.title, "описание", holiday.id.toInt())
        }
    }

    private fun calculateTime(timeSetting: Int): Calendar {
        val time = Time()
        return time.calculateDate(time.year, time.month, time.dayOfMonth, Calendar.DAY_OF_YEAR, timeSetting)
    }

    private fun buildNotification(name: String, msg: String, id : Int){
        Notification(applicationContext, id)
                .setHolidayName(name)
                .setSound(allowSound)
                .setVibration(allowVibration)
                .setMsgText(msg)
                .build()
    }
}