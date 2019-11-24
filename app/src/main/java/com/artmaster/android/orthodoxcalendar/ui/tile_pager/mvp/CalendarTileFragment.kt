package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.SpinnerAdapter
import com.artmaster.android.orthodoxcalendar.domain.Time2
import com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp.CalendarTileMonthFragment
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import kotlinx.android.synthetic.main.fragment_tile_calendar.view.*

internal class CalendarTileFragment: MvpAppCompatFragment(), ContractTileView {

    @InjectPresenter(tag = "TilePresenter", type = PresenterType.GLOBAL)
    lateinit var presenter: TilePresenter

    lateinit var tileView: View

    private lateinit var adapter : FragmentStatePagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //retainInstance = false
        if(!presenter.isInRestoreState(this)){
            presenter.attachView(this)
            presenter.viewIsReady()
        }
        adapter = getAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View? {
        tileView = inflater.inflate(R.layout.fragment_tile_calendar, groupContainer, false)
        return tileView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(!presenter.isInRestoreState(this))presenter.viewIsCreated()
        setChangePageListener()
    }

    override fun setPageAdapter() {
        if(tileView.holidayTilePager.adapter != null) return
        tileView.holidayTilePager.adapter =  adapter
        setCurrentItem()
    }

    override fun initSpinner(){
        setMonthSpinner()
    }

    private fun getYear() = arguments!!.getInt(Constants.Keys.YEAR.value, Time2().year)
    private fun getMonth() = arguments!!.getInt(Constants.Keys.MONTH.value, Time2().month)
    private fun getDay() = arguments!!.getInt(Constants.Keys.DAY.value, Time2().dayOfMonth)
    private fun getMonthsNames() =  resources.getStringArray(R.array.months)

    private fun setMonthSpinner(){
        val mNames = getMonthsNames()
        val adapter = SpinnerAdapter(context!!, R.layout.spinner_year_item, mNames)
        adapter.setDropDownViewResource(R.layout.spinner_year_dropdown)
        tileView.monthSpinner.adapter = adapter
        val monthNum = getMonth()
        tileView.monthSpinner.setSelection(monthNum)
        setOnItemSpinnerSelected()
    }

    private fun setOnItemSpinnerSelected(){
        tileView.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                tileView.holidayTilePager.currentItem = position
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun getAdapter(): FragmentStatePagerAdapter {
        //if(!isAdded) return null
        return object :FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(p0: Int): Fragment {
                val fragment = CalendarTileMonthFragment()
                fragment.arguments = arguments
                fragment.arguments?.putInt(Constants.Keys.MONTH.value, p0)
                return fragment
            }

            override fun getCount(): Int = 12 //month size
        }
    }

    private fun setCurrentItem() {
        //val uuid = intent.getSerializableExtra(Constants.Keys.HOLIDAY_ID.name)
       // holidayTilePager.currentItem = index
    }

    private fun setChangePageListener() {
        tileView.holidayTilePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                tileView.monthSpinner.setSelection(position)

            }
        })
    }

    private fun getUpdatedArrgs(): Bundle {
        return Bundle().apply {
            putInt(Constants.Keys.YEAR.value, getYear())
            putInt(Constants.Keys.MONTH.value, tileView.monthSpinner.selectedItemPosition)
            putInt(Constants.Keys.DAY.value, 13)// change
        }
    }
}
