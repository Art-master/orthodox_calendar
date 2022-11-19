package com.artmaster.android.orthodoxcalendar.ui.holiday_page.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.text.TextPaint
import android.util.DisplayMetrics
import android.util.TypedValue
import androidx.annotation.FontRes
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.content.res.ResourcesCompat

class HeadSymbol(private val context: Context) {

    private var textPaint: Paint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private var symbol: Char = ' '
    private var widths: FloatArray = FloatArray(1)
    private var minSymWidth: Float = 0f
    private var fontDescent: Int = 0
    private var fontInterline: Int = 0
    private var fontLineHeight: Int = 0
    private var w: Int = 0
    private var h: Int = 0

    fun onDraw(canvas: Canvas) {
        canvas.drawText(symbol.toString(), 0, 1, 0f, h.toFloat() + 30, textPaint)
    }

    fun setSize(size: Int) {
        w = size
        h = size
    }

    fun setTextColor(color: Color) {
        textPaint.color = color.toArgb()
    }

    fun setTextScaleX(scale: Float) {
        textPaint.textScaleX = scale
    }

    /** set start symbol size */
    fun setRawTextSize(size: Int) {
        if (size.toFloat() != textPaint.textSize) {
            textPaint.textSize = size.toFloat()

            val fm = textPaint.fontMetricsInt
            fontDescent = fm.descent
            fontInterline = fm.descent - fm.ascent
            fontLineHeight = -fm.top

            val widths = FloatArray(1)
            textPaint.getTextWidths(" ", widths)
            minSymWidth = widths[0]
        }
    }

    fun setHeadSymbol(symbol: Char) {
        this.symbol = symbol

        //get chars length
        textPaint.getTextWidths(this.symbol.toString(), widths)

    }

    fun setTextSize(unit: Int, size: Float, metrics: DisplayMetrics) {
        setRawTextSize(TypedValue.applyDimension(unit, size, metrics).toInt())
    }

    /** set custom font */
    fun setTypeface(tf: Typeface) {
        if (textPaint.typeface !== tf) {
            textPaint.typeface = tf
        }
    }

    fun setFont(@FontRes id: Int) {
        val typeface = ResourcesCompat.getFont(context, id)
        setTypeface(typeface!!)
    }
}
