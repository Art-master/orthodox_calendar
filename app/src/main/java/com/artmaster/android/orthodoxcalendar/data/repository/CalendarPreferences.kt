package com.artmaster.android.orthodoxcalendar.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.SETTINGS_FILE_NAME
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import javax.inject.Inject

/**
 * Execute save and receive the app preferences
 */
class CalendarPreferences @Inject constructor(context: Context) : AppPreferences {
    private var preferences: SharedPreferences = context.getSharedPreferences(SETTINGS_FILE_NAME, 0)

    override fun get(settName: Settings.Name): String {
        return preferences.getString(settName.name, settName.defValue) ?: settName.defValue
    }

    override fun set(settings: Settings.Name, value: String) {
        preferences.edit().putString(settings.name, value).apply()
    }
}