package com.artmaster.android.orthodoxcalendar.ui.review.mvp

import android.content.Context
import android.content.res.AssetFileDescriptor
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.PLACEHOLDER_FOR_IMAGE
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.RESOURCE_FOR_IMAGE
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.impl.AppPreferences
import com.artmaster.android.orthodoxcalendar.impl.mvp.AbstractAppPresenter
import com.artmaster.android.orthodoxcalendar.ui.review.impl.HolidayReviewContract
import java.util.*
import java.util.Collections.replaceAll


class HolidayReviewPresenter(private val context: Context,
                             private val preferences: AppPreferences)
    : AbstractAppPresenter<HolidayReviewContract.View>(),
        HolidayReviewContract.Presenter {


    private var holidayEntity: HolidayEntity? = null

    override fun init(holiday: HolidayEntity) {
        holidayEntity = holiday
    }

    override fun viewIsReady() {
        if (holidayEntity == null) throw NoSuchElementException()
        val date = getDate(holidayEntity!!.day, holidayEntity!!.month)
        val desc = getDescription(holidayEntity!!.description)

        getView().showHolidayName(holidayEntity!!.title)
        getView().showImageHoliday(getImageId(holidayEntity!!.imageLink), getImageId(""))
        getView().showNewStyleDate(date.first)
        getView().showOldStyleDate(date.second)
        getView().showDescription(desc.first, desc.second)
    }

    private fun getDate(day: Int, month: Int): Pair<String, String> {
        if (day.or(month) == 0) return Pair("", "")

        val newDate = "$day ${OrtUtils.getMonthName(context, month).toLowerCase()}"
        val oldDate = getOldStyleDate(day, month)
        return newDate to oldDate
    }

    private fun getOldStyleDate(day: Int, month: Int): String {
        val calendar = GregorianCalendar()
        calendar.set(Time().year, month - 1, day)
        calendar.gregorianChange = Date(java.lang.Long.MAX_VALUE)
        return "${calendar.get(Calendar.DAY_OF_MONTH)} " +
                OrtUtils.getMonthName(context,
                        (calendar.get(Calendar.MONTH)) + 1).toLowerCase()
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