package com.artmaster.android.orthodoxcalendar.notifications

import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        context.startService(Intent(context, NotificationsService::class.java))
    }
}