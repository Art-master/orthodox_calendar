package com.artmaster.android.orthodoxcalendar.ui.calendar.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.*
import kotlinx.android.synthetic.main.activity_calendar.*
import com.artmaster.android.orthodoxcalendar.common.OrtUtils.checkFragment
import com.artmaster.android.orthodoxcalendar.ui.MassageBuilderFragment
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppInfoView
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppSettingView
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import com.artmaster.android.orthodoxcalendar.domain.Time
import android.widget.AdapterView.OnItemSelectedListener
import android.view.View


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
    lateinit var listHolidayFragment: ListViewContract.ViewListPager

    @Inject
    lateinit var appInfoFragment: AppInfoView

    @Inject
    lateinit var appSettingsFragment: AppSettingView

    @Inject
    lateinit var appViewFragment: ContractTileView

    private var toolbarMenu: Menu? = null

    private lateinit var mainFragment : Fragment
    private var fragment : Fragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar)
        mainFragment = listHolidayFragment as Fragment
        listHolidayFragment.onChangePageListener { toolbarYearSpinner.setSelection(it) }
        presenter.attachView(this)
        presenter.viewIsReady()
        initBarSpinner()
    }

    private fun getYear() = intent.getIntExtra(Constants.Keys.YEAR.value, Time().year)
    private fun getMonth() = intent.getIntExtra(Constants.Keys.MONTH.value, Time().month -1) //with 1
    private fun getDay() = intent.getIntExtra(Constants.Keys.DAY.value, Time().dayOfMonth)

    private fun getYears(): ArrayList<String> {
      val size = Constants.HolidayList.PAGE_SIZE.value
      val initYear = getYear() - size/2
      val years = ArrayList<String>(size)
      for (element in initYear..initYear+size){
          years.add(element.toString())
      }
      return years
    }

    override fun showActionBar() = supportActionBar!!.show()

    override fun hideActionBar() {
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.hide()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_app, menu)
        toolbarMenu = menu
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val fragment = when (item!!.itemId) {
            R.id.item_about -> checkFragment(appInfoFragment)
            R.id.item_settings -> checkFragment(appSettingsFragment)
            else -> null
        }
        changeMainFragment(item)
        if (fragment != null) {
            if (isExist(fragment)) {
                showFragment(checkFragment(mainFragment))
                removeFragment(fragment)
            } else {
                this.fragment = fragment
                hideFragment(checkFragment(mainFragment))
                replaceFragment(R.id.menu_fragments_container, fragment)
            }
        }else if(item.itemId == R.id.item_view) {
            removeFragment(this.fragment)
            showFragment(checkFragment(mainFragment))
        }

        return super.onOptionsItemSelected(item)
    }

    private fun changeMainFragment(item: MenuItem){
        if(item.itemId != R.id.item_view) return
        mainFragment = if(mainFragment is ListViewContract.ViewListPager){
            toolbarMenu?.getItem(0)?.setIcon(R.drawable.list_item)
            appViewFragment as Fragment
        }else {
            toolbarMenu?.getItem(0)?.setIcon(R.drawable.item)
            listHolidayFragment as Fragment
        }

        replaceFragment(R.id.activityCalendar, mainFragment)
        setArguments(mainFragment)
    }
    private fun setArguments(fragment: Fragment){
        fragment.arguments = Bundle().apply {
            putInt(Constants.Keys.YEAR.value, getYear())
            putInt(Constants.Keys.MONTH.value, getMonth())
            putInt(Constants.Keys.DAY.value, getDay())
        }
    }

    private fun setArgYear(fragment: Fragment, year: Int){
        fragment.arguments?.putInt(Constants.Keys.YEAR.value, year)
    }

    override fun showHolidayList() {
        val fragment = checkFragment(mainFragment)
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

    private fun initBarSpinner(){
        val years = getYears()
        val adapter = SpinnerAdapter(this, R.layout.spinner_year_item, years.toTypedArray())
        adapter.setDropDownViewResource(R.layout.spinner_year_dropdown)
        toolbarYearSpinner.adapter = adapter
        val pos = years.indexOf(getYear().toString())
        toolbarYearSpinner.setSelection(pos)
        setOnItemSelected()
    }

    private fun showFragment(fragment: Fragment) {
        if (fragment.isHidden) {
            supportFragmentManager
                    .beginTransaction()
                    .setCustomAnimations(0,0)
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
                    .addToBackStack(null)
                    .commit()
        }
    }

    private fun isExist(fragment: Fragment): Boolean {
        return supportFragmentManager.fragments.contains(fragment)
    }

    override fun onBackPressed() {
        when {
            isExist(checkFragment(appSettingsFragment)) -> {
                removeFragment(appSettingsFragment as Fragment)
                showFragment(checkFragment(mainFragment))
            }

            isExist(checkFragment(appInfoFragment)) -> {
                removeFragment(appInfoFragment as Fragment)
                showFragment(checkFragment(mainFragment))
            }

            else -> OrtUtils.exitProgram(this)
        }
    }

    private fun setOnItemSelected(){
        toolbarYearSpinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                setArgYear(mainFragment, getCurrentYear())
                if(mainFragment is ListViewContract.ViewListPager){
                    (mainFragment as ListViewContract.ViewListPager).setPage(position)
                }else {
                    (mainFragment as ContractTileView).upadteView()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
            }

        }
    }

    private fun getCurrentYear(): Int {
        val years = getYears()
        val currentYear = years[toolbarYearSpinner.selectedItemPosition]
        return currentYear.toInt()
    }
}