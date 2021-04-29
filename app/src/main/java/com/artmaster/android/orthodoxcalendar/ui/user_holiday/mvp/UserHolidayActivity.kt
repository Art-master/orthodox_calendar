package com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputFilter
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
import java.util.*


class UserHolidayActivity : MvpAppCompatActivity(), ContractUserHolidayView, MvpView {

    @InjectPresenter(tag = "UserHolidayPresenter")
    lateinit var presenter: UserHolidayPresenter

    private var holiday = Holiday()

    private val currentTime = Time()

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
                if (selectedItemView != null) {
                    holiday.typeId = getTypeId(position)
                    setYearFieldVisibility()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }

        SpinnerDecorator(binding.holidayMonthSpinner, getMonthNames())
        binding.holidayMonthSpinner.setSelection(currentTime.monthWith0)
        binding.holidayMonthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
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
        }
    }

    private fun setYearFieldVisibility() {
        if (holiday.typeId == Holiday.Type.USERS_NAME_DAY.id) {
            binding.year.visibility = View.INVISIBLE
            binding.holidayYearCheckedView.visibility = View.INVISIBLE
        } else {
            binding.year.visibility = View.VISIBLE
            binding.holidayYearCheckedView.visibility = View.VISIBLE
        }
    }

    private fun updateHolidayData() {
        holiday.apply {
            title = binding.holidayName.text.toString()
            if (title.isEmpty()) title = getString(R.string.new_name_holiday)
            description = binding.holidayDescription.text.toString()
            isCreatedByUser = true

            binding.dayOfMonth.apply {
                day = if (text.toString().isNotEmpty()) text.toString().toInt()
                else currentTime.dayOfMonth
            }

            monthWith0 = binding.holidayMonthSpinner.selectedItemPosition
            month = monthWith0 + 1
            typeId = getTypeId(binding.holidayTypeSpinner.selectedItemPosition)

            year = when {
                typeId == Holiday.Type.USERS_NAME_DAY.id -> 0
                binding.year.text.toString().isNotEmpty() -> binding.year.text.toString().toInt()
                else -> currentTime.year
            }
        }
    }

    private fun getTypeId(pos: Int) = Holiday.Type.USERS_NAME_DAY.ordinal + pos

    private fun getYearFilter() = InputFilter { source, start, end, dest, dstart, dend ->
        val year = currentTime.year
        for (i in start until end) {
            if (!Character.isDigit(source[i])) return@InputFilter ""
        }
        if (source.length > 2) return@InputFilter source
        val str = dest.toString() + source
        if (source.isEmpty() || str.isEmpty()) return@InputFilter source
        if (str.length > 4) return@InputFilter ""

        val newYear = str.toInt()
        if (newYear > year || newYear <= 0) return@InputFilter ""
        null
    }

    private fun prepareTimeViews() {
        initYearField()
        initDayOfMonthField()
    }

    private fun initYearField() {
        binding.holidayYearCheckedView.setOnCheckedChangeListener { button, flag ->
            binding.year.visibility = if (flag) View.VISIBLE else View.INVISIBLE
            if (flag && holiday.year == 0) {
                holiday.year = currentTime.year
                binding.invalidateAll()
            }
        }

        binding.year.apply {
            filters = arrayOf(getYearFilter())
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    if (text.toString().isEmpty()) setText(currentTime.year.toString())
                    else {
                        val time = currentTime
                        val num = text.toString().toInt()
                        if (num > time.year) setText(currentTime.year.toString())
                    }
                }
            }
        }

    }

    private fun getDayOfMonthFilter() = InputFilter { source, start, end, dest, dstart, dend ->
        for (i in start until end) {
            if (!Character.isDigit(source[i])) return@InputFilter ""
        }
        if (source.length > 1) return@InputFilter source
        val str = dest.toString() + source
        if (source.isEmpty() || str.isEmpty()) return@InputFilter source
        if (str.length > 2) return@InputFilter ""

        val newDay = str.toInt()
        if (newDay > 31 || newDay <= 0) return@InputFilter ""
        null
    }

    private fun initDayOfMonthField() {
        binding.dayOfMonth.apply {
            filters = arrayOf(getDayOfMonthFilter())
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus.not()) {
                    if (text.toString().isEmpty()) setText(currentTime.dayOfMonth.toString())
                    else {
                        val num = text.toString().toInt()
                        if (num > 31) setText(currentTime.dayOfMonth.toString())
                    }
                }
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

    override fun showData(holiday: Holiday) {

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        holiday = savedInstanceState.getParcelable(HOLIDAY.value) ?: Holiday()
        binding.holiday = holiday
        binding.invalidateAll()
        super.onRestoreInstanceState(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        updateHolidayData()
        outState.putParcelable(HOLIDAY.value, holiday)
        super.onSaveInstanceState(outState)
    }

    override fun closeView() {
        onBackPressed()
        setResult(HOLIDAY.hashCode())
    }

    override fun dataWasSaved(isUpdate: Boolean) {
        setResult(HOLIDAY.hashCode(), buildIntentForResult())
    }

    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }
}
