package com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments

import android.animation.Animator
import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.databinding.HolidayListPagerBinding
import com.artmaster.android.orthodoxcalendar.domain.Filter
import com.artmaster.android.orthodoxcalendar.domain.SharedTime
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.impl.ListViewDiffContract
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.list.HolidayListFragment
import com.artmaster.android.orthodoxcalendar.ui.calendar_list.fragments.shared.CalendarViewModel
import dagger.android.support.AndroidSupportInjection


class ListHolidayPager : Fragment(), ListViewDiffContract.ViewListPager {

    private lateinit var adapter: FragmentStateAdapter

    private var changedCallback: ((Int, Int) -> Unit)? = null

    private val years = getYears()
    private var filters = ArrayList<Filter>()
    private var time = SharedTime()

    private var _binding: HolidayListPagerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: CalendarViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeToDataUpdate()

        time = requireArguments().getParcelable(Constants.Keys.TIME.value) ?: SharedTime()
    }

    private fun subscribeToDataUpdate() {
        viewModel.filters.observe(this, { item ->
            filters.clear()
            filters.addAll(item.toList())
            setPageAdapter()
            binding.holidayListPager.invalidate()
        })

        viewModel.time.observe(this, { item ->
            if (SharedTime.isTimeChanged(time, item)) {
                if (item.year != time.year) {
                    binding.holidayListPager.currentItem = getPosition(item.year)
                }
                time = item
            }
        })
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

    private fun getPosition() = years.indexOf(time.year)
    private fun getPosition(year: Int) = years.indexOf(year)

    private fun setPageAdapter() {
        adapter = getAdapter(this)
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
                bundle.putParcelableArrayList(Constants.Keys.FILTERS.value, filters)
                fragment.arguments = bundle

                return fragment
            }
        }
    }

    override fun onChangePageListener(body: (Int, Int) -> Unit) {
        changedCallback = body
    }

    private fun setChangePageListener() {
        binding.holidayListPager.registerOnPageChangeCallback(object : OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                time.year = years[position]
                changedCallback?.invoke(position, time.year)
            }
        })
    }

    private fun updateYear() {
        val years = getYears()
        val pos = years.indexOf(time.year)

        binding.holidayListPager.currentItem = pos
        //binding.holidayListPager.setCurrentItem(pos, 3000)
    }

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
}