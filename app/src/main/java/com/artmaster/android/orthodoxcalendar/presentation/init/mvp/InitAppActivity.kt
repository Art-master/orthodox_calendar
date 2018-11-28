package com.artmaster.android.orthodoxcalendar.presentation.init.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.data.MassageBuilderFragment
import com.artmaster.android.orthodoxcalendar.presentation.calendar.mvp.CalendarListActivity
import dagger.android.AndroidInjection
import javax.inject.Inject

class InitAppActivity : InitAppContract.View, AppCompatActivity() {

    @Inject
    lateinit var model: InitAppContract.Model
    @Inject
    lateinit var presenter: InitAppContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun showLoadingScreen() {
    }

    override fun showErrorMassage(msgType: Message.ERROR) {
        val bundle = Bundle()
        bundle.putString(msgType.name, msgType.toString())
        val dialog = MassageBuilderFragment()
        dialog.arguments = bundle
        dialog.show(supportFragmentManager, "dialogError")
    }

    override fun nextScreen() {
        val intent = Intent(applicationContext, CalendarListActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION)
        startActivity(intent)
        finish()
    }
}
