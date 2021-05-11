package com.artmaster.android.orthodoxcalendar.ui.review.mvp

import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.PLACEHOLDER_FOR_IMAGE
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.RESOURCE_FOR_IMAGE
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.review.impl.HolidayReviewContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*

@InjectViewState
class HolidayReviewPresenter : MvpPresenter<HolidayReviewContract.View>(), HolidayReviewContract.Presenter {
    private val repository = App.appComponent.getRepository()
    private val context = App.appComponent.getContext()

    private var holiday: Holiday? = null

    private var currentTime = Time()

    override fun init(id: Long, year: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.IO) {
                holiday = repository.getFullHolidayData(id, year)
                withContext(Dispatchers.Main) {
                    initUI()
                }
            }
        }
    }

    override fun viewIsReady() {
        if (holiday != null) initUI()
    }

    private fun initUI() {
        holiday?.let {
            val date = getDate(it)
            val desc = getDescription(it.description)

            viewState.showHolidayName(getHolidayName(it))
            viewState.showImageHoliday(getImageId(it.imageId), getImageId(PLACEHOLDER_FOR_IMAGE))
            viewState.showNewStyleDate(date.first, it.isCreatedByUser)
            viewState.initButtons(it)

            if (it.isCreatedByUser.not()) viewState.showOldStyleDate(date.second)
            viewState.showDescription(desc.first, desc.second)
        }
    }

    private fun getHolidayName(holiday: Holiday): String {
        var startStr = when (holiday.typeId) {
            Holiday.Type.USERS_BIRTHDAY.id -> context.getString(R.string.birthday_title)
            Holiday.Type.USERS_NAME_DAY.id -> context.getString(R.string.name_title)
            Holiday.Type.USERS_MEMORY_DAY.id -> context.getString(R.string.memory_day_title)
            else -> ""
        }

        if (startStr.isNotEmpty()) {
            startStr = startStr.capitalize(Locale.ROOT)
            return "$startStr \n \n ${holiday.title}"
        }
        return holiday.title
    }

    private fun getDate(holiday: Holiday): Pair<String, String> {
        val day = holiday.day
        val month = holiday.monthWith0
        if (day.or(month) == 0) return Pair("", "")

        val newDate = getNewStyleDate(holiday, day, month)
        val oldDate = getOldStyleDate(day, month)
        return newDate to oldDate
    }

    private fun getNewStyleDate(holiday: Holiday, day: Int, month: Int): String {
        var str = "$day ${OrtUtils.getMonthNameAcc(context, month).toLowerCase(Locale.ROOT)}"
        val endStr = when (holiday.typeId) {
            Holiday.Type.USERS_BIRTHDAY.id -> {
                if (holiday.year < 1) return str
                val yearsNum = currentTime.year - holiday.year
                if (yearsNum <= 0) return str
                "(${context.getString(R.string.age_title)}: $yearsNum)"
            }
            else -> ""
        }
        if (holiday.year > 0) str += " ${holiday.year} ${context.getString(R.string.year_title_short)}"

        if (endStr.isNotEmpty()) {
            return "$str \n $endStr"
        }
        return str
    }

    private fun getOldStyleDate(day: Int, month: Int): String {
        val calendar = GregorianCalendar()
        calendar.set(Time().year, month, day)
        calendar.gregorianChange = Date(Long.MAX_VALUE)
        return "${calendar.get(Calendar.DAY_OF_MONTH)} " +
                OrtUtils.getMonthNameAcc(context, (calendar.get(Calendar.MONTH))).toLowerCase(Locale.ROOT)
    }

    private fun getImageId(image: String): Int {
        return if (getResourceDrawable(image) != 0)
            getResourceDrawable(image)
        else getImageId(PLACEHOLDER_FOR_IMAGE)
    }

    private fun getResourceDrawable(name: String): Int {
        return context.resources.getIdentifier(
                name,
                RESOURCE_FOR_IMAGE,
                context.packageName)
    }

    private fun getDescription(description: String): Pair<String, String> {
        if (description.isEmpty()) return Pair("", "")

        val textForDescription = description.replace("[\\s&&[^\t?\n]]+", " ")
        val result = textForDescription.substring(1)
        return textForDescription.first().toString() to result
    }

    override fun removeHoliday() {
        if (holiday == null) return
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                repository.deleteById(holiday!!.id)
            }
        }
    }
}