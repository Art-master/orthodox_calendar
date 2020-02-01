package com.artmaster.android.orthodoxcalendar.ui.calendar.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.MvpView
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.artmaster.android.orthodoxcalendar.App
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.*
import com.artmaster.android.orthodoxcalendar.common.OrtUtils.checkFragment
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.impl.AppDatabase
import com.artmaster.android.orthodoxcalendar.ui.CalendarUpdateContract
import com.artmaster.android.orthodoxcalendar.ui.MessageBuilderFragment
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppInfoView
import com.artmaster.android.orthodoxcalendar.ui.calendar.fragments.impl.AppSettingView
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.CalendarListContractModel
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.CalendarListContractView
import com.artmaster.android.orthodoxcalendar.ui.calendar.impl.ListViewContract
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.support.HasSupportFragmentInjector
import kotlinx.android.synthetic.main.activity_calendar.*
import javax.inject.Inject

class CalendarListActivity : MvpAppCompatActivity(), HasSupportFragmentInjector, CalendarListContractView, MvpView {

    @Inject
    lateinit var database: AppDatabase

    @Inject
    lateinit var model: CalendarListContractModel

    @InjectPresenter(tag = "ListPresenter", type = PresenterType.LOCAL)
    lateinit var presenter: CalendarListPresenter

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Fragment>

    @Inject
    lateinit var listHolidayFragment: ListViewContract.ViewListPager

    @Inject
    lateinit var tileCalendarFragment: ContractTileView

    @Inject
    lateinit var appInfoFragment: AppInfoView

    @Inject
    lateinit var appSettingsFragment: AppSettingView

    private val preferences = App.appComponent.getPreferences()

    private val isFirstLoadTileCalendar = preferences.get(Settings.Name.FIRST_LOADING_TILE_CALENDAR).toBoolean()

    private var toolbarMenu: Menu? = null

    private lateinit var mainFragment : Fragment
    private var fragment : Fragment = Fragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar)
        setSupportActionBar(toolbar)
        initTypeOfCalendar()

        listHolidayFragment.onChangePageListener { controlSpinner(it) }

        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)
            presenter.viewIsReady()
        }

        initBarSpinner()
    }

    private fun initTypeOfCalendar(){
        mainFragment = if(isFirstLoadTileCalendar){
            tileCalendarFragment as Fragment
        } else {
            listHolidayFragment as Fragment
        }
        setArguments(mainFragment)
    }

    private fun controlSpinner(position: Int){
        val firstPosition = 0
        val lastPosition = getYears().size - 1
        when (position) {
            lastPosition -> arrowRight.visibility = View.GONE
            firstPosition -> arrowLeft.visibility = View.GONE
            else -> {
                arrowRight.visibility = View.VISIBLE
                arrowLeft.visibility = View.VISIBLE
            }
        }
        toolbarYearSpinner.setSelection(position)
    }

    private fun getYear() = intent.getIntExtra(Constants.Keys.YEAR.value, Time().year)
    private fun getMonth() = intent.getIntExtra(Constants.Keys.MONTH.value, Time().monthWith0)
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

        changeIconTypeCalendar()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        val fragment = when (item!!.itemId) {
            R.id.item_about -> checkFragment(appInfoFragment)
            R.id.item_settings -> checkFragment(appSettingsFragment)
            R.id.item_reset_date -> {
                resetDateState()
                null
            }
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
            tileCalendarFragment as Fragment
        }else {
            listHolidayFragment as Fragment
        }

        changeIconTypeCalendar()

        replaceFragment(R.id.activityCalendar, mainFragment)
        setArguments(mainFragment)
    }

    private fun changeIconTypeCalendar(){
        if(mainFragment is ListViewContract.ViewListPager){
            toolbarMenu?.getItem(0)?.setIcon(R.drawable.icon_tile)
        }else {
            toolbarMenu?.getItem(0)?.setIcon(R.drawable.icon_list)
        }
    }

    private fun setArguments(fragment: Fragment){
        fragment.arguments = Bundle().apply {
            putInt(Constants.Keys.YEAR.value, getYear())
            putInt(Constants.Keys.MONTH.value, getMonth())
            putInt(Constants.Keys.DAY.value, getDay())
            putInt(Constants.Keys.INIT_LIST_POSITION.value,
                    intent.getIntExtra(Constants.Keys.INIT_LIST_POSITION.value, 0))
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

        val dialogError = MessageBuilderFragment()
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
                if (getCurrentYear() != getYears()[position].toInt()) {
                    setArgYear(mainFragment, getCurrentYear())
                    (mainFragment as CalendarUpdateContract).updateYear()
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) { }
        }
    }

    private fun getCurrentYear(): Int {
        val years = getYears()
        val currentYear = years[toolbarYearSpinner.selectedItemPosition]
        return currentYear.toInt()
    }

    private fun resetDateState(){
        val fr = mainFragment as CalendarUpdateContract
        val time = Time()

        resetArgsValues()
        if (time.year == getYear()) {
            val years = getYears()
            val pos = years.indexOf(getYear().toString())
            toolbarYearSpinner.setSelection(pos)
            fr.updateYear()
        }
        if (time.monthWith0 == getMonth()) fr.updateMonth()
    }

    private fun resetArgsValues(){
        val time = Time()
        intent.putExtra(Constants.Keys.YEAR.value, time.year)
        intent.putExtra(Constants.Keys.MONTH.value, time.monthWith0)
        intent.putExtra(Constants.Keys.DAY.value, time.dayOfMonth)
    }

    override fun setInitPosition(index: Int) {
        intent.putExtra(Constants.Keys.INIT_LIST_POSITION.value, index)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }
}