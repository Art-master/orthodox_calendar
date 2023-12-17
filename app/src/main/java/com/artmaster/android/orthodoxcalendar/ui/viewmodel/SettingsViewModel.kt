package com.artmaster.android.orthodoxcalendar.ui.viewmodel

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Immutable
class SettingsViewModel : ViewModel(), ISettingsViewModel {
    private val preferences = App.appComponent.getPreferences()

    private val settings = HashMap<String, MutableState<String>>(Settings.Name.entries.size)
    override val isInit = mutableStateOf(false)

    init {
        initSettings()
    }

    private fun initSettings() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                val map = HashMap<String, String>(Settings.Name.entries.size)
                Settings.Name.entries.forEach { setting ->
                    val value = preferences.get(setting)
                    map[setting.value] = value
                }

                withContext(Dispatchers.Main) {
                    map.forEach {
                        settings[it.key] = mutableStateOf(it.value)
                    }
                    isInit.value = true
                }
            }
        }
    }

    override fun getSetting(setting: Settings.Name): MutableState<String> {
        return settings[setting.value]!!
    }

    override fun setSetting(setting: Settings.Name, value: String) {
        settings[setting.value]?.value = value
        preferences.set(setting, value)
    }

}