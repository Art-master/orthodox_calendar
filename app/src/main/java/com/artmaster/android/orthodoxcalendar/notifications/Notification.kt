package com.artmaster.android.orthodoxcalendar.notifications

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.PROJECT_DIR
import android.media.AudioAttributes
import android.media.RingtoneManager

class Notification(private val context: Context, private val id: Int) {
    companion object {
        const val CHANNEL_ID = "$PROJECT_DIR.notifications.channel.message"
        const val CHANNEL_NAME = "$PROJECT_DIR.notifications.channel.users.msg"
    }

    private var msgText = ""
    private var holidayName = ""
    private var imageUrl = ""
    private var soundUri = buildSoundUri()
    private var soundEnable: Boolean = true
    private var vibrationEnable: Boolean = true
    private var vibration = longArrayOf(100, 300, 100, 300)

    private fun buildSoundUri(): Uri {
        //var uri = Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.raw.sino}")
        return RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
    }

    fun setMsgText(text: String): Notification {
        msgText = text
        return this
    }

    fun setHolidayName(name: String): Notification {
        holidayName = name
        return this
    }

    fun setSound(isEnable: Boolean = true, uri: Uri = Uri.EMPTY): Notification {
        soundUri = uri
        soundEnable = isEnable
        return this
    }

    fun setVibration(isEnable: Boolean = true): Notification {
        vibrationEnable = isEnable
        return this
    }

    fun build() {
        val notification = getBuilder()
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(holidayName)
                .setContentText(msgText)
                .setLights(Color.YELLOW, 3000, 3000)

        if(soundEnable) notification.setSound(soundUri)
        if(vibrationEnable) notification.setVibrate(vibration)

        val notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(id, notification.build())
    }

    private fun getBuilder(): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
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
                    setSound(soundUri, buildAudioAttributes())
                    lightColor = Color.YELLOW
                    enableVibration(true)
                }
    }

    private fun buildAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
    }
}