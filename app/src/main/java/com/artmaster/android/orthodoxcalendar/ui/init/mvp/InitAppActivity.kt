package com.artmaster.android.orthodoxcalendar.ui.init.mvp

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.ui.MassageBuilderFragment
import com.artmaster.android.orthodoxcalendar.ui.calendar.mvp.CalendarListActivity
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

    override fun onBackPressed() = OrtUtils.exitProgram(applicationContext)
}
