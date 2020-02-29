package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import android.os.Bundle
import android.support.v4.view.PagerAdapter
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
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_SIZE
import com.artmaster.android.orthodoxcalendar.common.SpinnerAdapter
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.CalendarUpdateContract
import com.artmaster.android.orthodoxcalendar.ui.CustomViewPager
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.fragment.CalendarInfoFragment
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import kotlinx.android.synthetic.main.fragment_tile_calendar.*
import kotlinx.android.synthetic.main.fragment_tile_calendar.view.*

internal class CalendarTileFragment : MvpAppCompatFragment(), ContractTileView, CalendarUpdateContract {

    @InjectPresenter(tag = "TilePresenter", type = PresenterType.GLOBAL)
    lateinit var presenter: TilePresenter

    lateinit var tileView: View

    private lateinit var adapter: PagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        presenter.viewIsCreated()
        setChangePageListener()
        initHelper()
    }

    override fun setPageAdapter() {
        tileView.holidayTilePager.adapter = getAdapter()
        tileView.holidayTilePager.currentItem = getMonth()
    }

    override fun initSpinner(){
        val mNames = getMonthsNames()
        val adapter = SpinnerAdapter(context!!, R.layout.spinner_year_item, mNames)
        adapter.setDropDownViewResource(R.layout.spinner_year_dropdown)
        tileView.monthSpinner.adapter = adapter
        val monthNum = getMonth()
        tileView.monthSpinner.setSelection(monthNum)
        setOnItemSpinnerSelected()
    }

    private fun getYear() = arguments!!.getInt(Constants.Keys.YEAR.value, Time().year)
    private fun getMonth() = arguments!!.getInt(Constants.Keys.MONTH.value, Time().monthWith0)
    private fun getDay() = arguments!!.getInt(Constants.Keys.DAY.value, Time().dayOfMonth)
    private fun getMonthsNames() = resources.getStringArray(R.array.months_names_gen)


    private fun setOnItemSpinnerSelected(){
        tileView.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                tileView.holidayTilePager.apply {
                    if (currentItem != position) {
                        currentItem = position
                    }
                }
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {}
        }
    }

    private fun getAdapter(): PagerAdapter {
        return CustomViewPager.CustomPagerAdapter(childFragmentManager)
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
        val lastPosition = MONTH_SIZE - 1

        when (position) {
            lastPosition -> arrowRight.visibility = View.GONE
            firstPosition -> arrowLeft.visibility = View.GONE
            else -> {
                arrowLeft.visibility = View.VISIBLE
                arrowRight.visibility = View.VISIBLE
            }
        }
    }

    private fun initHelper(){
        tileView.helperButton.setOnClickListener{
            val fr = CalendarInfoFragment()
            val transaction = fragmentManager!!.beginTransaction()
            fr.show(transaction, "helper")
        }
    }

    override fun updateYear() {
        tileView.holidayTilePager.adapter?.notifyDataSetChanged()
    }

    override fun updateMonth() {
        val position = getMonth()
        tileView.holidayTilePager.setCurrentItem(position, false)
    }

    override fun updateDay() {

    }
}
