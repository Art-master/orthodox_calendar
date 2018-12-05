package com.artmaster.android.orthodoxcalendar.ui.calendar.fragments

import android.os.Bundle
import android.support.v7.preference.PreferenceFragmentCompat
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppSettingView
import javax.inject.Inject

/**
 * Showed settings of the Holiday app
 */
class FragmentSettingsApp : PreferenceFragmentCompat(), AppSettingView {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        addPreferencesFromResource(R.xml.app_settings)
    }

    @Inject
    lateinit var settings: AppPreferences


}
