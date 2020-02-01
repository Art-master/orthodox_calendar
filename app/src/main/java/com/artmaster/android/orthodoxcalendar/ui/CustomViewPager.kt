package com.artmaster.android.orthodoxcalendar.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_SIZE
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.VIEW_PAGER_SPEED
import com.artmaster.android.orthodoxcalendar.ui.tile_month.mvp.CalendarTileMonthFragment

class CustomViewPager(context: Context, attrs: AttributeSet) : ViewPager(context, attrs) {

    init {
        setMyScroller()
    }

    private fun setMyScroller() {
        try {
            val viewpager = ViewPager::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, MyScroller(context))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class MyScroller(context: Context) : Scroller(context, DecelerateInterpolator()) {

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, VIEW_PAGER_SPEED)
        }
    }

    class CustomPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

        private var fragments: ArrayList<Fragment> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return if (position <= fragments.size - 1) {
                this.fragments[position]
            } else {
                val fragment = CalendarTileMonthFragment()
                val args = Bundle()
                args.putInt(Constants.Keys.MONTH.value, position)
                fragment.arguments = args
                fragment
            }
        }

        override fun getCount(): Int = MONTH_SIZE

        override fun getItemPosition(`object`: Any): Int {
            return POSITION_NONE
        }
    }
}