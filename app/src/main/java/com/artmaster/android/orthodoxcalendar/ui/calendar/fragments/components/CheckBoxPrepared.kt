package com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.components

import android.view.View
import android.widget.CheckBox
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import org.jetbrains.anko.sdk27.coroutines.onClick

class CheckBoxPrepared(obj: View,
                       preferences: AppPreferences,
                       setting: Settings.Name,
                       private val defValue: Boolean = true)
    : ElementUiPrepared(obj, preferences, setting) {

    private val checkBox = obj as CheckBox
    val callback: (isChecked: Boolean) -> Unit = {}

    override fun prepareUiElement(objectUi: View, preferences: AppPreferences) {
        val value = preferences.get(setting)
        checkBox.isChecked = value.toBoolean()
        checkBox.onClick {
            saveSetting()
            callback(checkBox.isChecked)
        }
    }

    override fun putInitState(objectUi: View, preferences: AppPreferences) {
        checkBox.isChecked = defValue
        saveSetting()
    }

    override fun saveSetting() {
        val data = checkBox.isChecked
        preferences.set(setting, data.toString())
    }

    private fun onClick(){
        saveSetting()
    }
}
