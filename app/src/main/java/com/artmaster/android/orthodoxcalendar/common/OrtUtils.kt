package com.artmaster.android.orthodoxcalendar.common

import android.content.Context
import android.content.Intent
import android.util.TypedValue

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
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dp,
            context.resources.displayMetrics
        ).toInt()
    }

    /** Convert sp in px  */
    fun convertSpToPixels(context: Context, sp: Float): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP, sp,
            context.resources.displayMetrics
        ).toInt()
    }

    /** Convert dp in sp  */
    fun convertDpToSp(context: Context, dp: Float): Int {
        return (convertDpToPixels(context, dp) / convertSpToPixels(context, dp).toFloat()).toInt()
    }

    /** Execute exit from a program */
    fun exitProgram(context: Context) {
        val a = Intent(Intent.ACTION_MAIN)
        a.addCategory(Intent.CATEGORY_HOME)
        a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(a)
    }
}
