package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.MessageBuilderFragment
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp.CalendarListActivity
import com.artmaster.android.orthodoxcalendar.ui.init.fragments.LoadingScreenFragment
import dagger.android.AndroidInjection
import javax.inject.Inject

class InitAppActivity : InitAppContract.View, AppCompatActivity() {

    @Inject
    lateinit var model: InitAppContract.Model

    @Inject
    lateinit var presenter: InitAppContract.Presenter

    lateinit var bundle: Bundle

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_init_app)

        bundle = savedInstanceState ?: Bundle.EMPTY

        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun showLoadingScreen() {
        val fm = supportFragmentManager

        val fr = LoadingScreenFragment()
        fr.retainInstance = false
        if (bundle.isEmpty) {
            fm.beginTransaction().add(R.id.activity_init, fr).commit()
        } else {
            fr.onDestroy()
        }
    }

    override fun showErrorMassage(msgType: Message.ERROR) {
        val bundle = Bundle()
        bundle.putString(msgType.name, msgType.toString())

        val dialog = MessageBuilderFragment()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "dialogError")
    }

    override fun initNotifications() {
        AlarmBuilder.build(applicationContext)
    }

    override fun nextScreen() {
        startActivity(getArgs())
        finish()
    }

    private fun getArgs() = Intent(applicationContext, CalendarListActivity::class.java).apply {
        putExtra(Constants.Keys.YEAR.value, Time().year)
        putExtra(Constants.Keys.MONTH.value, Time().monthWith0)
        putExtra(Constants.Keys.DAY.value, Time().dayOfMonth)
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }

    override fun onBackPressed() = OrtUtils.exitProgram(applicationContext)
}
