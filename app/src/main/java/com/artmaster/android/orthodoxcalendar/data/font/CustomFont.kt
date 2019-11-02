package com.artmaster.android.orthodoxcalendar.data.font

import android.content.Context
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.TextView
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.FONT_ASSETS_DIRECTORY

import java.lang.ref.SoftReference
import java.util.Hashtable

/**
 * Set custom font on a TextView or Button
 */
object CustomFont {

    private const val TAG = "CustomFont"

    private val fontCache = Hashtable<String, SoftReference<Typeface>>()

    fun setCustomFont(textViewOrButton: View, ctx: Context, attrs: AttributeSet, attributeSet: IntArray, fontId: Int) {
        val a = ctx.obtainStyledAttributes(attrs, attributeSet)
        val customFont = a.getString(fontId)
        setCustomFont(textViewOrButton, ctx, customFont)
        a.recycle()
    }

    private fun setCustomFont(textView: View, ctx: Context, asset: String?): Boolean {
        if (TextUtils.isEmpty(asset)) {
            return false
        }

        try {
            if (textView is TextView) {
                textView.typeface = getFont(ctx, asset)
            }
        } catch (e: Exception) {
            Log.e(TAG, "Could not get typeface: " + asset!!, e)
            return false
        }

        return true
    }

    public fun getFont(context: Context, name: String?): Typeface {
        synchronized(fontCache) {
            return if (fontCache[name] != null) {
                fontCache[name]!!.get()!!
            } else {
                val typeface = getTypeface(context, FONT_ASSETS_DIRECTORY + name)
                fontCache[name] = SoftReference(typeface)
                typeface
            }
        }
    }

    private fun getTypeface(context: Context, dirName: String): Typeface {
        return Typeface.createFromAsset(context.assets, dirName)
    }
}
