package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings.Name.*
import com.artmaster.android.orthodoxcalendar.databinding.FragmentSettingsBinding
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.AppSettingView
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings.components.CheckBoxDecorator
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings.components.SpinnerPrepared

/**
 * Show settings of the Holiday app
 */
class FragmentSettingsApp : Fragment(), AppSettingView {

    private val prefs = App.appComponent.getPreferences()

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareViews()
    }

    private fun prepareViews() {
        CheckBoxDecorator(binding.settingsNotifications, prefs, IS_ENABLE_NOTIFICATION_TODAY).prepare()
        CheckBoxDecorator(binding.settingsNotifyAverageHolidays, prefs, AVERAGE_HOLIDAYS_NOTIFY_ALLOW).prepare()
        CheckBoxDecorator(binding.settingsNotifyNameDays, prefs, NAME_DAYS_NOTIFY_ALLOW).prepare()
        CheckBoxDecorator(binding.settingsNotifyBirthdays, prefs, BIRTHDAYS_NOTIFY_ALLOW).prepare()
        CheckBoxDecorator(binding.settingsNotifyMemoryDays, prefs, MEMORY_DAYS_NOTIFY_ALLOW).prepare()

        CheckBoxDecorator(binding.settingsNotifySound, prefs, SOUND_OF_NOTIFICATION).prepare()
        CheckBoxDecorator(binding.settingsNotifyVibration, prefs, VIBRATION_OF_NOTIFICATION).prepare()
        CheckBoxDecorator(binding.settingsFirstLoadTileView, prefs, FIRST_LOADING_TILE_CALENDAR).prepare()

        CheckBoxDecorator(binding.settingsNotifyTime, prefs, IS_ENABLE_NOTIFICATION_TIME).prepare().apply {
            onClick = {
                binding.timeSpinner.isEnabled = it.not()
            }
        }
        SpinnerPrepared(binding.timeSpinner, prefs, TIME_OF_NOTIFICATION, getDaysNumbers()).prepare()

        CheckBoxDecorator(binding.settingsNotifyInTime, prefs, IS_ENABLE_NOTIFICATION_IN_TIME).prepare().apply {
            onClick = {
                binding.timeInSpinner.isEnabled = it.not()
            }
        }
        SpinnerPrepared(binding.timeInSpinner, prefs, HOURS_OF_NOTIFICATION, getHoursNumbers()).prepare().apply {
            onClick = {
                AlarmBuilder.build(requireContext())
            }
        }

        CheckBoxDecorator(binding.settingsSpeedUpAnimation, prefs, SPEED_UP_START_ANIMATION).prepare()

        CheckBoxDecorator(binding.settingsOffAnimation, prefs, OFF_START_ANIMATION).prepare().apply {
            onClick = {
                binding.settingsSpeedUpAnimation.isEnabled = it
                if (binding.settingsSpeedUpAnimation.isChecked) {
                    binding.settingsSpeedUpAnimation.isChecked = false
                }
            }
        }
    }

    private fun getHoursNumbers() = getDaysNumbers().take(24).toTypedArray()
    private fun getDaysNumbers() = resources.getStringArray(R.array.spinner_days_numbers)
}
