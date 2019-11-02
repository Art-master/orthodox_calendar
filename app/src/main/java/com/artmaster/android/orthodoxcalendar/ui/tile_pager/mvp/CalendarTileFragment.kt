package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentStatePagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.arellomobile.mvp.MvpAppCompatFragment
import com.arellomobile.mvp.presenter.InjectPresenter
import com.arellomobile.mvp.presenter.PresenterType
import com.artmaster.android.orthodoxcalendar.R
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
        //setPageAdapter()
    }

    override fun onResume() {
        super.onResume()
    }

    override fun setPageAdapter() {
        if(tileView.holidayTilePager.adapter != null) return
        tileView.holidayTilePager.adapter =  adapter
        setCurrentItem()
    }

    private fun getAdapter(): FragmentStatePagerAdapter {
        //if(!isAdded) return null
        return object :FragmentStatePagerAdapter(childFragmentManager) {

            override fun getItem(p0: Int): Fragment {
                val fragment = CalendarTileMonthFragment()
                return fragment
            }

            override fun getCount(): Int = 10 //month size
        }
    }

    private fun setCurrentItem() {
        //val uuid = intent.getSerializableExtra(Constants.Keys.HOLIDAY_ID.name)
       // holidayTilePager.currentItem = index
    }
}
