package com.artmaster.android.orthodoxcalendar.notifications

import android.annotation.SuppressLint
import android.app.Service
import android.content.Intent
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import com.artmaster.android.orthodoxcalendar.data.repository.DataProvider
import com.artmaster.android.orthodoxcalendar.domain.Day
import com.artmaster.android.orthodoxcalendar.domain.Fasting.Type.*
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Holiday.Type
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.domain.Time.Month.DECEMBER
import kotlinx.coroutines.*
import java.util.*

class NotificationsService : Service() {

    private val prefs = App.appComponent.getPreferences()
    private val dataProvider = DataProvider()

    private val scope = CoroutineScope(Dispatchers.Main + Job())

    private var allowSound = false
    private var allowVibration = false

    private var allowAverageHolidays = false
    private var allowNameDays = false
    private var allowBirthdays = false
    private var allowMemoryDays = false
    private var allowFastingNotification = false

    private var allowTodayNotification = false
    private var allowTimeNotification = false

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            updatePermissions()
            if (allowTodayNotification or allowTimeNotification or allowNameDays or
                allowBirthdays or allowMemoryDays
            ) {

                if (timeCoincidence()) {
                    checkNotifications()
                }
            }
        }
        AlarmBuilder.build(applicationContext)

        return super.onStartCommand(intent, flags, startId)
    }

    private fun updatePermissions() {
        allowSound = prefs.getBoolean(SOUND_OF_NOTIFICATION)
        allowVibration = prefs.getBoolean(VIBRATION_OF_NOTIFICATION)

        allowAverageHolidays = prefs.getBoolean(AVERAGE_HOLIDAYS_NOTIFY_ALLOW)
        allowNameDays = prefs.getBoolean(NAME_DAYS_NOTIFY_ALLOW)
        allowBirthdays = prefs.getBoolean(BIRTHDAYS_NOTIFY_ALLOW)
        allowMemoryDays = prefs.getBoolean(MEMORY_DAYS_NOTIFY_ALLOW)
        allowFastingNotification = prefs.getBoolean(FASTING_NOTIFY_ALLOW)

        allowTodayNotification = prefs.getBoolean(IS_ENABLE_NOTIFICATION_TODAY)
        allowTimeNotification = prefs.getBoolean(IS_ENABLE_NOTIFICATION_TIME)
    }

    private fun timeCoincidence(): Boolean {
        val time = Time()
        return time.hour == getHoursInSettings()
    }

    private fun getHoursInSettings() = prefs.get(HOURS_OF_NOTIFICATION).toInt()

    override fun onBind(intent: Intent?): Nothing? = null

    private fun checkNotifications() {
        scope.launch {
            withContext(Dispatchers.IO) {
                val currentTime = Time()
                val days = dataProvider.getMonthDays(currentTime.monthWith0, currentTime.year)

                if (allowTimeNotification) {
                    notificationsInCertainTime(currentTime, days)
                }

                if (allowTodayNotification) {
                    val holidays = days[currentTime.dayOfMonth - 1].holidays
                    prepareNotificationsHolidays(holidays, currentTime)
                }

                if (allowFastingNotification) {
                    fastingNotifications(currentTime, days)
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
            buildNotification(description, holiday.id, holiday.title)
        }
    }

    private fun isAverageHoliday(holiday: Holiday): Boolean {
        return holiday.typeId == Type.AVERAGE_POLYLEIC.id || holiday.typeId == Type.AVERAGE_PEPPY.id
    }

    private fun getDescription(holiday: Holiday, time: Time, numDays: Int): String {
        val type = when (holiday.typeId) {
            Type.USERS_NAME_DAY.id -> getString(R.string.name_title)
            Type.USERS_BIRTHDAY.id -> getString(R.string.birthday_title)
            Type.USERS_MEMORY_DAY.id or Type.COMMON_MEMORY_DAY.id -> getString(R.string.memory_day_title)
            else -> getString(R.string.holiday_title)
        }

        return when {
            holiday.day == time.dayOfMonth && holiday.month == time.month ->
                getString(R.string.today) + " " + type

            numDays == 1 -> getString(R.string.tomorrow) + " " + type
            numDays > 1 -> getString(R.string.after_days, numDays) + " " + type
            else -> type.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
            }
        }
    }

    private fun buildNotification(description: String, id: Long, title: String) {
        Notification(applicationContext, id)
            .setSound(allowSound)
            .setVibration(allowVibration)
            .setName(description)
            .setMsgText(title)
            .build()
    }

    private fun notificationsInCertainTime(time: Time, days: List<Day>) {
        val usersNumDaysNotification = getTimeNotification()
        val numNotifyDay = time.dayOfMonth + usersNumDaysNotification - 1

        if (time.dayOfMonth + usersNumDaysNotification > time.daysInMonth) {
            val daysTwoMonths = days as ArrayList
            daysTwoMonths.addAll(getDaysOfNextMonth())

            prepareNotificationsHolidays(daysTwoMonths[numNotifyDay].holidays, time)
            checkRestDaysNotify(usersNumDaysNotification, time, daysTwoMonths, numNotifyDay)
        } else {
            prepareNotificationsHolidays(days[numNotifyDay].holidays, time)
            checkRestDaysNotify(usersNumDaysNotification, time, days, numNotifyDay)
        }
    }

    private fun checkRestDaysNotify(
        timeNotification: Int,
        time: Time,
        days: List<Day>,
        numNotifyDay: Int
    ) {
        if (timeNotification > 1) {
            val restDays = days.subList(time.dayOfMonth - 1, numNotifyDay)
            notifyAboutRestOfDaysTo(restDays, time)
        }
    }

    private fun notifyAboutRestOfDaysTo(days: List<Day>, time: Time) {
        for (i in days.size - 1 downTo 1) {
            for (holiday in days[i].holidays) {
                if (!allowAverageHolidays && isAverageHoliday(holiday)) continue
                if (!allowNameDays && holiday.typeId == Type.USERS_NAME_DAY.id) continue
                if (!allowBirthdays && holiday.typeId == Type.USERS_BIRTHDAY.id) continue
                if (!allowMemoryDays && holiday.typeId == Type.USERS_MEMORY_DAY.id) continue

                val description = getDescription(holiday, time, i)
                buildNotification(description, holiday.id, holiday.title)
            }
        }
    }

    private fun getTimeNotification() = prefs.getInt(TIME_OF_NOTIFICATION)
    private fun getFastingTimeNotification() = prefs.getInt(TIME_OF_FASTING_NOTIFICATION_IN_DAYS)

    private fun getDaysOfNextMonth(): List<Day> {
        val time = Time()
        if (time.month == DECEMBER.num) {
            time.calendar.set(Calendar.MONTH, Time.Month.JANUARY.num)
            time.calendar.set(Calendar.YEAR, time.year + 1)
        } else {
            time.calendar.set(Calendar.MONTH, time.month + 1)
        }
        return dataProvider.getMonthDays(time.month, time.year)
    }

    private fun fastingNotifications(time: Time, days: List<Day>) {
        val usersNumDaysNotification = getFastingTimeNotification()
        val numNotifyDay = time.dayOfMonth + usersNumDaysNotification - 1

        if (time.dayOfMonth + usersNumDaysNotification > time.daysInMonth) {
            val daysTwoMonths = days as ArrayList
            daysTwoMonths.addAll(getDaysOfNextMonth())
        }

        prepareFastingNotifications(days[numNotifyDay - 1], days[numNotifyDay], time)
        checkRestDaysNotify(usersNumDaysNotification, time, days, numNotifyDay)
    }

    @SuppressLint("DiscouragedApi")
    private fun prepareFastingNotifications(prevDay: Day, current: Day, time: Time) {
        if (current.fasting.type == FASTING_DAY) return

        val fastingStart = if (prevDay.fasting.type == NONE && current.fasting.type != NONE) {
            true
        } else if (prevDay.fasting.type != NONE && current.fasting.type == NONE) {
            false
        } else null

        fastingStart?.let {
            val resId =
                resources.getIdentifier(current.fasting.type.resourceName, "string", packageName)
            val type = getString(resId)
            val description = getFastingDescription(it, getTimeNotification())
            buildNotification(description, 0, type)
        }
    }

    private fun getFastingDescription(fastingStart: Boolean, numDays: Int): String {
        val suffix = getString(if (fastingStart) R.string.start else R.string.end)

        return when {
            numDays == 1 -> getString(R.string.tomorrow) + " " + suffix
            numDays > 1 -> getString(R.string.after_days, numDays) + " " + suffix
            else -> ""
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()

    }
}