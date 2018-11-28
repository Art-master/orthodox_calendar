package com.artmaster.android.orthodoxcalendar.data.font

import android.content.Context
import android.util.AttributeSet
import android.support.v7.widget.AppCompatTextView

import com.artmaster.android.orthodoxcalendar.R

class TextViewWithCustomFont : AppCompatTextView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        CustomFont.setCustomFont(this, context, attrs,
                R.styleable.customizableTextView,
                R.styleable.customizableTextView_customFont)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        CustomFont.setCustomFont(this, context, attrs,
                R.styleable.customizableTextView,
                R.styleable.customizableTextView_customFont)
    }
}
