package com.artmaster.android.orthodoxcalendar.ui.user_holiday.components

import android.widget.Spinner
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.SpinnerAdapter

class SpinnerDecorator(private val spinner: Spinner, private val defValues: Array<String> = emptyArray()) {

    init {
        prepare()
    }

    private fun prepare(layoutSpinnerId: Int = R.layout.spinner_type_holiday,
                        layoutDropdownId: Int = R.layout.spinner_type_holiday_dropdown) {

        val adapter = SpinnerAdapter(spinner.context!!, layoutSpinnerId, defValues)
        adapter.setDropDownViewResource(layoutDropdownId)
        spinner.adapter = adapter
    }
}