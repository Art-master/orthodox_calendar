package com.artmaster.android.orthodoxcalendar.data.components

import android.graphics.Canvas
import android.graphics.Paint
import android.text.Layout
import android.text.style.LeadingMarginSpan

class CustomLeadingMarginSpan2(private var margin: Int = 0, private var lines: Int = 0)
    : LeadingMarginSpan.LeadingMarginSpan2 {

    /* Возвращает значение, на которе должен быть добавлен отступ */
    override fun getLeadingMargin(first: Boolean): Int {
        return if (first)
            margin else 0
    }

    override fun drawLeadingMargin(c: Canvas, p: Paint, x: Int, dir: Int,
                                   top: Int, baseline: Int, bottom: Int, text: CharSequence,
                                   start: Int, end: Int, first: Boolean, layout: Layout) {
    }

    /*
     * Возвращает количество строк, к которым должен быть
     * применен отступ возвращаемый методом getLeadingMargin(true)
     * Замечание:
     * Отступ применяется только к N строкам первого параграфа.
     */
    override fun getLeadingMarginLineCount(): Int {
        return lines
    }
}