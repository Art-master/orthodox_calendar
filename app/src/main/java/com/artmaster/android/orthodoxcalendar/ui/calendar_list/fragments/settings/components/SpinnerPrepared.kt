package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.settings.components

import android.support.v7.widget.AppCompatSpinner
import android.view.View
import android.widget.AdapterView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Settings
import com.artmaster.android.orthodoxcalendar.common.SpinnerAdapter
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences

class SpinnerPrepared(private val spinner: AppCompatSpinner,
                      preferences: AppPreferences,
                      setting: Settings.Name,
                      private val defValues: Array<String> = emptyArray())
    : ElementUiPrepared(spinner as View, preferences, setting) {

    init {
        prepareUiElement(spinner, preferences)
    }

    override fun prepareUiElement(objectUi: View, preferences: AppPreferences) {
        val adapter = SpinnerAdapter(spinner.context!!, R.layout.spinner_year_item, defValues)
        adapter.setDropDownViewResource(R.layout.spinner_year_dropdown)
        spinner.adapter = adapter
        setOnItemSpinnerSelected()
        spinner.setSelection(getDay() - 1)
    }

    private fun setOnItemSpinnerSelected(){
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                saveSetting()
            }
            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun getDay() = preferences.get(setting).toInt()

    override fun putInitState(objectUi: View, preferences: AppPreferences) {
        spinner.setSelection(getDay() - 1)
    }

    override fun saveSetting() {
        val data = spinner.selectedItemPosition + 1
        preferences.set(setting, data.toString())
    }
}