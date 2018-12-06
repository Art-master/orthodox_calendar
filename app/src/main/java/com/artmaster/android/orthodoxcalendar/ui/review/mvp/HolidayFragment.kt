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
import dagger.android.support.AndroidSupportInjection
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
        val v = inflater.inflate(R.layout.fragment_holiday, groupContainer, false)
        presenter.attachView(this)
        holidayView = v
        presenter.viewIsReady()
        return v
    }

    override fun showHolidayName(name: String) {
        holidayView.holiday_name_in_page.text = name
    }

    override fun showDescription(description: String) {
        holidayView.description_text_view.text = description
    }

    override fun showNewStyleDate(date: String) {
        holidayView.new_date_style_text_view.text = date
    }

    override fun showOldStyleDate(date: String) {
        holidayView.old_date_style_text_view.text = date
    }

    override fun showImageHoliday() {
    }

    override fun showErrorMassage(msgType: Message.ERROR) {
    }
}
