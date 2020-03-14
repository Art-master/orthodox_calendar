package com.artmaster.android.orthodoxcalendar.notifications

import android.app.Service
import android.content.Intent
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.util.*

class NotificationsService : Service() {

    private val prefs = App.appComponent.getPreferences()
    private val repository = App.appComponent.getRepository()

    private val allowSound = prefs.get(SOUND_OF_NOTIFICATION).toBoolean()
    private val allowVibration = prefs.get(VIBRATION_OF_NOTIFICATION).toBoolean()
    private val allowAverageHolidays = prefs.get(AVERAGE_HOLIDAYS_NOTIFY_ALLOW).toBoolean()
    private val allowTodayNotification = prefs.get(IS_ENABLE_NOTIFICATION_TODAY).toBoolean()
    private val allowTimeNotification = prefs.get(IS_ENABLE_NOTIFICATION_TIME).toBoolean()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (allowTodayNotification or allowTimeNotification) startThread()
        }

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
        var time = getTime()

        val days = repository.getMonthDays(time.monthWith0, time.year)

        if(allowTimeNotification){
            notificationsByTime(time, days)
        }

        if(allowTodayNotification){
            time = getTime()
            val holidays = days[time.dayOfMonth - 1].holidays
            prepareNotificationsHolidays(holidays, time)
        }
    }

    private fun getTime(): Time{
        return Time()
    }

    private fun prepareNotificationsHolidays(holidays : ArrayList<HolidayEntity>, time: Time){
        for (holiday in holidays){
            if(!allowAverageHolidays && isAverageHoliday(holiday)) continue
            val description = getDescription(holiday, time, getTimeNotification())
            buildNotification(description, holiday)
        }
    }

    private fun isAverageHoliday(holiday: HolidayEntity): Boolean {
        return holiday.type.contains(HolidayEntity.Type.AVERAGE.value)
    }

    private fun getDescription(holiday: HolidayEntity, time: Time, numDays: Int): String {
        return when {
            holiday.day == time.dayOfMonth && holiday.month == time.month ->
                getString(R.string.notifications_today_name)
            numDays == 1 -> getString(R.string.notifications_tomorrow_name)
            numDays > 1 -> getString(R.string.notifications_after_days_name, numDays)
            else -> getString(R.string.notifications_holiday)
        }
    }

    private fun buildNotification(description: String, holiday: HolidayEntity){
        Notification(applicationContext, holiday)
                .setSound(allowSound)
                .setVibration(allowVibration)
                .setName(description)
                .setMsgText(holiday.title)
                .build()
    }

    private fun notificationsByTime(time: Time, days: List<Day>){
        val timeNotification = getTimeNotification()
        val numNotifyDay = time.dayOfMonth + timeNotification - 1

        if(time.dayOfMonth + timeNotification > time.daysInMonth){
            val daysTwoMonths = days as ArrayList
            daysTwoMonths.addAll(getDaysOfNextMonth())

            prepareNotificationsHolidays(daysTwoMonths[numNotifyDay].holidays, time)
            checkRestDaysNotify(timeNotification, time, daysTwoMonths, numNotifyDay)
        } else {
            prepareNotificationsHolidays(days[numNotifyDay].holidays, time)
            checkRestDaysNotify(timeNotification, time, days, numNotifyDay)
        }
    }

    private fun checkRestDaysNotify(timeNotification: Int, time: Time, days: List<Day>, numNotifyDay: Int){
        if(timeNotification > 1){
            val restDays = days.subList(time.dayOfMonth - 1, numNotifyDay)
            notifyRestDays(restDays, time)
        }
    }

    private fun notifyRestDays(days: List<Day>, time: Time){
        for (i in days.size - 1 downTo 1) {
            if(i == 0) continue
            for (holiday in days[i].holidays){
                if(!allowAverageHolidays && isAverageHoliday(holiday)) continue
                val description = getDescription(holiday, time, i)
                buildNotification(description, holiday)
            }
        }
    }

    private fun getTimeNotification() = prefs.get(TIME_OF_NOTIFICATION).toInt()

    private fun getDaysOfNextMonth(): List<Day>{
        val time = getTime()
        if(time.month == Time.Month.DECEMBER.num) {
            time.calendar.set(Calendar.MONTH, 0)
            time.calendar.set(Calendar.YEAR,  time.year + 1)
        } else {
            time.calendar.set(Calendar.MONTH, time.month + 1)
        }
        return repository.getMonthDays(time.month, time.year)
    }
}