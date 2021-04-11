package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.info

import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.data.font.MutableForegroundSpan
import com.artmaster.android.orthodoxcalendar.data.font.TextViewWithCustomFont
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.AppInfoView

/**
 * Fragment, what show info app
 */
class FragmentAppInfo : Fragment(), AppInfoView {
    lateinit var spannableString: SpannableString

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_app_info, container, false)

        val textView = view.findViewById<TextViewWithCustomFont>(R.id.page_info_text)

        val text = getText(R.string.text_view_about_app).toString()
        //строки для поиска
        val string1 = "Христ"
        val string2 = "Иисус"

        spannableString = SpannableString(text)

        val num1 = getNumRelapse(text, string1)
        val num2 = getNumRelapse(text, string2)
        val spans1 = arrayOfNulls<MutableForegroundSpan>(num1)
        val spans2 = arrayOfNulls<MutableForegroundSpan>(num2)

        setStyleForText(text, spans1, string1)
        setStyleForText(text, spans2, string2)

        textView.text = spannableString

        return view
    }

    private fun setColorForText(span: MutableForegroundSpan?, index: Int, text: String, string: String) {
        val stringCompare1 = text.indexOf(string, index)
        var stringCompare2 = text.indexOf(" ", stringCompare1)
        if (text[stringCompare2 - 1] == "."[0]) stringCompare2--
        spannableString.setSpan(span, stringCompare1, stringCompare2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        span!!.color = Color.RED
    }


    /**
     * @return num relapse string [findString] in [string]
     */
    private fun getNumRelapse(string: String, findString: String, index: Int = 0): Int {
        var i = index
        return if (string.indexOf(findString, i) != -1) {
            i = string.indexOf(findString, i)
            getNumRelapse(string, findString, i + findString.length + 1) + 1
        } else 0
    }

    /**
     * Find relapse in the [text] and styling the [spans]
     */
    private fun setStyleForText(text: String, spans: Array<MutableForegroundSpan?>,
                                stringFind: String, initIndex: Int = 0, index: Int = 0) {
        var initIndexForSpanMassive = initIndex
        if (text.indexOf(stringFind, index) != -1) {
            spans[initIndexForSpanMassive] = MutableForegroundSpan()
            setColorForText(spans[initIndexForSpanMassive], index, text, stringFind)
            val r = text.indexOf(stringFind, index)
            setStyleForText(text, spans, stringFind, ++initIndexForSpanMassive, r + stringFind.length + 1)
        }
    }

}
