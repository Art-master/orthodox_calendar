package com.artmaster.android.orthodoxcalendar.notifications

import android.annotation.TargetApi
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentResolver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.graphics.Color
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.PROJECT_DIR
import com.artmaster.android.orthodoxcalendar.common.Constants.ExtraData
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.ui.InitAppActivity

class Notification(private val context: Context, private val id: Long) {
    companion object {
        const val CHANNEL_ID = "$PROJECT_DIR.notifications.channel.message"
        const val CHANNEL_NAME = "orthodox_calendar"
    }

    private val prefs = App.appComponent.getPreferences()

    private var msgText = ""
    private var notificationName = ""
    private var soundUri: Uri? = null
    private val isStandardSound = prefs.get(Settings.Name.STANDARD_SOUND).toBoolean()
    private var soundEnable: Boolean = true
    private var vibrationEnable: Boolean = true
    private var vibrationSchema = longArrayOf(100, 300, 100, 300)

    fun setMsgText(text: String): Notification {
        msgText = text
        return this
    }

    fun setName(name: String): Notification {
        notificationName = name
        return this
    }

    fun setSound(isEnable: Boolean = true): Notification {
        soundEnable = isEnable
        soundUri = if (isEnable) buildSoundUri() else null
        return this
    }

    fun setVibration(isEnable: Boolean = true): Notification {
        vibrationEnable = isEnable
        return this
    }

    fun build() {
        val notificationManager =
            context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val notification = getBuilder(notificationManager)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(notificationName)
            .setContentText(msgText)
            .setAutoCancel(true)
            .setLights(Color.BLUE, 3000, 3000)
            .setContentIntent(createIntent())
            .setStyle(NotificationCompat.BigTextStyle().bigText(msgText))

        if (soundEnable) notification.setSound(soundUri)
        else notification.setSilent(true)

        notificationManager.notify(id.toInt(), notification.build())
    }

    private fun createIntent(): PendingIntent {
        val flags = if (Build.VERSION.SDK_INT >= 23) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }

        val notificationIntent = getIntent(context)
        return PendingIntent.getActivity(context, id.toInt(), notificationIntent, flags)
    }

    private fun getIntent(context: Context): Intent {
        val intent = Intent(context, InitAppActivity::class.java)
        intent.action = Constants.Action.OPEN_HOLIDAY_PAGE.value
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        intent.putExtra(ExtraData.HOLIDAY_ID.value, id)
        return intent
    }

    private fun getBuilder(notificationManager: NotificationManager): NotificationCompat.Builder {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(createChannel())
            NotificationCompat.Builder(context, CHANNEL_ID)
        } else {
            @Suppress("DEPRECATION")
            NotificationCompat.Builder(context)
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(): NotificationChannel {
        return NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            .apply {
                description = "Orthodox calendar"
                enableLights(true)
                setSound(soundUri, buildAudioAttributes())
                lightColor = Color.WHITE
                enableVibration(false)
            }
    }

    private fun buildAudioAttributes(): AudioAttributes {
        return AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .setUsage(AudioAttributes.USAGE_NOTIFICATION)
            .build()
    }

    private fun buildSoundUri(): Uri? {
        return if (isStandardSound) {
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        } else {
            Uri.parse("${ContentResolver.SCHEME_ANDROID_RESOURCE}://${context.packageName}/${R.raw.bell}")
        }
    }
}