package com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.components

import android.view.View
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences

abstract class ElementUiPrepared
internal constructor(private val obj: View,
                     internal val preferences: AppPreferences,
                     internal val setting: Settings.Name) {

    fun prepare() {
        if (containsSetting()) {
            prepareUiElement(obj, preferences)
        } else {
            putInitState(obj, preferences)
        }
    }

    private fun containsSetting() = preferences.get(setting) != Settings.EMPTY

    /**
     * if settings found, then read from file and show prepared element
     * @param objectUi - object interface which extends View
     * @param preferences - object preferences
     */
    internal abstract fun prepareUiElement(objectUi: View, preferences: AppPreferences)

    /**
     * if settings not found, execute initialisation this View and save settings in file
     * @param objectUi - object interface which extends View
     * @param preferences - object preferences
     */
    internal abstract fun putInitState(objectUi: View, preferences: AppPreferences)

    /**
     * Save settings in file
     */
    abstract fun saveSetting()
}
