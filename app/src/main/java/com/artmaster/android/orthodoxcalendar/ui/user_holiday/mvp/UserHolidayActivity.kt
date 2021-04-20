package com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp

import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants.Keys.HOLIDAY
import com.artmaster.android.orthodoxcalendar.databinding.ActivityUserHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.components.SpinnerDecorator
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl.ContractUserHolidayView
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.presenter.InjectPresenter

class UserHolidayActivity : MvpAppCompatActivity(), ContractUserHolidayView, MvpView {

    @InjectPresenter(tag = "UserHolidayPresenter")
    lateinit var presenter: UserHolidayPresenter

    private var holiday = Holiday()

    private var _binding: ActivityUserHolidayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUserHolidayBinding.inflate(layoutInflater)
        setContentView(binding.root)
        prepareSpinners()
        holiday = savedInstanceState?.get(HOLIDAY.value) as Holiday


        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)
            presenter.viewIsReady()
        }
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(parent, name, context, attrs)
    }

    private fun prepareSpinners() {
        SpinnerDecorator(binding.holidayTypeSpinner, getEntityTypes())
        binding.holidayTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (selectedItemView != null) holiday.typeId = selectedItemView.id
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        binding.saveButton.setOnClickListener {
            holiday.title = binding.holidayName.text.toString()
            holiday.description = binding.holidayDescription.text.toString()
            holiday.isCreatedByUser = true
            presenter.dataCanBeSave(holiday)
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

    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        outState.putParcelable(HOLIDAY.value, holiday)
        super.onSaveInstanceState(outState, outPersistentState)
    }

    override fun closeView() {
        onBackPressed()
        setResult(HOLIDAY.hashCode())
    }
}
