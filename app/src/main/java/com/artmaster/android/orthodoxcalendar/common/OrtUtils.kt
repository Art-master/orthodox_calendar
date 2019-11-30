package com.artmaster.android.orthodoxcalendar.common

import android.content.Context
import android.util.TypedValue
import android.content.Intent
import android.support.v4.app.Fragment
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.domain.Time

/**
 * Various functions for this application
 */
object OrtUtils {

    /** Calculate dp in the device  */
    fun setDp(context: Context, num: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (num * scale + 0.5f).toInt()
    }

    /** Convert dp in px  */
    fun convertDpToPixels(context: Context, dp: Float): Int {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.resources.displayMetrics).toInt()
        return px
    }

    /** Convert sp in px  */
    fun convertSpToPixels(context: Context, sp: Float): Int {
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.resources.displayMetrics).toInt()
        return px
    }

    /** Convert dp in sp  */
    fun convertDpToSp(context: Context, dp: Float): Int {
        val sp = (convertDpToPixels(context, dp) / convertSpToPixels(context, dp).toFloat()).toInt()
        return sp
    }

    /** get day of week by a holiday obj  */
    @JvmStatic
    fun getDayOtWeek(context: Context, holiday : HolidayEntity): String {
        val time = Time()
        time.calendar.set(holiday.year, holiday.month -1, holiday.day)
        val names = context.resources.getStringArray(R.array.daysNamesAbb)
        return names[time.dayOfWeek - 1]
    }

    /**
     * Get name of the month from android string resources.
     * Annotation @JvmStatic for data binding.
     * @return month name or "NONE" if resources not found
     */
    @JvmStatic
    fun getMonthName(context: Context, monthNum: Int): String {
        if (monthNum > 0 || monthNum < 13) {
            val stringName = "month$monthNum"

            val identifier = context
                    .resources
                    .getIdentifier(stringName, "string", context.packageName)

            return context.resources.getString(identifier)
        }
        return "NONE"
    }

    /** Execute exit from a program */
    fun exitProgram(context: Context) {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(a)
    }

    /** Check if an [element]  is Fragment*/
    fun checkFragment(element: Any): Fragment {
        if (element is Fragment) {
            return element
        } else throw TypeCastException()
    }
}
