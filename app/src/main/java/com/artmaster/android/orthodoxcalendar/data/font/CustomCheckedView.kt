package com.artmaster.android.orthodoxcalendar.data.font

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatCheckBox
import com.artmaster.android.orthodoxcalendar.R

class CustomCheckedView : AppCompatCheckBox {
    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        setCustomFont(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        setCustomFont(context, attrs)
    }

    private fun setCustomFont(context: Context, attrs: AttributeSet) {
        CustomFont.setCustomFont(this, context, attrs,
                R.styleable.customizableView,
                R.styleable.customizableView_customFont)
    }
}