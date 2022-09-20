package com.artmaster.android.orthodoxcalendar.ui.settings

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SettingsViewModel : ViewModel() {
    private val preferences = App.appComponent.getPreferences()


    private val settings = HashMap<String, MutableState<String>>(Settings.Name.values().size)

    init {
        initSettings()
    }

    private fun initSettings() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val map = HashMap<String, String>(Settings.Name.values().size)
                Settings.Name.values().forEach { setting ->
                    val value = preferences.get(setting)
                    map[setting.value] = value
                }

                withContext(Dispatchers.Main) {
                    map.forEach {
                        settings[it.key] = mutableStateOf(it.value)
                    }
                }
            }
        }
    }

    fun getSettings(setting: Settings.Name) = settings[setting.value]

}