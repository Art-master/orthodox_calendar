package com.artmaster.android.orthodoxcalendar.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

/**
 * Executed, when the device is loading.
 */
class AppWakeUpReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            AlarmBuilder.build(context)
        }
    }
}