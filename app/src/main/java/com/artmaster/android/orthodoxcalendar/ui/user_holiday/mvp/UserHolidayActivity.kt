package com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants.Keys
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
        holiday = if (savedInstanceState == null) {
            intent?.extras?.getParcelable(HOLIDAY.value) ?: Holiday()
        } else {
            savedInstanceState.getParcelable(HOLIDAY.value) ?: Holiday()
        }
        binding.holiday = holiday
        prepareSpinners()
        prepareTimeViews()


        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)
            presenter.viewIsReady()
        }
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(parent, name, context, attrs)
    }

    private fun prepareSpinners() {
        val types = getHolidayTypes()
        SpinnerDecorator(binding.holidayTypeSpinner, types)
        binding.holidayTypeSpinner.setSelection(holiday.typeId - Holiday.Type.USERS_NAME_DAY.ordinal)
        binding.holidayTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (selectedItemView != null) holiday.typeId = selectedItemView.id
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        SpinnerDecorator(binding.holidayMonthSpinner, getMonthNames())
        binding.holidayMonthSpinner.setSelection(Time().monthWith0)
        binding.holidayTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, selectedItemView: View?, position: Int, id: Long) {
                if (selectedItemView != null) {
                    holiday.month = selectedItemView.id + 1
                    holiday.monthWith0 = selectedItemView.id
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        binding.saveButton.setOnClickListener {
            updateHolidayData()
            val needUpdate = intent?.extras?.getBoolean(Keys.NEED_UPDATE.value) ?: false
            presenter.dataCanBeSave(holiday, needUpdate)
            setResult(HOLIDAY.hashCode(), buildIntentForResult())
        }
    }

    private fun updateHolidayData() {
        holiday.apply {
            title = binding.holidayName.text.toString()
            description = binding.holidayDescription.text.toString()
            isCreatedByUser = true
            day = binding.dayOfMonth.text.toString().toInt()
            monthWith0 = binding.holidayMonthSpinner.selectedItemPosition
            month = monthWith0 + 1
            typeId = Holiday.Type.USERS_NAME_DAY.ordinal + binding.holidayTypeSpinner.selectedItemPosition
        }
    }

    private fun prepareTimeViews() {
        binding.holidayYearCheckedView.setOnCheckedChangeListener { button, flag ->
            binding.year.visibility = if (flag) View.VISIBLE else View.INVISIBLE
            if (flag && holiday.year == 0) {
                holiday.year = Time().year
                binding.invalidateAll()
            }
        }
    }

    private fun buildIntentForResult(): Intent {
        val intent = Intent(applicationContext, UserHolidayActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        intent.putExtra(HOLIDAY.value, holiday)
        return intent
    }

    private fun getMonthNames() = resources.getStringArray(R.array.months_names_gen)
    private fun getHolidayTypes() = resources.getStringArray(R.array.user_holidays_names)
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
