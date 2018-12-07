package com.artmaster.android.orthodoxcalendar.impl

import com.artmaster.android.orthodoxcalendar.common.Settings

interface AppPreferences {
    fun get(settName: Settings.Name): String?
    fun set(settings: Settings.Name, value: String)
}