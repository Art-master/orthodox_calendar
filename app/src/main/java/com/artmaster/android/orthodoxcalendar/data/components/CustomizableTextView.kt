package com.artmaster.android.orthodoxcalendar.data.components

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.Spanned
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants

/**
 * This class execute text animation
 * Created by Art-_-master on 05.09.2017.
 */
class CustomizableTextView : AppCompatTextView {

    private var textString: String = ""

    //parts of text
    private var spannableString: SpannableString? = null

    //alpha channel values for symbols
    private var alphas: DoubleArray? = null

    private var mSpans: Array<MutableForegroundSpan>? = null

    private var isVisible = false
    private var isTextResetting = false

    private var durationAnim = Constants.LOADING_ANIMATION_DURATION.toLong()

    private var animator: ValueAnimator? = null

    /**
     * Animator listener
     * Reloading visibility of text
     */
    private var listener = { valueAnimator: ValueAnimator ->
        val percent = valueAnimator.animatedValue as Float
        resetSpannableString((if (isVisible) percent else 2.0f - percent).toDouble())
    }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
        CustomFont.setCustomFont(this, context, attrs,
                R.styleable.customizableView,
                R.styleable.customizableView_customFont)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init()
        CustomFont.setCustomFont(this, context, attrs,
                R.styleable.customizableView,
                R.styleable.customizableView_customFont)
    }

    private fun init() {
        animator = ValueAnimator.ofFloat(0.0f, 2.0f)
        animator!!.addUpdateListener(listener)
        animator!!.duration = durationAnim.toLong()
    }

    fun toggleReverseAnim() {
        init()
        if (isVisible) {
            hide()
        } else {
            show()
        }
    }

    fun show() {
        isVisible = true
        animator!!.start()
    }

    fun hide() {
        isVisible = false
        animator!!.start()
    }

    fun setVisible(isVisible: Boolean) {
        this.isVisible = isVisible
        resetSpannableString((if (isVisible) 2.0f else 0.0f).toDouble())
    }

    /**
     * Reloading transparency of text
     */
    private fun resetSpannableString(percentTransparency: Double) {
        isTextResetting = true

        val color = currentTextColor
        for (i in 0 until this.textString.length) {
            val span = mSpans!![i]
            span.color = Color.argb(clamp(alphas!![i] + percentTransparency),
                    Color.red(color), Color.green(color), Color.blue(color))
        }

        text = spannableString

        isTextResetting = false
    }

    /**
     * Reloading transparency of symbols
     * @param length - length massive with symbols
     */
    private fun resetAlphas(length: Int) {
        alphas = DoubleArray(length)
        for (i in alphas!!.indices) {
            alphas!![i] = Math.random() - 1
        }
    }

    /** if text need reset, then func reset alphas and SpannableString  */
    private fun resetIfNeeded() {
        if (!isTextResetting) {
            textString = text.toString()
            spannableString = SpannableString(textString)
            mSpans = Array(textString.length) { MutableForegroundSpan() }
            for (i in 0 until textString.length) {
                val span = MutableForegroundSpan()
                spannableString!!.setSpan(span, i, i + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                mSpans!![i] = span
            }
            resetAlphas(textString.length)
            resetSpannableString((if (isVisible) 2.0 else 0.0))
        }
    }

    fun setText(text: String) {
        super.setText(text)
        resetIfNeeded()
    }

    /**
     * calculating alpha values from 0 to 1
     * @param value - value for calculate
     * @return alpha values from 0 to 1
     */
    private fun clamp(value: Double): Int {
        return (255 * Math.min(Math.max(value, 0.0), 1.0)).toInt()
    }

    fun setDurationAnim(durationMs: Long) {
        this.durationAnim = durationMs
        animator!!.duration = durationMs
    }

    /** Reverse animation */
    fun reverseAnimation() {
/*        animator!!.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {}

            override fun onAnimationEnd(animation: Animator?) {
                toggleReverseAnim()
                animation!!.removeListener(this)
            }

            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationStart(animation: Animator?) {}
        })*/
    }

    fun cancelAnimation() {
        animator!!.end()
        animator!!.removeAllListeners()
        animator!!.removeAllUpdateListeners()
    }
}
