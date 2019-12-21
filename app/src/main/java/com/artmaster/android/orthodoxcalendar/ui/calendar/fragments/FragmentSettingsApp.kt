package com.artmaster.android.orthodoxcalendar.ui.calendar.fragments

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.components.CheckBoxPrepared
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppSettingView
import javax.inject.Inject
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.components.ElementUiPrepared
import kotlinx.android.synthetic.main.fragment_settings.*


/**
 * Showed settings of the Holiday app
 */
class FragmentSettingsApp : Fragment(), AppSettingView {

    var preparedElements: ArrayList<ElementUiPrepared> = ArrayList()

    val prefs = App.appComponent.getPreferences()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_settings, container, false)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViews()
    }

    private fun prepareViews(){
        val notify = CheckBoxPrepared(settingsNotifications, prefs, IS_ENABLE_NOTIFICATION_TODAY)
        val notifyTime = CheckBoxPrepared(settingsNotifications, prefs, IS_ENABLE_NOTIFICATION_TIME)
    }
    fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        //addPreferencesFromResource(R.xml.app_settings)
    }

    @Inject
    lateinit var settings: AppPreferences


}
