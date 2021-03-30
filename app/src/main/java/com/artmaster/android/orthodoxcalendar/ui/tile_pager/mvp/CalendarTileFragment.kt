package com.artmaster.android.orthodoxcalendar.ui.tile_pager.mvp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.artmaster.android.orthodoxcalendar.R
import com.artmaster.android.orthodoxcalendar.common.Constants
import com.artmaster.android.orthodoxcalendar.common.Constants.Companion.MONTH_SIZE
import com.artmaster.android.orthodoxcalendar.common.SpinnerAdapter
import com.artmaster.android.orthodoxcalendar.databinding.FragmentTileCalendarBinding
import com.artmaster.android.orthodoxcalendar.domain.Time
import com.artmaster.android.orthodoxcalendar.ui.CalendarUpdateContract
import com.artmaster.android.orthodoxcalendar.ui.CustomViewPager
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.fragment.CalendarInfoFragment
import com.artmaster.android.orthodoxcalendar.ui.tile_pager.impl.ContractTileView
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

internal class CalendarTileFragment : MvpAppCompatFragment(), ContractTileView, CalendarUpdateContract {

    @InjectPresenter(tag = "TilePresenter")
    lateinit var presenter: TilePresenter

    private lateinit var adapter: PagerAdapter

    private var _binding: FragmentTileCalendarBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!presenter.isInRestoreState(this)) {
            presenter.attachView(this)
            presenter.viewIsReady()
        }
        adapter = getAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, groupContainer: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTileCalendarBinding.inflate(inflater, groupContainer, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.viewIsCreated()
        setChangePageListener()
        initHelper()
    }

    override fun setPageAdapter() {
        binding.holidayTilePager.adapter = getAdapter()
        binding.holidayTilePager.currentItem = getMonth()
    }

    override fun initSpinner() {
        val mNames = getMonthsNames()
        val adapter = SpinnerAdapter(requireContext(), R.layout.spinner_year_item, mNames)
        adapter.setDropDownViewResource(R.layout.spinner_year_dropdown)
        binding.monthSpinner.adapter = adapter
        val monthNum = getMonth()
        binding.monthSpinner.setSelection(monthNum)
        setOnItemSpinnerSelected()
    }

    private fun getYear() = requireArguments().getInt(Constants.Keys.YEAR.value, Time().year)
    private fun getMonth() = requireArguments().getInt(Constants.Keys.MONTH.value, Time().monthWith0)
    private fun getDay() = requireArguments().getInt(Constants.Keys.DAY.value, Time().dayOfMonth)
    private fun getMonthsNames() = resources.getStringArray(R.array.months_names_gen)


    private fun setOnItemSpinnerSelected() {
        binding.monthSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedItemView: View, position: Int, id: Long) {
                binding.holidayTilePager.apply {
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
        binding.holidayTilePager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}
            override fun onPageSelected(position: Int) {
                binding.monthSpinner.setSelection(position)
                setVisibleArrows(position)
            }
        })
    }

    private fun setVisibleArrows(position: Int){
        val firstPosition = 0
        val lastPosition = MONTH_SIZE - 1

        when (position) {
            lastPosition -> binding.arrowRight.visibility = View.GONE
            firstPosition -> binding.arrowLeft.visibility = View.GONE
            else -> {
                binding.arrowLeft.visibility = View.VISIBLE
                binding.arrowRight.visibility = View.VISIBLE
            }
        }
    }

    private fun initHelper(){
        binding.helperButton.setOnClickListener {
            val fr = CalendarInfoFragment()
            val transaction = parentFragmentManager.beginTransaction()
            fr.show(transaction, "helper")
        }
    }

    override fun updateYear() {
        binding.holidayTilePager.adapter?.notifyDataSetChanged()
    }

    override fun updateMonth() {
        val position = getMonth()
        binding.holidayTilePager.setCurrentItem(position, false)
    }

    override fun updateDay() {

    }
}
