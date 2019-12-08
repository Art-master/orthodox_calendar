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
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp.CalendarTileMonthFragment
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import kotlinx.android.synthetic.main.fragment_tile_calendar.*
import kotlinx.android.synthetic.main.fragment_tile_calendar.view.*

internal class CalendarTileFragment: MvpAppCompatFragment(), ContractTileView {

    @InjectPresenter(tag = "TilePresenter", type = PresenterType.GLOBAL)
    lateinit var presenter: TilePresenter

    lateinit var tileView: View

    private lateinit var adapter : FragmentStatePagerAdapter
    private val monthSize =12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if(!presenter.isInRestoreState(this)){
            presenter.attachView(this)
            presenter.viewIsReady()
        }
        adapter = buildAdapter()
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
    }

    override fun initSpinner(){
        setMonthSpinner()
    }

    private fun getYear() = arguments!!.getInt(Constants.Keys.YEAR.value, Time().year)
    private fun getMonth() = arguments!!.getInt(Constants.Keys.MONTH.value, Time().month-1)
    private fun getDay() = arguments!!.getInt(Constants.Keys.DAY.value, Time().dayOfMonth)
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

    private fun buildAdapter(): FragmentStatePagerAdapter {
        //if(!isAdded) return null
        return object :FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(p0: Int): Fragment {
                val fragment = CalendarTileMonthFragment()
                val args = Bundle()
                args.putInt(Constants.Keys.MONTH.value, p0)
                fragment.arguments = args
                return fragment
            }

            override fun getCount(): Int = monthSize
        }
    }

    private fun setChangePageListener() {
        tileView.holidayTilePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                tileView.monthSpinner.setSelection(position)
                setVisibleArrows(position)
            }
        })
    }

    private fun setVisibleArrows(position: Int){
        val firstPosition = 0
        val lastPosition = monthSize - 1

        when (position) {
            lastPosition -> arrowRight.visibility = View.GONE
            firstPosition -> arrowLeft.visibility = View.GONE
            else -> {
                arrowLeft.visibility = View.VISIBLE
                arrowRight.visibility = View.VISIBLE
            }
        }
    }

    override fun upadteView() {
        val position = tileView.holidayTilePager.currentItem
        tileView.holidayTilePager.adapter = buildAdapter()
        tileView.holidayTilePager.currentItem = position
    }
}
