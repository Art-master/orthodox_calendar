package com.artmaster.android.orthodoxcalendar.data.font

import android.graphics.Typeface
import android.text.TextPaint
import android.text.style.CharacterStyle
import android.text.style.UpdateAppearance

/**
 * Class create mutable text and let control his parameters
 */
class MutableForegroundSpan : CharacterStyle(), UpdateAppearance {

    companion object {
        val TAG = "MutableForegroundSpan"
    }

    var color: Int = 0

    private var typeFace: Typeface? = null

    private var fontSize: Int = 0

    private var scaleX: Float = 0.toFloat()

    override fun updateDrawState(tp: TextPaint) {
        tp.color = color
        if (typeFace != null) tp.typeface = typeFace
        if (fontSize != 0) tp.textSize = fontSize.toFloat()
        if (scaleX != 0f) tp.textScaleX = scaleX
    }
}
