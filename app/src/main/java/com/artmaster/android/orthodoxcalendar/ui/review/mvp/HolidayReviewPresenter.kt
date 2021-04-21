package com.artmaster.android.orthodoxcalendar.ui.review.mvp

import android.content.Context
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.PLACEHOLDER_FOR_IMAGE
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.RESOURCE_FOR_IMAGE
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import com.artmaster.android.orthodoxcalendar.impl.mvp.AbstractAppPresenter
import com.artmaster.android.orthodoxcalendar.ui.review.impl.HolidayReviewContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*


class HolidayReviewPresenter(private val context: Context,
                             private val preferences: AppPreferences)
    : AbstractAppPresenter<HolidayReviewContract.View>(),
        HolidayReviewContract.Presenter {
    val repository = App.appComponent.getRepository()

    private var holiday: Holiday? = null

    private var isDataNotReadyYet = true

    override fun init(id: Long) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                holiday = repository.getFullHolidayData(id)
                withContext(Dispatchers.Main) {
                    if (isDataNotReadyYet.not()) {
                        viewIsReady()
                    }
                }
            }
        }
    }

    override fun viewIsReady() {
        if (holiday == null) {
            isDataNotReadyYet = false
            return
        }
        holiday?.let {
            val date = getDate(it.day, it.monthWith0)
            val desc = getDescription(it.description)

            getView().showHolidayName(it.title)
            getView().showImageHoliday(getImageId(it.imageId), getImageId(""))
            getView().showNewStyleDate(date.first)
            getView().showOldStyleDate(date.second)
            getView().showDescription(desc.first, desc.second)
        }

    }

    private fun getDate(day: Int, month: Int): Pair<String, String> {
        if (day.or(month) == 0) return Pair("", "")

        val newDate = "$day ${OrtUtils.getMonthNameAcc(context, month).toLowerCase(Locale.ROOT)}"
        val oldDate = getOldStyleDate(day, month)
        return newDate to oldDate
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
}