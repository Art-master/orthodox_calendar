package com.artmaster.android.orthodoxcalendar.presentation.calendar.mvp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.artmaster.android.orthodoxcalendar.R
import kotlinx.android.synthetic.main.activity_calendar.*
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.data.MassageBuilderFragment
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.presentation.calendar.impl.ListViewContract
import dagger.android.AndroidInjection
import javax.inject.Inject

class CalendarListActivity : AppCompatActivity(), CalendarListContract.View {

    @Inject
    lateinit var database: AppDatabase
    @Inject
    lateinit var model: CalendarListContract.Model
    @Inject
    lateinit var presenter: CalendarListContract.Presenter

    @Inject
    lateinit var adapter: ListViewContract.Adapter

    override fun onCreate(savedInstanceState: Bundle?) {

        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_calendar)
        setSupportActionBar(findViewById(R.id.toolbar))

        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun showActionBar() {
        supportActionBar!!.show()
    }

    override fun hideActionBar() {
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.hide()

    }

    override fun showHolidayList() {
        recyclerView!!.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter as? RecyclerView.Adapter<*>
    }

    override fun showErrorMassage(msgType: Message.ERROR) {
        val bundle = Bundle()
        bundle.putString(msgType.name, msgType.toString())

        val dialogError = MassageBuilderFragment()
        dialogError.arguments = bundle
        dialogError.show(supportFragmentManager, "dialogError")
    }

    override fun onBackPressed() {
        OrtUtils.exitProgram(this)
    }
}