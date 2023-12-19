package com.artmaster.android.orthodoxcalendar.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.artmaster.android.orthodoxcalendar.api.AppPreferences
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.SETTINGS_FILE_NAME
import com.artmaster.android.orthodoxcalendar.common.Settings
import javax.inject.Inject

/**
 * Execute save and receive the app preferences
 */
class CalendarPreferences @Inject constructor(context: Context) : AppPreferences {
    private var preferences: SharedPreferences = context.getSharedPreferences(SETTINGS_FILE_NAME, 0)

    override fun get(settName: Settings.Name): String {
        return preferences.getString(settName.value, settName.defValue) ?: settName.defValue
    }

    override fun getBoolean(settName: Settings.Name): Boolean {
        val settings = preferences.getString(settName.value, settName.defValue) ?: settName.defValue
        return settings.toBoolean()
    }

    override fun getInt(settName: Settings.Name): Int {
        val settings = preferences.getString(settName.value, settName.defValue) ?: settName.defValue
        return settings.toInt()
    }

    override fun set(settings: Settings.Name, value: String) {
        preferences.edit().putString(settings.value, value).apply()
    }

    override fun has(settings: Settings.Name): Boolean {
        return preferences.contains(settings.value)
    }
}