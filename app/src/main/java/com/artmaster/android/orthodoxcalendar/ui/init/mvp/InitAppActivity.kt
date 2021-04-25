package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.common.msg.Error
import com.artmaster.android.orthodoxcalendar.notifications.AlarmBuilder
import com.artmaster.android.orthodoxcalendar.ui.MessageBuilderFragment
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.mvp.MainCalendarActivity
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

    override fun showLoadingScreen(timeAnimation: Long) {
        val fm = supportFragmentManager

        val fr = LoadingScreenFragment()
        val bundle = Bundle()
        bundle.putLong(Constants.Keys.ANIM_TIME.value, timeAnimation)
        fr.arguments = bundle
        if (this.bundle.isEmpty) {
            fm.beginTransaction().add(R.id.activity_init, fr).commit()
        } else {
            fr.onDestroy()
        }
    }

    override fun showErrorMessage(msgType: Error) {
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

    private fun getArgs() = Intent(applicationContext, MainCalendarActivity::class.java).apply {
        addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
    }

    override fun onBackPressed() = OrtUtils.exitProgram(applicationContext)
}
