package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.AppSettingView
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings.components.CheckBoxPrepared
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings.components.SpinnerPrepared
import kotlinx.android.synthetic.main.fragment_settings.view.*

/**
 * Show settings of the Holiday app
 */
class FragmentSettingsApp : Fragment(), AppSettingView {

    private val prefs = App.appComponent.getPreferences()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViews(view)
    }

    private fun prepareViews(v : View){
        CheckBoxPrepared(v.settingsNotifications, prefs, IS_ENABLE_NOTIFICATION_TODAY).prepare()
        CheckBoxPrepared(v.settingsNotifyAverageHolidays, prefs, AVERAGE_HOLIDAYS_NOTIFY_ALLOW).prepare()
        CheckBoxPrepared(v.settingsNotifySound, prefs, SOUND_OF_NOTIFICATION).prepare()
        CheckBoxPrepared(v.settingsNotifyVibration, prefs, VIBRATION_OF_NOTIFICATION).prepare()
        CheckBoxPrepared(v.settingsFirstLoadTileView, prefs, FIRST_LOADING_TILE_CALENDAR).prepare()

        CheckBoxPrepared(v.settingsNotifyTime, prefs, IS_ENABLE_NOTIFICATION_TIME).prepare().apply {
                    callback = {v.time_spinner.isEnabled = it.not()}
                }
        SpinnerPrepared(v.time_spinner, prefs, TIME_OF_NOTIFICATION, getDaysNumbers()).prepare()

        CheckBoxPrepared(v.settingsNotifyInTime, prefs, IS_ENABLE_NOTIFICATION_IN_TIME).prepare().apply {
                    callback = {v.time_in_spinner.isEnabled = it.not()}
                }
        SpinnerPrepared(v.time_in_spinner, prefs, HOURS_OF_NOTIFICATION, getHoursNumbers()).prepare()
    }

    private fun getHoursNumbers() =  getDaysNumbers().take(24).toTypedArray()
    private fun getDaysNumbers() =  resources.getStringArray(R.array.spinner_days_numbers)
}
