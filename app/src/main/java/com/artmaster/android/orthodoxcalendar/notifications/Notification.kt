package com.artmaster.android.orthodoxcalendar.notifications

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.PROJECT_DIR


class Notification(private val context: Context) {
    companion object {
        const val CHANNEL_ID = "$PROJECT_DIR.notifications.channel.message"
        const val CHANNEL_NAME = "$PROJECT_DIR.notifications.channel.users.msg"
        const val NOTIFICATION_ID = 213
    }

    private var msgText = ""
    private var holidayName = ""
    private var imageUrl = ""

    fun setMsgText(text: String): Notification {
        msgText = text
        return this
    }

    fun setHolidayName(name: String): Notification {
        holidayName = name
        return this
    }

    fun build() {
        val notification = getBuilder()
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setLargeIcon(Picasso.with(context).load(imageUrl).get())
                .setContentTitle(holidayName)
                .setContentText(msgText)
                .build()

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    private fun getBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            createChannel()
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            NotificationCompat.Builder(context)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(): NotificationChannel {
        return NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
                .apply {
                    description = "My channel description"
                    enableLights(true)
                    lightColor = Color.RED
                    enableVibration(false)
                }
    }
}