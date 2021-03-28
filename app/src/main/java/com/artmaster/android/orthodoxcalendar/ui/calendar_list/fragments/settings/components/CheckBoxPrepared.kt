package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings.components

import android.view.View
import android.widget.CheckBox
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences

class CheckBoxPrepared(private val checkBox: CheckBox,
                       preferences: AppPreferences,
                       setting: Settings.Name)
    : ElementUiPrepared(checkBox, preferences, setting) {

    init {
        prepareUiElement(checkBox, preferences)
    }

    override fun prepareUiElement(objectUi: View, preferences: AppPreferences) {
        val value = preferences.get(setting)
        checkBox.isChecked = value.toBoolean()
        checkBox.setOnClickListener {
            saveSetting()
            callback(checkBox.isChecked.not())
        }
    }

    override fun putInitState(objectUi: View, preferences: AppPreferences) {
        checkBox.isChecked = preferences.get(setting).toBoolean()
        saveSetting()
    }

    override fun saveSetting() {
        val data = checkBox.isChecked
        preferences.set(setting, data.toString())
    }
}
