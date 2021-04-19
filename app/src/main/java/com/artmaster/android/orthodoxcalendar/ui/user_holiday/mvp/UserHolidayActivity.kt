package com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.databinding.ActivityUserHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.components.SpinnerDecorator
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl.ContractUserHolidayView

class UserHolidayActivity : AppCompatActivity(), ContractUserHolidayView {

    //@InjectPresenter(tag = "UserHolidayPresenter")
    //lateinit var presenter: UserHolidayPresenter

    private var holiday = Holiday()

    private var _binding: ActivityUserHolidayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUserHolidayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareSpinners()
        //holiday = savedInstanceState?.get(Constants.Keys.HOLIDAY.value) as Holiday

        // if (presenter.isViewAttached()) {
        //    presenter.attachView(this)
        //     presenter.viewIsReady()
        // }
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(parent, name, context, attrs)
    }

    private fun prepareSpinners() {
        SpinnerDecorator(binding.holidayTypeSpinner, getEntityTypes())
        binding.holidayTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {

            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun getEntityTypes() = resources.getStringArray(R.array.user_holidays_names)
    private fun getMonths() = resources.getStringArray(R.array.months_names_gen)
    private fun getDays(): Array<out String> {
        val time = Time()
        return resources.getStringArray(R.array.months_names_gen)
    }


    override fun showData(holiday: Holiday) {

    }
}
