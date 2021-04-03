package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.Scroller
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.databinding.HolidayListPagerBinding
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.CalendarUpdateContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.HolidayListFragment
import dagger.android.support.AndroidSupportInjection


class ListHolidayPager : Fragment(), ListViewDiffContract.ViewListPager, CalendarUpdateContract {

    private lateinit var adapter: FragmentStateAdapter

    private var changedCallback: ((Int) -> Unit)? = null

    private val years = getYears()

    private var _binding: HolidayListPagerBinding? = null
    private val binding get() = _binding!!

    init {
        setMyScroller()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = getAdapter(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        AndroidSupportInjection.inject(this)
        _binding = HolidayListPagerBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setPageAdapter()
    }

    override fun onResume() {
        super.onResume()
        binding.holidayListPager.apply {
            currentItem = getPosition()
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
        }
    }

    private fun getPosition(): Int {
        return years.indexOf(getYear())
    }

    private fun setPageAdapter() {
        if (binding.holidayListPager.adapter != null) return
        binding.holidayListPager.adapter = adapter
        binding.holidayListPager.currentItem = getPosition()
        setChangePageListener()
    }

    private fun getYears(): ArrayList<Int> {
        val size = Constants.HolidayList.PAGE_SIZE.value
        val firstYear = Time().year - size / 2
        val years = ArrayList<Int>(size)
        for (element in firstYear until firstYear + size) {
            years.add(element)
        }
        return years
    }

    private fun getAdapter(fa: Fragment): FragmentStateAdapter {

        return object : FragmentStateAdapter(fa) {
            override fun getItemCount() = Constants.HolidayList.PAGE_SIZE.value

            override fun createFragment(position: Int): Fragment {
                val fragment = HolidayListFragment()

                val bundle = Bundle()
                bundle.putInt(Constants.Keys.YEAR.value, years[position])
                fragment.arguments = bundle

                return fragment
            }
        }
    }

    override fun onChangePageListener(body: (Int) -> Unit) {
        changedCallback = body
    }

    private fun setChangePageListener() {
        binding.holidayListPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                changedCallback?.invoke(position)
            }
        })
    }

    private fun getYear() = requireArguments().getInt(Constants.Keys.YEAR.value, Time().year)

    override fun updateYear() {
        val years = getYears()
        val pos = years.indexOf(getYear())

        binding.holidayListPager.currentItem = pos
        //binding.holidayListPager.setCurrentItem(pos, 3000)
    }

    /**
     * Reduces drag sensitivity of [ViewPager2] widget
     */
/*    fun ViewPager2.reduceDragSensitivity() {
        val recyclerViewField = ViewPager2::class.java.getDeclaredField("mRecyclerView")
        recyclerViewField.isAccessible = true
        val recyclerView = recyclerViewField.get(this) as RecyclerView

        val touchSlopField = RecyclerView::class.java.getDeclaredField("mTouchSlop")
        touchSlopField.isAccessible = true
        val touchSlop = touchSlopField.get(recyclerView) as Int
        touchSlopField.set(recyclerView, touchSlop*8)       // "8" was obtained experimentally
    }*/

    fun ViewPager2.setCurrentItem(
            item: Int,
            duration: Long,
            interpolator: TimeInterpolator = DecelerateInterpolator(),
            pagePxWidth: Int = width // Default value taken from getWidth() from ViewPager2 view
    ) {
        val pxToDrag: Int = pagePxWidth * (item - currentItem)
        val animator = ValueAnimator.ofInt(0, pxToDrag)
        var previousValue = 0
        animator.addUpdateListener { valueAnimator ->
            val currentValue = valueAnimator.animatedValue as Int
            val currentPxToDrag = (currentValue - previousValue).toFloat()
            fakeDragBy(-currentPxToDrag)
            previousValue = currentValue
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                beginFakeDrag()
            }

            override fun onAnimationEnd(animation: Animator?) {
                endFakeDrag()
            }

            override fun onAnimationCancel(animation: Animator?) {  /*Ignored*/
            }

            override fun onAnimationRepeat(animation: Animator?) {  /*Ignored*/
            }
        })
        animator.interpolator = interpolator
        animator.duration = duration
        animator.start()
    }


    private fun setMyScroller() {
        try {
            val viewpager = ViewPager2::class.java
            val scroller = viewpager.getDeclaredField("mScroller")
            scroller.isAccessible = true
            scroller.set(this, MyScroller(requireContext()))
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    inner class MyScroller(context: Context) : Scroller(context, DecelerateInterpolator()) {

        override fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, duration: Int) {
            super.startScroll(startX, startY, dx, dy, Constants.VIEW_PAGER_SPEED)
        }
    }

    override fun updateMonth() {

    }

    override fun updateDay() {

    }
}