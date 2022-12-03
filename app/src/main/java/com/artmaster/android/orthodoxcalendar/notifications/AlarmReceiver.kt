package com.artmaster.android.orthodoxcalendar.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.artmaster.android.orthodoxcalendar.common.Constants.Action

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Action.NOTIFICATION.value) {
            context.startService(Intent(context, NotificationsService::class.java))
        }
    }
}