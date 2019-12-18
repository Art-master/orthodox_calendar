package com.artmaster.android.orthodoxcalendar.ui.review.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.domain.HolidayEntity
import com.artmaster.android.orthodoxcalendar.ui.review.impl.HolidayReviewContract
import com.squareup.picasso.Picasso
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_holiday.view.*
import javax.inject.Inject
import android.util.TypedValue
import android.graphics.Color
import com.artmaster.android.orthodoxcalendar.data.font.JustifiedTextView
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import android.text.SpannableString
import com.artmaster.android.orthodoxcalendar.data.font.CustomFont
import com.artmaster.android.orthodoxcalendar.data.font.CustomLeadingMarginSpan2

class HolidayFragment : Fragment(), HolidayReviewContract.View {

    @Inject
    lateinit var presenter: HolidayReviewContract.Presenter

    lateinit var holiday: HolidayEntity

    lateinit var holidayView: View

    companion object {
        fun newInstance(holiday: HolidayEntity): HolidayFragment {
            val intent = Intent()
            intent.putExtra(Constants.Keys.HOLIDAY.name, holiday)
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)

            val fragment = HolidayFragment()
            fragment.arguments = intent.extras
            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)

        holiday = arguments!!.get(Constants.Keys.HOLIDAY.name) as HolidayEntity
        presenter.init(holiday)
    }

    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View? {
        holidayView = inflater.inflate(R.layout.fragment_holiday, groupContainer, false)
        presenter.attachView(this)
        presenter.viewIsReady()
        return holidayView
    }

    override fun showHolidayName(name: String) {
        holidayView.holiday_name_in_page.text = name
    }

    override fun showDescription(initialLater: String, description: String) {
        if (description.isEmpty()) return

        buildInitialLater(initialLater)
        val desc = prepareDescription(description)
        holidayView.relative_layout.addView(desc)
    }

    private fun prepareDescription(description: String): JustifiedTextView {
        return JustifiedTextView(context!!).apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimensionPixelSize(R.dimen.size_text_basic).toFloat())
            setTypeface(CustomFont.getFont(context!!, getString(R.string.font_basic)))
            setLeadingMargin(3, 8)
            val strWithoutSpaces = deleteSpaces(description)
            setText(getTextByPadding(strWithoutSpaces))
            setTextColor(holidayView.holiday_name_in_page.textColors)
        }
    }

    private fun deleteSpaces(string: String): String {
        val deleteSpaces = Regex.fromLiteral("[\\s&&[^\t?\n]]+")
       return string.replace(deleteSpaces, " ")
    }

    private fun buildInitialLater(later: String) {
         holidayView.initial_later_text_view.apply {
            val textName =  resources.getString(R.string.font_for_bukvica)
            val size = resources.getDimensionPixelSize(R.dimen.size_text_bukvica).toFloat()
            setTextSize(TypedValue.COMPLEX_UNIT_PX, size)
            setTextColor(Color.RED)
            typeface = CustomFont.getFont(context!!, textName)
            scaleX = 2F
            text = later
        }
    }

    private fun getTextByPadding(string: String): SpannableString {
        val finalStr = SpannableString(string)
        val size = OrtUtils.convertDpToPixels(context!!, 25f)
        finalStr.setSpan(CustomLeadingMarginSpan2(3, size),0, finalStr.length, 0)
        return finalStr
    }

    override fun showNewStyleDate(date: String) {
        if (date.isEmpty()) return

        val str = getString(R.string.new_style_date_string, date)
        holidayView.new_date_style_text_view.text = str
    }

    override fun showOldStyleDate(date: String) {
        if (date.isEmpty()) return

        val str = getString(R.string.old_style_date_string, date)
        holidayView.old_date_style_text_view.text = str
    }

    override fun showImageHoliday(resId: Int, placeholderId: Int) {
        Picasso.with(context)
                .load(resId)
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(holidayView.image_holiday)
    }

    override fun showErrorMessage(msgType: Message.ERROR) {
    }
}
