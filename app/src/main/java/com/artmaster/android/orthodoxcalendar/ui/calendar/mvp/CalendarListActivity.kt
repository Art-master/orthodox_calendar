package com.artmaster.android.orthodoxcalendar.ui.calendar.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import com.artmaster.android.orthodoxcalendar.R
import kotlinx.android.synthetic.main.activity_calendar.*
import com.artmaster.android.orthodoxcalendar.common.Message
import com.artmaster.android.orthodoxcalendar.common.OrtUtils
import com.artmaster.android.orthodoxcalendar.common.OrtUtils.checkFragment
import com.artmaster.android.orthodoxcalendar.ui.MassageBuilderFragment
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppInfoView
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppSettingView
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject

class CalendarListActivity : AppCompatActivity(), HasSupportFragmentInjector, CalendarListContract.View {

    @Inject
    lateinit var database: AppDatabase
    @Inject
    lateinit var model: CalendarListContract.Model
    @Inject
    lateinit var presenter: CalendarListContract.Presenter
    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>
    @Inject
    lateinit var listHolidayFragment: ListViewContract.ViewList

    @Inject
    lateinit var appInfoFragment: AppInfoView

    @Inject
    lateinit var appSettingsFragment: AppSettingView

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar)

        presenter.attachView(this)
        presenter.viewIsReady()
    }

    override fun showActionBar() = supportActionBar!!.show()

    override fun hideActionBar() {
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        var fragment: Fragment? = null
        when (item!!.itemId) {
            R.id.item_about -> fragment = checkFragment(appInfoFragment)
            R.id.item_settings -> fragment = checkFragment(appSettingsFragment)
        }
        if (fragment != null) {
            if (checkDoubleClickItem(fragment)) {
                showFragment(checkFragment(listHolidayFragment))
                removeFragment(fragment)
            } else {
                hideFragment(checkFragment(listHolidayFragment))
                replaceFragment(R.id.menu_fragments_container, fragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showHolidayList() {
        val fragment = checkFragment(listHolidayFragment)
        addFragment(R.id.activityCalendar, fragment)
    }

    override fun showErrorMessage(msgType: Message.ERROR) {
        val bundle = Bundle()
        bundle.putString(msgType.name, msgType.toString())

        val dialogError = MassageBuilderFragment()
        dialogError.arguments = bundle
        dialogError.show(supportFragmentManager, "dialogError")
    }

    override fun supportFragmentInjector(): AndroidInjector<Fragment> {
        return fragmentInjector
    }

    private fun showFragment(fragment: Fragment) {
        if (fragment.isHidden) {
            supportFragmentManager
                    .beginTransaction()
                    .show(fragment)
                    .commit()
        }
    }

    private fun hideFragment(fragment: Fragment) {
        if (!fragment.isHidden) {
            supportFragmentManager
                    .beginTransaction()
                    .hide(fragment)
                    .commit()
        }
    }

    private fun removeFragment(fragment: Fragment) {
        if (fragment.isAdded) {
            supportFragmentManager
                    .beginTransaction()
                    .remove(fragment)
                    .commit()
        }
    }

    private fun addFragment(resId: Int, fragment: Fragment) {
        if (!fragment.isAdded) {
            supportFragmentManager
                    .beginTransaction()
                    .add(resId, fragment)
                    .commit()
        }
    }

    private fun replaceFragment(resId: Int, fragment: Fragment) {
        addFragment(resId, fragment)
        if (!fragment.isHidden) {
            supportFragmentManager
                    .beginTransaction()
                    .replace(resId, fragment)
                    .commit()
        }
    }

    private fun checkDoubleClickItem(fragment: Fragment): Boolean {
        return supportFragmentManager.fragments.contains(fragment)
    }

    override fun onBackPressed() {
        when {
            checkDoubleClickItem(checkFragment(appSettingsFragment)) -> {
                removeFragment(appSettingsFragment as Fragment)
                showFragment(checkFragment(listHolidayFragment))
            }

            checkDoubleClickItem(checkFragment(appInfoFragment)) -> {
                removeFragment(appInfoFragment as Fragment)
                showFragment(checkFragment(listHolidayFragment))
            }

            else -> OrtUtils.exitProgram(this)
        }
    }
}