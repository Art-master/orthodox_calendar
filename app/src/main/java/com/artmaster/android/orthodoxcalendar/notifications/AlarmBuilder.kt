package com.artmaster.android.orthodoxcalendar.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*
import android.content.pm.PackageManager
import android.content.ComponentName
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Settings

object AlarmBuilder {

    private val prefs = App.appComponent.getPreferences()

    fun build(context: Context){
        if(isNotificationDisable()) return

        val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(context, 0, intent, 0)
        }

        // Set the alarm to start at approximately by setting time
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, getHoursBySettings())
        }

        alarmMgr.setInexactRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                alarmIntent
        )
    }

    private fun isNotificationDisable(): Boolean{
        val isEnableToday = prefs.get(Settings.Name.IS_ENABLE_NOTIFICATION_TODAY)
        val isEnableBefore = prefs.get(Settings.Name.IS_ENABLE_NOTIFICATION_BEFORE)
        return isEnableToday == Settings.FALSE || isEnableBefore == Settings.FALSE
    }

    private fun getHoursBySettings(): Int{
        val settings = prefs.get(Settings.Name.TIME_OF_NOTIFICATION)
        if (settings == Settings.EMPTY) return Constants.STANDARD_NOTIFICATION_HOURS
        return settings?.toInt() ?: Constants.STANDARD_NOTIFICATION_HOURS
    }

    /**
     * Enable boot receiver to persist alarms set for notifications across device reboots
     */
    fun enableBootReceiver(context: Context) {
        val receiver = ComponentName(context, AlarmReceiver::class.java)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP)
    }

    /**
     * Disable boot receiver when user cancels/opt-out from notifications
     */
    fun disableBootReceiver(context: Context) {
        val receiver = ComponentName(context, AlarmReceiver::class.java)
        val pm = context.packageManager

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP)
    }
}