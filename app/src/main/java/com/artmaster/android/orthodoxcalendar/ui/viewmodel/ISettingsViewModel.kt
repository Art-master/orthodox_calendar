package com.artmaster.android.orthodoxcalendar.ui.viewmodel

import androidx.compose.runtime.MutableState
import com.artmaster.android.orthodoxcalendar.common.Settings

interface ISettingsViewModel {
    val isInit: MutableState<Boolean>
    fun getSetting(setting: Settings.Name): MutableState<String>?
    fun setSetting(setting: Settings.Name, value: String)
}