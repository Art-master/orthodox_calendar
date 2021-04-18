package com.artmaster.android.orthodoxcalendar.ui.user_holiday.mvp

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.artmaster.android.orthodoxcalendar.databinding.ActivityUserHolidayBinding
import com.artmaster.android.orthodoxcalendar.domain.Holiday
import com.artmaster.android.orthodoxcalendar.ui.user_holiday.impl.ContractUserHolidayView

class UserHolidayActivity : AppCompatActivity(), ContractUserHolidayView {

    //@InjectPresenter(tag = "UserHolidayPresenter")
    //lateinit var presenter: UserHolidayPresenter

    lateinit var holiday: Holiday

    private var _binding: ActivityUserHolidayBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityUserHolidayBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //holiday = savedInstanceState?.get(Constants.Keys.HOLIDAY.value) as Holiday

        // if (presenter.isViewAttached()) {
        //    presenter.attachView(this)
        //     presenter.viewIsReady()
        // }
    }

    override fun onCreateView(parent: View?, name: String, context: Context, attrs: AttributeSet): View? {
        return super.onCreateView(parent, name, context, attrs)

    }

    override fun showData(holiday: Holiday) {

    }
}
