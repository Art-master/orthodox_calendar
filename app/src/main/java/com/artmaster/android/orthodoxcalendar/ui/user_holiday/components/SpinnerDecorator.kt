package com.artmaster.android.orthodoxcalendar.ui.user_holiday.components

import android.widget.Spinner
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.SpinnerAdapter

class SpinnerDecorator(private val spinner: Spinner, private val defValues: Array<String> = emptyArray()) {

    init {
        prepare()
    }

    private fun prepare() {
        val adapter = SpinnerAdapter(spinner.context!!, R.layout.spinner_type_holiday, defValues)
        adapter.setDropDownViewResource(R.layout.spinner_year_dropdown)
        spinner.adapter = adapter
    }
}