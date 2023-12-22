package com.artmaster.android.orthodoxcalendar.api

import com.artmaster.android.orthodoxcalendar.common.Settings

interface AppPreferences {
    fun get(settName: Settings.Name): String
    fun getBoolean(settName: Settings.Name): Boolean
    fun getInt(settName: Settings.Name): Int
    fun set(settings: Settings.Name, value: String)
    fun has(settings: Settings.Name): Boolean
}