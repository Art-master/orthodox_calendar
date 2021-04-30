package com.artmaster.android.orthodoxcalendar.notifications

import android.app.Service
import android.content.Intent
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Type
import com.artmaster.android.orthodoxcalendar.domain.Time
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class NotificationsService : Service() {

    private val prefs = App.appComponent.getPreferences()
    private val dataProvider = DataProvider()

    private val allowSound = prefs.get(SOUND_OF_NOTIFICATION).toBoolean()
    private val allowVibration = prefs.get(VIBRATION_OF_NOTIFICATION).toBoolean()

    private val allowAverageHolidays = prefs.get(AVERAGE_HOLIDAYS_NOTIFY_ALLOW).toBoolean()
    private val allowNameDays = prefs.get(NAME_DAYS_NOTIFY_ALLOW).toBoolean()
    private val allowBirthdays = prefs.get(BIRTHDAYS_NOTIFY_ALLOW).toBoolean()
    private val allowMemoryDays = prefs.get(MEMORY_DAYS_NOTIFY_ALLOW).toBoolean()

    private val allowTodayNotification = prefs.get(IS_ENABLE_NOTIFICATION_TODAY).toBoolean()
    private val allowTimeNotification = prefs.get(IS_ENABLE_NOTIFICATION_TIME).toBoolean()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            if (allowTodayNotification or allowTimeNotification or allowNameDays or
                    allowBirthdays or allowMemoryDays) {

                if (timeCoincidence()) {
                    checkNotifications()
                    AlarmBuilder.build(applicationContext)
                }
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun timeCoincidence() = Time().hour == getHoursInSettings()
    private fun getHoursInSettings() = prefs.get(HOURS_OF_NOTIFICATION).toInt()

    override fun onBind(intent: Intent?): Nothing? = null

    private fun checkNotifications() {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                val currentTime = Time()
                val days = dataProvider.getMonthDays(currentTime.monthWith0, currentTime.year)

                if (allowTimeNotification) {
                    notificationsByTime(currentTime, days)
                }

                if (allowTodayNotification) {
                    val holidays = days[currentTime.dayOfMonth - 1].holidays
                    prepareNotificationsHolidays(holidays, currentTime)
                }
            }
        }
    }

    private fun prepareNotificationsHolidays(holidays: List<Holiday>, time: Time) {
        for (holiday in holidays) {
            if (!allowAverageHolidays && isAverageHoliday(holiday)) continue
            if (!allowNameDays && holiday.typeId == Type.USERS_NAME_DAY.id) continue
            if (!allowBirthdays && holiday.typeId == Type.USERS_BIRTHDAY.id) continue
            if (!allowMemoryDays && holiday.typeId == Type.USERS_MEMORY_DAY.id) continue

            val description = getDescription(holiday, time, getTimeNotification())
            buildNotification(description, holiday)
        }
    }

    private fun isAverageHoliday(holiday: Holiday): Boolean {
        return holiday.typeId == Type.AVERAGE_POLYLEIC.id || holiday.typeId == Type.AVERAGE_PEPPY.id
    }

    private fun getDescription(holiday: Holiday, time: Time, numDays: Int): String {
        val type = when (holiday.typeId) {
            Type.USERS_NAME_DAY.id -> getString(R.string.holiday_title)
            Type.USERS_BIRTHDAY.id -> getString(R.string.birthday_title)
            Type.USERS_MEMORY_DAY.id or Type.COMMON_MEMORY_DAY.id -> getString(R.string.memory_day_title)
            else -> getString(R.string.holiday_title)
        }

        return when {
            holiday.day == time.dayOfMonth && holiday.month == time.month ->
                getString(R.string.notifications_today_name) + " " + type
            numDays == 1 -> getString(R.string.tomorrow) + " " + type
            numDays > 1 -> getString(R.string.after_days, numDays) + " " + type
            else -> type.capitalize(Locale.ROOT)
        }
    }

    private fun buildNotification(description: String, holiday: Holiday) {
        Notification(applicationContext, holiday)
                .setSound(allowSound)
                .setVibration(allowVibration)
                .setName(description)
                .setMsgText(holiday.title)
                .build()
    }

    private fun notificationsByTime(time: Time, days: List<Day>) {
        val userDaysNotification = getTimeNotification()
        val numNotifyDay = time.dayOfMonth + userDaysNotification - 1

        if (time.dayOfMonth + userDaysNotification > time.daysInMonth) {
            val daysTwoMonths = days as ArrayList
            daysTwoMonths.addAll(getDaysOfNextMonth())

            prepareNotificationsHolidays(daysTwoMonths[numNotifyDay].holidays, time)
            checkRestDaysNotify(userDaysNotification, time, daysTwoMonths, numNotifyDay)
        } else {
            prepareNotificationsHolidays(days[numNotifyDay].holidays, time)
            checkRestDaysNotify(userDaysNotification, time, days, numNotifyDay)
        }
    }

    private fun checkRestDaysNotify(timeNotification: Int, time: Time, days: List<Day>, numNotifyDay: Int) {
        if (timeNotification > 1) {
            val restDays = days.subList(time.dayOfMonth - 1, numNotifyDay)
            notifyAboutRestOfDaysTo(restDays, time)
        }
    }

    private fun notifyAboutRestOfDaysTo(days: List<Day>, time: Time) {
        for (i in days.size - 1 downTo 1) {
            if (i == 0) continue
            for (holiday in days[i].holidays) {
                if (!allowAverageHolidays && isAverageHoliday(holiday)) continue
                if (!allowNameDays && holiday.typeId == Type.USERS_NAME_DAY.id) continue
                if (!allowBirthdays && holiday.typeId == Type.USERS_BIRTHDAY.id) continue
                if (!allowMemoryDays && holiday.typeId == Type.USERS_MEMORY_DAY.id) continue

                val description = getDescription(holiday, time, i)
                buildNotification(description, holiday)
            }
        }
    }

    private fun getTimeNotification() = prefs.get(TIME_OF_NOTIFICATION).toInt()

    private fun getDaysOfNextMonth(): List<Day> {
        val time = Time()
        if (time.month == Time.Month.DECEMBER.num) {
            time.calendar.set(Calendar.MONTH, 0)
            time.calendar.set(Calendar.YEAR, time.year + 1)
        } else {
            time.calendar.set(Calendar.MONTH, time.month + 1)
        }
        return dataProvider.getMonthDays(time.month, time.year)
    }
}