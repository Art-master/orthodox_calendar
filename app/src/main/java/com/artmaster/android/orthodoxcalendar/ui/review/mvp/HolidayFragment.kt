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
import kotlinx.android.synthetic.main.fragment_holiday.*
import kotlinx.android.synthetic.main.fragment_holiday.view.*
import javax.inject.Inject


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
        holidayView.initial_later_text_view.text = initialLater
        holidayView.description_text_view.text = description
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

    override fun showErrorMassage(msgType: Message.ERROR) {
    }
}
